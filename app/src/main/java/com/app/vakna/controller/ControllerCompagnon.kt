package com.app.vakna.controller

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.app.vakna.R
import com.app.vakna.adapters.GridAdapterInventaire
import com.app.vakna.databinding.FragmentCompagnonBinding
import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.GestionnaireDeCompagnons
import com.app.vakna.modele.Inventaire
import com.app.vakna.modele.Objet
import com.app.vakna.modele.ObjetObtenu
import com.app.vakna.modele.Shop
import com.app.vakna.modele.TypeObjet
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.ObjetDAO
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import kotlin.random.Random

/**
 * Contrôleur pour gérer les interactions avec le compagnon dans l'application.
 * @param binding Le binding associé au fragment compagnon pour accéder aux vues.
 */
class ControllerCompagnon(private val binding: FragmentCompagnonBinding) {

    private val context: Context = binding.root.context
    private val gestionnaire = GestionnaireDeCompagnons(CompagnonDAO(context))
    private var compagnon: Compagnon? = null
    private val inventaire = Inventaire(context)
    private val shop = Shop(context)

    /**
     * Initialise l'interface utilisateur pour afficher les informations du compagnon.
     * Charge les données depuis la base et configure les éléments graphiques.
     */
    init {
        Log.e("test", inventaire.getObjetParNom("Jouet 4")?.getQuantite().toString())

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

        // Charger et afficher un GIF via Glide
        Glide.with(context)
            .asGif()
            .load(R.drawable.dragon)
            .into(binding.dragonGif)

        val distinctTypesList = shop.getObjets().map { it.getType() }
            .distinct()

        distinctTypesList.forEach {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(it.name))
        }

        val items = inventaire.getObjetsParType(distinctTypesList.first())
        setupGridView(items)

        // Gérer la sélection d'onglets (Jouets / Nourriture)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedTypeName = tab?.text.toString()

                val selectedType = TypeObjet.valueOf(selectedTypeName)

                val filteredItems = inventaire.getObjetsParType(selectedType)

                setupGridView(filteredItems)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    /**
     * Configure le GridView pour afficher la liste des items (jouets ou nourriture).
     * @param items La liste d'items à afficher dans le GridView.
     */
    private fun setupGridView(items: List<ObjetObtenu>) {
        val sortedItems = items.sortedWith(compareBy<ObjetObtenu> { it.getType() }.thenBy { it.getNom() })

        // Convert sorted items to GridData format
        val gridItems = Inventaire.setToGridDataArray(sortedItems)

        // Set up the GridAdapterInventaire with the sorted items
        val adapter = GridAdapterInventaire(context, gridItems)
        binding.gridViewItems.adapter = adapter
    }

    /**
     * Affiche une boîte de dialogue permettant de modifier le nom du compagnon.
     * Le nouveau nom est sauvegardé dans la base de données et mis à jour dans l'interface utilisateur.
     */
    fun showEditNameDialog() {
        val editText = EditText(context).apply {
            hint = "Nouveau nom"
            inputType = android.text.InputType.TYPE_CLASS_TEXT
            filters = arrayOf(android.text.InputFilter.LengthFilter(50))
            compagnon?.let {
                setText(it.nom)  // Pré-remplir avec le nom actuel du compagnon
            }
        }

        MaterialAlertDialogBuilder(context)
            .setTitle("Modifier le nom de ton Companion")
            .setView(editText)
            .setPositiveButton("Confirmer") { dialog, _ ->
                val newName = editText.text.toString()
                if (newName.isNotEmpty()) {
                    // Mettre à jour le nom du compagnon dans la base de données
                    compagnon?.let { gestionnaire.modifierNom(it.id, newName) }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { dialog, _ ->
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
        binding.dragonLevel.text = "Niv. $level"

        // Mettre à jour la barre de progression avec l'XP restant pour le niveau
        binding.progressBarLevel.progress = xpForCurrentLevel
    }
}
