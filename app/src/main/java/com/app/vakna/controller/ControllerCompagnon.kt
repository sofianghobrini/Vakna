package com.app.vakna.controller

import android.content.Context
import android.widget.EditText
import com.app.vakna.R
import com.app.vakna.adapters.GridConsommableAdapterInventaire
import com.app.vakna.databinding.FragmentCompagnonBinding
import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.GestionnaireDeCompagnons
import com.app.vakna.modele.Inventaire
import com.app.vakna.modele.ObjetObtenu
import com.app.vakna.modele.Shop
import com.app.vakna.modele.TypeObjet
import com.app.vakna.modele.dao.CompagnonDAO
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout

/**
 * Contrôleur pour gérer les interactions avec le compagnon dans l'application.
 * @param binding Le binding associé au fragment compagnon pour accéder aux vues.
 */
class ControllerCompagnon(private val binding: FragmentCompagnonBinding) {

    private val context: Context = binding.root.context
    private var gestionnaire = GestionnaireDeCompagnons(CompagnonDAO(context))
    private var compagnon: Compagnon? = null
    private var inventaire = Inventaire(context)
    private val shop = Shop(context)

    /**
     * Initialise l'interface utilisateur pour afficher les informations du compagnon.
     * Charge les données depuis la base et configure les éléments graphiques.
     */
    init {

        setUpView()

        binding.editNameButton.setOnClickListener {
            showEditNameDialog()
        }
    }

    private fun setUpView() {

        // Charger le compagnon depuis la base de données
        val compagnons = gestionnaire.obtenirCompagnons()
        compagnon = if (compagnons.isNotEmpty()) compagnons.first() else null

        // Mettre à jour l'affichage du nom et du niveau du compagnon
        compagnon?.let {
            binding.dragonName.text = it.nom
            updateLevelAndProgress(it) // Mise à jour du niveau et progression XP
        }

        updateHumeurCompagnon(binding)

        // Mettre à jour les progress bar
        compagnon?.let {
            binding.texteHumeur.text = context.getString(R.string.humeur_text, it.humeur)
            binding.progressHumeur.progress = it.humeur
            binding.texteFaim.text = context.getString(R.string.faim_text, it.faim)
            binding.progressFaim.progress = it.faim
        }

        val distinctTypesList = shop.getObjets().map { it.getType() }.distinct()

        distinctTypesList.forEach {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(it.name))
        }

        val items = inventaire.getObjetsParType(distinctTypesList.first())
        setupGridView(items, binding)

        // Gérer la sélection d'onglets (Jouets / Nourriture)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                inventaire = Inventaire(context)

                val selectedTypeName = tab?.text.toString()

                val selectedType = TypeObjet.valueOf(selectedTypeName)

                val filteredItems = inventaire.getObjetsParType(selectedType)

                setupGridView(filteredItems, binding)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    /**
     * Affiche une boîte de dialogue permettant de modifier le nom du compagnon.
     * Le nouveau nom est sauvegardé dans la base de données et mis à jour dans l'interface utilisateur.
     */
    fun showEditNameDialog() {
        val editText = EditText(context).apply {
            hint = context.getString(R.string.new_name_hint)
            inputType = android.text.InputType.TYPE_CLASS_TEXT
            filters = arrayOf(android.text.InputFilter.LengthFilter(50))
            compagnon?.let {
                setText(it.nom)  // Pré-remplir avec le nom actuel du compagnon
            }
        }

        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.edit_name_dialog_title))
            .setView(editText)
            .setPositiveButton(context.getString(R.string.confirm)) { dialog, _ ->
                val newName = editText.text.toString()
                if (newName.isNotEmpty()) {
                    // Mettre à jour le nom du compagnon dans la base de données
                    compagnon?.let { gestionnaire.modifierNom(it.id, newName) }
                    binding.dragonName.text = newName
                }
                dialog.dismiss()
            }
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * Met à jour le niveau du compagnon et l'affichage de la progression en fonction de l'XP.
     * @param compagnon Le compagnon pour lequel le niveau et la progression doivent être mis à jour.
     */
    private fun updateLevelAndProgress(compagnon: Compagnon) {
        val currentXp = compagnon.xp
        val level = currentXp / 100  // Calculer le niveau à partir de l'XP
        val xpForCurrentLevel = currentXp % 100  // XP restant pour compléter le niveau actuel

        // Mettre à jour le texte du niveau
        binding.dragonLevel.text = context.getString(R.string.level_text, level)

        // Mettre à jour la barre de progression avec l'XP restant pour le niveau
        binding.progressBarLevel.progress = xpForCurrentLevel
    }

    companion object {
        fun updateHumeurCompagnon(binding: FragmentCompagnonBinding) {
            val context = binding.root.context
            val gestionnaire = GestionnaireDeCompagnons(CompagnonDAO(context))
            val compagnons = gestionnaire.obtenirCompagnons()
            val compagnon = if (compagnons.isNotEmpty()) compagnons.first() else null
            val fichierApparence = compagnon?.apparence()


            // Charger et afficher un GIF via Glide
            Glide.with(context)
                .asGif()
                .load(fichierApparence)
                .into(binding.dragonGif)
        }

        /**
         * Configure le GridView pour afficher la liste des items (jouets ou nourriture).
         * @param items La liste d'items à afficher dans le GridView.
         */
        fun setupGridView(items: List<ObjetObtenu>, binding: FragmentCompagnonBinding) {
            val gestionnaire = GestionnaireDeCompagnons(CompagnonDAO(binding.root.context))
            val compagnons = gestionnaire.obtenirCompagnons()
            val compagnon = if (compagnons.isNotEmpty()) compagnons.first() else null
            compagnon?.let {
                binding.texteHumeur.text = binding.root.context.getString(R.string.humeur_text, it.humeur)
                binding.progressHumeur.progress = it.humeur
                binding.texteFaim.text = binding.root.context.getString(R.string.faim_text, it.faim)
                binding.progressFaim.progress = it.faim
            }

            val sortedItems = items.sortedWith(compareBy<ObjetObtenu> { it.getType() }.thenBy { it.getNom() })

            val gridItems = Inventaire.setToGridDataArray(sortedItems)

            val adapter = GridConsommableAdapterInventaire(binding, gridItems)
            binding.gridViewItems.adapter = adapter
        }
    }
}
