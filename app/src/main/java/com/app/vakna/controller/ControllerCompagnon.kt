package com.app.vakna.controller

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.InputFilter
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
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
import com.app.vakna.modele.Personnalite
import com.app.vakna.modele.Refuge
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
    private val gestionnaireRefuge = GestionnaireDeRefuge(context)

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
        showPersonnalite(gestionnaire, binding.root, compagnon.id)

        compagnon.let {
            when {
                it.humeur > 60 -> {
                    binding.imageBonheur.setImageResource(R.drawable.humeur_0)
                }
                it.humeur > 30 -> {
                    binding.imageBonheur.setImageResource(R.drawable.humeur_1)
                }
                it.humeur > 0 -> {
                    binding.imageBonheur.setImageResource(R.drawable.humeur_2)
                }
                it.humeur == 0 -> {
                    binding.imageBonheur.setImageResource(R.drawable.humeur_3)
                }
            }
            when {
                it.faim > 60 -> {
                    binding.imageFaim.setImageResource(R.drawable.faim_0)
                }
                it.faim > 30 -> {
                    binding.imageFaim.setImageResource(R.drawable.faim_1)
                }
                it.faim > 0 -> {
                    binding.imageFaim.setImageResource(R.drawable.faim_2)
                }
                it.faim == 0 -> {
                    binding.imageFaim.setImageResource(R.drawable.faim_3)
                }
            }
        }

        val distinctTypesList = shop.getObjets().map { it.getType() }.distinct()

        distinctTypesList.forEach {
            val tabTitle = getTabTitle(it)
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(tabTitle))
        }

        val items = inventaire.getObjetsParType(distinctTypesList.first())
        setupGridView(items, distinctTypesList.first(), binding)


        binding.freeCompagnonButton.setOnClickListener{
            val campagnonRestant = gestionnaire.obtenirCompagnons()
            if(campagnonRestant.size > 1) { showDialogRelease() }
            else {
                Toast.makeText(context, context.getString(R.string.un_campagnon_restant), Toast.LENGTH_SHORT).show()
            }
        }

        // Gérer la sélection d'onglets (Jouets / Nourriture)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                inventaire = Inventaire(context)

                val selectedTypeName = tab?.text.toString()

                val selectedType = when (selectedTypeName) {
                    context.getString(R.string.tab_toys) -> TypeObjet.JOUET
                    context.getString(R.string.tab_food) -> TypeObjet.NOURRITURE
                    else -> TypeObjet.JOUET
                }

                val filteredItems = inventaire.getObjetsParType(selectedType)

                setupGridView(filteredItems, selectedType, binding)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        compagnonsSup = (compagnons - compagnon).toTypedArray()

        if (compagnonsSup.isEmpty()) {
            binding.switchCompagnon1.setOnClickListener {
                showCompagnonPopUp(it)
            }
        }

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
            val appearancePath = it.apparence()

            Glide.with(context)
                .asGif()
                .load(appearancePath)
                .into(bouton)
            val id = iteration
            bouton.setOnClickListener { view ->
                val appearancePathNew = compagnon.apparence()

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
                    when {
                        it.humeur > 60 -> {
                            binding.imageBonheur.setImageResource(R.drawable.humeur_0)
                        }
                        it.humeur > 30 -> {
                            binding.imageBonheur.setImageResource(R.drawable.humeur_1)
                        }
                        it.humeur > 0 -> {
                            binding.imageBonheur.setImageResource(R.drawable.humeur_2)
                        }
                        it.humeur == 0 -> {
                            binding.imageBonheur.setImageResource(R.drawable.humeur_3)
                        }
                    }
                    when {
                        it.faim > 60 -> {
                            binding.imageFaim.setImageResource(R.drawable.faim_0)
                        }
                        it.faim > 30 -> {
                            binding.imageFaim.setImageResource(R.drawable.faim_1)
                        }
                        it.faim > 0 -> {
                            binding.imageFaim.setImageResource(R.drawable.faim_2)
                        }
                        it.faim == 0 -> {
                            binding.imageFaim.setImageResource(R.drawable.faim_3)
                        }
                    }
                }

                showPersonnalite(gestionnaire, binding.root, compagnon.id)

                val items = inventaire.getObjetsParType(distinctTypesList.first())
                setupGridView(items, distinctTypesList.first(), binding)
            }

            if(iteration + 1 <= 4) {
                val boutonNext = switchBoutons[iteration + 1]
                boutonNext.visibility = View.VISIBLE

                boutonNext.setOnClickListener {view ->
                    showCompagnonPopUp(view)
                }
            }
            iteration++

        }

        val refuges = gestionnaireRefuge.getRefuges()
        val refuge = gestionnaireRefuge.getActif()

        refuge?.let {
            Glide.with(context)
                .load(refuge.apparence())
                .into(binding.refuge)
        } ?: {
            if(refuges.isNotEmpty()) {
                val nouveauActif = refuges.first()
                gestionnaireRefuge.setActif(nouveauActif.getId())
            }
        }
        when {
            refuges.isEmpty() -> {
                binding.switchRefuge.setOnClickListener {
                    showAcheterRefugeDialog()
                }
            }
            refuges.size == 1 -> {
                binding.switchRefuge.setOnClickListener {
                    showAcheterRefugeDialog()
                }
            }
            refuges.size > 1 -> {
                binding.switchRefuge.setImageResource(R.drawable.changer_refuges)
                binding.switchRefuge.setPadding(18,18,18,18)
                binding.switchRefuge.setOnClickListener {
                    showSelectRefugeDialog()
                }
            }
        }

        if(refuges.isEmpty()) {
            binding.switchRefuge.setOnClickListener {
                showAcheterRefugeDialog()
            }
        }

        if(refuges.size == 1) {
            binding.switchRefuge.setOnClickListener {
                showAcheterRefugeDialog()
            }
        }
    }

    private fun getTabTitle(type: TypeObjet): String {
        return when (type) {
            TypeObjet.JOUET -> context.getString(R.string.tab_toys)
            TypeObjet.NOURRITURE -> context.getString(R.string.tab_food)
            else -> type.name
        }
    }

    private fun showCompagnonPopUp(view: View) {
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

    fun showAcheterRefugeDialog() {
        val text = TextView(context).apply {
            text = "Les refuges peuvent augmenter le gain d'humeur ou de faim d'un compagnon"
            gravity = Gravity.CENTER
            setPadding(8, 32, 8, 16)
        }

        MaterialAlertDialogBuilder(context)
            .setTitle("Vous n'avez aucun Refuges!")
            .setView(text)
            .setPositiveButton("Acheter des Refuges") { dialog, _ ->
                if (context is MainActivity) {
                    val navController = context.findNavController(R.id.nav_host_fragment_activity_main)
                    navController.navigate(R.id.navigation_notifications)
                }
                dialog.dismiss()
            }
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun showSelectRefugeDialog() {
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_select_refuge, null)

        val horizontalContainer = dialogView.findViewById<LinearLayout>(R.id.horizontalContainer)
        val boutonCancel = dialogView.findViewById<Button>(R.id.boutonCancel)
        val boutonSelect = dialogView.findViewById<Button>(R.id.boutonSelect)

        val refuges = gestionnaireRefuge.getRefuges()
        var selectedRefuge: Refuge? = null

        refuges.forEach { refuge ->
            val imageButton = ImageButton(context).apply {
                layoutParams = ViewGroup.LayoutParams(200, 200)
                scaleType = ImageView.ScaleType.CENTER_CROP
                setPadding(8, 8, 8, 8)


                Glide.with(context)
                    .load(refuge.apparence())
                    .into(this)

                setOnClickListener {
                    selectedRefuge = refuge
                    this.setBackgroundColor(Color.CYAN)
                    horizontalContainer.children.forEach { child ->
                        if (child != this) child.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
            horizontalContainer.addView(imageButton)
        }


        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("")
            .setView(dialogView)
            .create()

        boutonCancel.setOnClickListener {
            dialog.dismiss()
        }

        boutonSelect.setOnClickListener {
            selectedRefuge?.let {
                gestionnaireRefuge.setActif(it.getId())
                Glide.with(context)
                    .load(it.apparence())
                    .into(binding.refuge)
            }
            dialog.dismiss()
        }

        dialog.show()
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

    fun showDialogRelease(){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_relacher, null)

        // Créer le popup avec AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        val dialog = dialogBuilder.create()

        val buttonAnnuler = dialogView.findViewById<Button>(R.id.boutonAnnuler)
        val buttonValider = dialogView.findViewById<Button>(R.id.boutonRelacher)

        buttonValider.setOnClickListener{
            val compagnonsRestants = gestionnaire.obtenirCompagnons()
            gestionnaire.relacherCompagnon(compagnon, compagnon.id)
            if (compagnonsRestants.isNotEmpty()) {
                compagnon = compagnonsRestants.first()
                gestionnaire.setActif(compagnon.id)
            }
            dialog.dismiss()
        }

        buttonAnnuler.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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

        @SuppressLint("StringFormatInvalid")
        fun showPersonnalite(gestionnaireCompagnons: GestionnaireDeCompagnons, view: View, id: Int) {
            val context = view.context
            val compagnon = gestionnaireCompagnons.obtenirCompagnon(id)
            val typePersonnalite = when (compagnon!!.personnalite) {
                Personnalite.GOURMAND -> "Gourmand"
                Personnalite.JOUEUR -> "Joueur"
                Personnalite.CALME -> "Calme"
                Personnalite.AVARE -> "Avare"
                Personnalite.GRINCHEUX -> "Grincheux"
                Personnalite.RADIN -> "Radin"
                Personnalite.GENTIL -> "Gentil"
                Personnalite.JOYEUX -> "Joyeux"
                Personnalite.TRAVAILLEUR -> "Travailleur"
            }

            val personnaliteTextView = view.findViewById<TextView>(R.id.personnalite)
            personnaliteTextView?.text = typePersonnalite +" (?)"// Affiche uniquement le nom
            personnaliteTextView?.setOnClickListener {
                val message = when (compagnon!!.personnalite) {
                    Personnalite.GOURMAND -> context.getString(R.string.personalite_popup_message_gourmand, typePersonnalite)
                    Personnalite.JOUEUR -> context.getString(R.string.personalite_popup_message_joueur, typePersonnalite)
                    Personnalite.CALME -> context.getString(R.string.personalite_popup_message_calme, typePersonnalite)
                    Personnalite.AVARE -> context.getString(R.string.personalite_popup_message_avare, typePersonnalite)
                    Personnalite.GRINCHEUX -> context.getString(R.string.personalite_popup_message_grincheux, typePersonnalite)
                    Personnalite.RADIN -> context.getString(R.string.personalite_popup_message_radin, typePersonnalite)
                    Personnalite.GENTIL -> context.getString(R.string.personalite_popup_message_gentil, typePersonnalite)
                    Personnalite.JOYEUX -> context.getString(R.string.personalite_popup_message_joyeux, typePersonnalite)
                    Personnalite.TRAVAILLEUR -> context.getString(R.string.personalite_popup_message_travailleur, typePersonnalite)
                    else -> context.getString(R.string.personalite_popup_message_inconnue)
                }
                AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.personalite_popup_title, typePersonnalite))
                    .setMessage(message) // Utilise le message défini dans le switch
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        dialog.dismiss() // Ferme le popup
                    }
                    .create()
                    .show()
        }
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
                when {
                    it.humeur > 60 -> {
                        binding.imageBonheur.setImageResource(R.drawable.humeur_0)
                    }
                    it.humeur > 30 -> {
                        binding.imageBonheur.setImageResource(R.drawable.humeur_1)
                    }
                    it.humeur > 0 -> {
                        binding.imageBonheur.setImageResource(R.drawable.humeur_2)
                    }
                    it.humeur == 0 -> {
                        binding.imageBonheur.setImageResource(R.drawable.humeur_3)
                    }
                }
                when {
                    it.faim > 60 -> {
                        binding.imageFaim.setImageResource(R.drawable.faim_0)
                    }
                    it.faim > 30 -> {
                        binding.imageFaim.setImageResource(R.drawable.faim_1)
                    }
                    it.faim > 0 -> {
                        binding.imageFaim.setImageResource(R.drawable.faim_2)
                    }
                    it.faim == 0 -> {
                        binding.imageFaim.setImageResource(R.drawable.faim_3)
                    }
                }
            }

            if(items.isEmpty()) {

                binding.gridViewItems.numColumns = 1

                val placeholderMessage = when (type) {
                    TypeObjet.JOUET -> context.getString(R.string.message_empty_inventory_toys)
                    TypeObjet.NOURRITURE -> context.getString(R.string.message_empty_inventory_food)
                }

                val placeholderItem = PlaceholderData(
                    message = placeholderMessage,
                    buttonText = context.getString(R.string.store_title),
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
