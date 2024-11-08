package com.app.vakna.controller

import android.content.Context
import android.content.Intent
import android.text.InputFilter
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.app.vakna.adapters.GridConsommableAdapterInventaire
import com.app.vakna.adapters.PlaceholderAdapter
import com.app.vakna.adapters.PlaceholderData
import com.app.vakna.databinding.FragmentCompagnonBinding
import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.GestionnaireDeCompagnons
import com.app.vakna.modele.GestionnaireDeRefuge
import com.app.vakna.modele.Inventaire
import com.app.vakna.modele.ObjetObtenu
import com.app.vakna.modele.Shop
import com.app.vakna.modele.TypeObjet
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.ui.compagnon.CompagnonFragment
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
    private lateinit var compagnon: Compagnon
    private lateinit var compagnonsSup: Array<Compagnon>
    private var inventaire = Inventaire(context)
    private val shop = Shop(context)

    /**
     * Initialise l'interface utilisateur pour afficher les informations du compagnon.
     * Charge les données depuis la base et configure les éléments graphiques.
     */
    init {
        inventaire.ajouterPieces(500)

        setUpView()

        binding.editNameButton.setOnClickListener {
            showEditNameDialog()
        }
    }

    private fun setUpView() {

        // Charger le compagnon depuis la base de données
        val compagnons = gestionnaire.obtenirCompagnons()
        if (gestionnaire.obtenirActif() == null) {
            compagnon = compagnons.first()
            gestionnaire.setActif(compagnon.id)
        } else {
            compagnon = gestionnaire.obtenirActif()!!
        }

        compagnon.let {
            binding.dragonName.text = it.nom
            updateLevelAndProgress(it)
        }

        updateRefuge(binding)
        updateHumeurCompagnon(binding, compagnon)

        compagnon.let {
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
        setupGridView(items, distinctTypesList.first(), binding)

        // Gérer la sélection d'onglets (Jouets / Nourriture)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                inventaire = Inventaire(context)

                val selectedTypeName = tab?.text.toString()

                val selectedType = TypeObjet.valueOf(selectedTypeName)

                val filteredItems = inventaire.getObjetsParType(selectedType)

                setupGridView(filteredItems, selectedType, binding)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        compagnonsSup = (compagnons - compagnon).toTypedArray()

        var iteration = 0
        compagnonsSup.forEach {
            val switchBoutons = listOf(
                binding.switchCompagnon1,
                binding.switchCompagnon2,
                binding.switchCompagnon3,
                binding.switchCompagnon4,
                binding.switchCompagnon5
            )

            val bouton = switchBoutons[iteration]
            val appearancePath = it.apparenceDefaut()

            Glide.with(context)
                .asGif()
                .load(appearancePath)
                .into(bouton)
            val id = iteration
            bouton.setOnClickListener { view ->
                val appearancePathNew = compagnon.apparenceDefaut()

                Glide.with(context)
                    .asGif()
                    .load(appearancePathNew)
                    .into(bouton)

                val newCompagnon = compagnonsSup[id]
                compagnonsSup[id] = compagnon
                gestionnaire.setActif(newCompagnon.id)
                compagnon = newCompagnon

                newCompagnon.let { comp ->
                    binding.dragonName.text = comp.nom
                    updateLevelAndProgress(comp)
                }

                updateHumeurCompagnon(binding, newCompagnon)

                // Mettre à jour les progress bar
                newCompagnon.let { comp ->
                    binding.texteHumeur.text = context.getString(R.string.humeur_text, comp.humeur)
                    binding.progressHumeur.progress = comp.humeur
                    binding.texteFaim.text = context.getString(R.string.faim_text, comp.faim)
                    binding.progressFaim.progress = comp.faim
                }

                val items = inventaire.getObjetsParType(distinctTypesList.first())
                setupGridView(items, distinctTypesList.first(), binding)
            }

            if(iteration + 1 <= 4) {
                val boutonNext = switchBoutons[iteration + 1]
                boutonNext.visibility = View.VISIBLE

                boutonNext.setOnClickListener {view ->
                    val popupMagasinView =
                        LayoutInflater.from(context).inflate(R.layout.popup_acheter_compagnon, null)

                    val popupMagasinWindow = PopupWindow(
                        popupMagasinView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    popupMagasinView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    val popupWidth = popupMagasinView.measuredWidth
                    val popupHeight = popupMagasinView.measuredHeight

                    val location = IntArray(2)
                    view.getLocationOnScreen(location)

                    val offsetX = location[0] + view.width
                    val offsetY = location[1] - (popupHeight / 2) + (view.height / 2)

                    popupMagasinWindow.isOutsideTouchable = true
                    popupMagasinWindow.isFocusable = true

                    popupMagasinWindow.showAtLocation(view, Gravity.NO_GRAVITY, offsetX, offsetY)

                    val boutonMagasin: Button = popupMagasinView.findViewById(R.id.boutonMagasin)
                    boutonMagasin.setOnClickListener {
                        if (context is MainActivity) {
                            val navController = context.findNavController(R.id.nav_host_fragment_activity_main)
                            navController.navigate(R.id.navigation_notifications)
                        }
                        popupMagasinWindow.dismiss()
                    }

                    popupMagasinView.setOnClickListener {
                        popupMagasinWindow.dismiss()
                    }
                }
            }
            iteration++
        }
    }

    /**
     * Affiche une boîte de dialogue permettant de modifier le nom du compagnon.
     * Le nouveau nom est sauvegardé dans la base de données et mis à jour dans l'interface utilisateur.
     */
    fun showEditNameDialog() {
        val editText = EditText(context).apply {
            hint = context.getString(R.string.new_name_hint)
            inputType = android.text.InputType.TYPE_CLASS_TEXT
            filters = arrayOf(android.text.InputFilter.LengthFilter(16))
            compagnon.let {
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
                    compagnon.let { gestionnaire.modifierNom(it.id, newName) }
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
        fun updateHumeurCompagnon(binding: FragmentCompagnonBinding, compagnon: Compagnon) {
            val context = binding.root.context
            val gestionnaire = GestionnaireDeCompagnons(CompagnonDAO(context))
            val compagnons = gestionnaire.obtenirCompagnons()
            val compagnonUpd: Compagnon
            if (gestionnaire.obtenirActif() == null) {
                compagnonUpd = compagnons.first()
                gestionnaire.setActif(compagnon.id)
            } else {
                compagnonUpd = gestionnaire.obtenirActif()!!
            }
            val fichierApparence = compagnonUpd.apparence()
            println(fichierApparence)


            // Charger et afficher un GIF via Glide
            Glide.with(context)
                .asGif()
                .load(fichierApparence)
                .into(binding.dragonGif)
        }

        fun updateRefuge(binding: FragmentCompagnonBinding) {
            val context = binding.root.context
            val gestionnaire = GestionnaireDeRefuge(context)
            val refuges = gestionnaire.getRefuges()
            val refuge = if (refuges.isNotEmpty()) refuges.first() else null
            val fichierApparence = refuge?.apparence()

            // Charger et afficher un GIF via Glide
            Glide.with(context)
                .load(fichierApparence)
                .into(binding.refuge)
        }

        /**
         * Configure le GridView pour afficher la liste des items (jouets ou nourriture).
         * @param items La liste d'items à afficher dans le GridView.
         */
        fun setupGridView(items: List<ObjetObtenu>, type: TypeObjet, binding: FragmentCompagnonBinding) {
            val context = binding.root.context
            val gestionnaire = GestionnaireDeCompagnons(CompagnonDAO(context))
            val compagnons = gestionnaire.obtenirCompagnons()
            var compagnonGrid = gestionnaire.obtenirActif()
            if (compagnonGrid == null) {
                compagnonGrid = compagnons.first()
            }

            compagnonGrid.let {
                binding.texteHumeur.text = binding.root.context.getString(R.string.humeur_text, it.humeur)
                binding.progressHumeur.progress = it.humeur
                binding.texteFaim.text = binding.root.context.getString(R.string.faim_text, it.faim)
                binding.progressFaim.progress = it.faim
            }

            if(items.isEmpty()) {

                binding.gridViewItems.numColumns = 1

                val placeholderMessage = when (type) {
                    TypeObjet.JOUET -> "Vous n'avez aucun jouet disponible! Allez au magasin pour en acheter."
                    TypeObjet.NOURRITURE -> "Vous n'avez aucune nourriture disponible! Allez au magasin pour en acheter."
                }

                val placeholderItem = PlaceholderData(
                    message = placeholderMessage,
                    buttonText = "Magasin",
                    buttonAction = {
                        if (context is MainActivity) {
                            val navController = context.findNavController(R.id.nav_host_fragment_activity_main)
                            navController.navigate(R.id.navigation_notifications)
                        }
                    }
                )

                val gridItems = listOf(placeholderItem)

                val adapter = PlaceholderAdapter(binding, gridItems)
                binding.gridViewItems.adapter = adapter
            } else {

                binding.gridViewItems.numColumns = 2

                val sortedItems = items.sortedWith(compareBy<ObjetObtenu> { it.getType() }.thenBy { it.getNom() })

                val gridItems = Inventaire.setToGridDataArray(sortedItems)

                val adapter = GridConsommableAdapterInventaire(binding, gridItems)
                binding.gridViewItems.adapter = adapter
            }
        }
    }
}
