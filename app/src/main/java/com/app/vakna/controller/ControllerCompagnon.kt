package com.app.vakna.controller

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.vue.MainActivity
import com.app.vakna.R
import com.app.vakna.vue.SettingsActivity
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.adapters.ViewPagerAdapterInventaire
import com.app.vakna.databinding.FragmentCompagnonBinding
import com.app.vakna.modele.dao.compagnon.Compagnon
import com.app.vakna.modele.gestionnaires.GestionnaireDeCompagnons
import com.app.vakna.modele.gestionnaires.GestionnaireDeRefuges
import com.app.vakna.modele.gestionnaires.Inventaire
import com.app.vakna.modele.dao.objetobtenu.ObjetObtenu
import com.app.vakna.modele.dao.Personnalite
import com.app.vakna.modele.dao.refuge.Refuge
import com.app.vakna.modele.gestionnaires.MagasinObjets
import com.app.vakna.modele.dao.TypeObjet
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Contrôleur pour gérer les interactions avec le compagnon dans l'application.
 * @param binding Le binding associé au fragment compagnon pour accéder aux vues.
 */
class ControllerCompagnon(private val binding: FragmentCompagnonBinding) {

    private val context: Context = binding.root.context
    private var gestionnaireCompagnons = GestionnaireDeCompagnons(context)
    private var inventaire = Inventaire(context)
    private val magasinObjets = MagasinObjets(context)
    private val gestionnaireRefuge = GestionnaireDeRefuges(context)
    private lateinit var compagnon: Compagnon
    private lateinit var compagnonsSupplementaire: Array<Compagnon>

    /**
     * Initialise l'interface utilisateur pour afficher les informations du compagnon.
     * Charge les données depuis la base et configure les éléments graphiques.
     */
    init {
        setUpView()

        binding.editNameButton.setOnClickListener {
            showEditNameDialog()
        }

        binding.boutonSettings.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, SettingsActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    private fun setUpView() {

        val compagnons = gestionnaireCompagnons.obtenirCompagnons()
        var compagnonTemp = gestionnaireCompagnons.obtenirActif()
        if (compagnonTemp == null) {
            compagnonTemp = gestionnaireCompagnons.obtenirCompagnons().first()
            gestionnaireCompagnons.setActif(compagnonTemp.id)
        }
        compagnon = compagnonTemp

        updateAffichageCompagnon()
        updateRefuge(binding)

        creationTabs()

        updateCompagnonsSupplementaires(compagnons)

        setUpRefuges()

        binding.boutonRelacherCompagnon.setOnClickListener{
            val compagnonRestant = gestionnaireCompagnons.obtenirCompagnons()
            if(compagnonRestant.size > 1) { showDialogRelease() }
            else {
                Toast.makeText(context, context.getString(R.string.un_compagnon_restant), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCompagnonsSupplementaires(compagnons: Set<Compagnon>) {
        compagnonsSupplementaire = (compagnons - compagnon).toTypedArray()

        val switchBoutons = listOf(
            binding.switchCompagnon1,
            binding.switchCompagnon2,
            binding.switchCompagnon3,
            binding.switchCompagnon4,
            binding.switchCompagnon5
        )

        switchBoutons.forEach{
            it.visibility = View.GONE
        }

        if (compagnonsSupplementaire.isEmpty()) {
            binding.switchCompagnon1.visibility = View.VISIBLE
            binding.switchCompagnon1.setImageResource(R.drawable.ajout_compagnon)
            binding.switchCompagnon1.setOnClickListener {
                showCompagnonPopUp(it)
            }
        }

        var iteration = 0

        compagnonsSupplementaire.forEach {
            val id = iteration

            //Création du bouton switch
            val bouton = switchBoutons[iteration]
            bouton.visibility = View.VISIBLE
            val appearancePath = it.apparence()
            Glide.with(context)
                .asGif()
                .load(appearancePath)
                .into(bouton)

            bouton.setOnClickListener { view ->
                echangeDeCompagnon(bouton, id)
            }

            if (iteration + 1 <= 4) {
                val boutonNext = switchBoutons[iteration + 1]
                boutonNext.visibility = View.VISIBLE
                boutonNext.setImageResource(R.drawable.ajout_compagnon)
                boutonNext.setOnClickListener { view ->
                    showCompagnonPopUp(view)
                }
            }
            iteration++
        }
    }

    private fun updateAffichageCompagnon() {
        compagnon.let { comp ->
            binding.dragonName.text = comp.nom
            updateLevelAndProgress(comp)
        }

        compagnon.let {
            affichageHumeur(binding, it)
            affichageFaim(binding, it)
        }

        updateHumeurCompagnon(binding, compagnon)
        showPersonnalite(gestionnaireCompagnons, binding.root, compagnon.id)
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
        binding.dragonLevel.text = context.getString(R.string.texte_niveau, level)

        // Mettre à jour la barre de progression avec l'XP restant pour le niveau
        binding.progressBarLevel.progress = xpForCurrentLevel
    }

    private fun updateRefuge(binding: FragmentCompagnonBinding) {
        val refuge = gestionnaireRefuge.obtenirActif()
        val fichierApparence = refuge?.apparence()

        Glide.with(context)
            .load(fichierApparence)
            .into(binding.refuge)
    }

    private fun creationTabs(){
        val listeTypesObjet = magasinObjets.obtenirObjets().map { it.getType() }.distinct()

        listeTypesObjet.forEach {
            val tabTitle = getTabTitle(it)
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(tabTitle))
        }

        val items = inventaire.obtenirObjets(listeTypesObjet.first())
        setupPagerView(items, listeTypesObjet, binding)
    }

    private fun getTabTitle(type: TypeObjet): String {
        return when (type) {
            TypeObjet.JOUET -> context.getString(R.string.tab_jouet)
            TypeObjet.NOURRITURE -> context.getString(R.string.tab_nourriture)
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
            NavigationHandler.navigationFragmentVersFragment(context, R.id.navigation_magasin, "Compagnons")
            popupMagasinWindow.dismiss()
        }

        popupMagasinView.setOnClickListener {
            popupMagasinWindow.dismiss()
        }
    }

    private fun echangeDeCompagnon(bouton: ImageButton, id: Int) {
        val appearancePathNew = compagnon.apparence()

        Glide.with(context)
            .asGif()
            .load(appearancePathNew)
            .into(bouton)

        val newCompagnon = compagnonsSupplementaire[id]
        compagnonsSupplementaire[id] = compagnon
        gestionnaireCompagnons.setActif(newCompagnon.id)
        compagnon = newCompagnon

        updateAffichageCompagnon()

        val listeTypesObjet = magasinObjets.obtenirObjets().map { it.getType() }.distinct()

        val items = inventaire.obtenirObjets(listeTypesObjet.first())
        setupPagerView(items, listOf(TypeObjet.JOUET, TypeObjet.NOURRITURE), binding)
    }

    private fun setUpRefuges() {
        val refuges = gestionnaireRefuge.obtenirRefuges()
        val refuge = gestionnaireRefuge.obtenirActif()

        refuge?.let {
            Glide.with(context)
                .load(refuge.apparence())
                .into(binding.refuge)
        } ?: {
            if (refuges.isNotEmpty()) {
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

            else -> {
                binding.switchRefuge.setOnClickListener {
                    showSelectRefugeDialog()
                }
            }
        }
    }

    private fun showAcheterRefugeDialog() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_refuges)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.findViewById<Button>(R.id.btn_buy).setOnClickListener {
            NavigationHandler.navigationFragmentVersFragment(context, R.id.navigation_magasin, "Refuges")
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showSelectRefugeDialog() {
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_select_refuge, null)

        val horizontalContainer = dialogView.findViewById<LinearLayout>(R.id.horizontalContainer)
        val boutonCancel = dialogView.findViewById<Button>(R.id.boutonCancel)
        val boutonSelect = dialogView.findViewById<Button>(R.id.boutonSelect)

        val refuges = gestionnaireRefuge.obtenirRefuges()
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
        // Charger la mise en page personnalisée pour le dialogue
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_modifier_nom, null)

        // Trouver l'EditText dans la mise en page personnalisée
        val editText = dialogView.findViewById<EditText>(R.id.edit_text_name).apply {
            hint = context.getString(R.string.nouveau_nom_exemple) // Définir un indice pour l'entrée de texte
            inputType = android.text.InputType.TYPE_CLASS_TEXT // Définir le type d'entrée comme texte
            filters = arrayOf(android.text.InputFilter.LengthFilter(16)) // Limiter la longueur à 16 caractères
            compagnon.let {
                setText(it.nom)
            }
        }

        // Trouver les boutons existants dans la mise en page
        val buttonConfirm = dialogView.findViewById<Button>(R.id.btn_confirm)
        val buttonCancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        // Construire et afficher le dialogue
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogView) // Définir la vue personnalisée pour le dialogue
            .create()

        // Configurer le comportement des boutons
        buttonConfirm.setOnClickListener {
            val newName = editText.text.toString()
            if (newName.isNotEmpty()) {
                // Mettre à jour le nom du compagnon dans la base de données
                val compagnonModif = compagnon
                compagnonModif.nom = newName
                gestionnaireCompagnons.modifierCompagnon(compagnon.id, compagnonModif)
                binding.dragonName.text = newName // Mettre à jour l'affichage du nom dans l'interface utilisateur
            }
            dialog.dismiss()
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showDialogRelease(){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_relacher, null)

        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        val dialog = dialogBuilder.create()

        val buttonAnnuler = dialogView.findViewById<Button>(R.id.boutonAnnuler)
        val buttonValider = dialogView.findViewById<Button>(R.id.boutonRelacher)

        buttonValider.setOnClickListener{
            val compagnonsRestants = gestionnaireCompagnons.obtenirCompagnons()
            gestionnaireCompagnons.relacherCompagnon(compagnon, compagnon.id)
            if (compagnonsRestants.isNotEmpty()) {
                compagnon = compagnonsRestants.first()
                gestionnaireCompagnons.setActif(compagnon.id)
            }
            updateAffichageCompagnon()
            val compagnons = gestionnaireCompagnons.obtenirCompagnons()
            updateCompagnonsSupplementaires(compagnons)
            dialog.dismiss()
        }

        buttonAnnuler.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }



    companion object {
        private fun affichageHumeur(binding: FragmentCompagnonBinding, it: Compagnon) {
            val context = binding.root.context
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
            binding.texteHumeur.text = context.getString(R.string.texte_humeur, it.humeur)
        }

        private fun affichageFaim(binding: FragmentCompagnonBinding, it: Compagnon) {
            val context = binding.root.context
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
            binding.texteFaim.text = context.getString(R.string.texte_faim, it.faim)
        }

        fun updateHumeurCompagnon(binding: FragmentCompagnonBinding, compagnon: Compagnon) {
            val context = binding.root.context
            val gestionnaire = GestionnaireDeCompagnons(context)
            var compagnonUpd: Compagnon? = gestionnaire.obtenirActif()
            if (compagnonUpd == null) {
                compagnonUpd = gestionnaire.obtenirCompagnons().first()
                gestionnaire.setActif(compagnonUpd.id)
            }
            val fichierApparence = compagnonUpd.apparence()
            println(fichierApparence)

            Glide.with(context)
                .asGif()
                .load(fichierApparence)
                .into(binding.dragonGif)
        }

        @SuppressLint("StringFormatInvalid")
        fun showPersonnalite(gestionnaireCompagnons: GestionnaireDeCompagnons, view: View, id: Int) {
            val context = view.context
            val compagnon = gestionnaireCompagnons.obtenirCompagnon(id)
            val typePersonnalite = when (compagnon!!.personnalite) {
                Personnalite.GOURMAND -> context.getString(R.string.personnalite_gourmand)
                Personnalite.JOUEUR -> context.getString(R.string.personnalite_joueur)
                Personnalite.CALME -> context.getString(R.string.personnalite_calme)
                Personnalite.AVARE -> context.getString(R.string.personnalite_avare)
                Personnalite.GRINCHEUX -> context.getString(R.string.personnalite_grincheux)
                Personnalite.RADIN -> context.getString(R.string.personnalite_radin)
                Personnalite.GENTIL -> context.getString(R.string.personnalite_gentil)
                Personnalite.JOYEUX -> context.getString(R.string.personnalite_joyeux)
                Personnalite.TRAVAILLEUR -> context.getString(R.string.personnalite_travailleur)
            }

            val personnaliteTextView = view.findViewById<TextView>(R.id.personnalite)
            personnaliteTextView?.text = typePersonnalite +" (?)"// Affiche uniquement le nom
            personnaliteTextView?.setOnClickListener {
                val message = when (compagnon.personnalite) {
                    Personnalite.GOURMAND -> context.getString(R.string.personnalite_popup_message_gourmand, typePersonnalite)
                    Personnalite.JOUEUR -> context.getString(R.string.personnalite_popup_message_joueur, typePersonnalite)
                    Personnalite.CALME -> context.getString(R.string.personnalite_popup_message_calme, typePersonnalite)
                    Personnalite.AVARE -> context.getString(R.string.personnalite_popup_message_avare, typePersonnalite)
                    Personnalite.GRINCHEUX -> context.getString(R.string.personnalite_popup_message_grincheux, typePersonnalite)
                    Personnalite.RADIN -> context.getString(R.string.personnalite_popup_message_radin, typePersonnalite)
                    Personnalite.GENTIL -> context.getString(R.string.personnalite_popup_message_gentil, typePersonnalite)
                    Personnalite.JOYEUX -> context.getString(R.string.personnalite_popup_message_joyeux, typePersonnalite)
                    Personnalite.TRAVAILLEUR -> context.getString(R.string.personnalite_popup_message_travailleur, typePersonnalite)
                    else -> context.getString(R.string.personnalite_popup_message_inconnue)
                }
                AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.personnalite_popup_titre, typePersonnalite))
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }


        /**
         * Configure le GridView pour afficher la liste des items (jouets ou nourriture).
         * @param items La liste d'items à afficher dans le GridView.
         */


        fun setupPagerView(items: List<ObjetObtenu>, type: List<TypeObjet>, binding: FragmentCompagnonBinding) {
            val context = binding.root.context
            val gestionnaire = GestionnaireDeCompagnons(context)
            val inventaire = Inventaire(context)
            var compagnonGrid = gestionnaire.obtenirActif()
            if (compagnonGrid == null) {
                compagnonGrid = gestionnaire.obtenirCompagnons().first()
                gestionnaire.setActif(compagnonGrid.id)
            }

            compagnonGrid.let {
                affichageHumeur(binding, it)
                affichageFaim(binding, it)
            }

            setupViewPagerInventaire(binding, items, type, inventaire)

        }
        fun setupViewPagerInventaire(
            binding: FragmentCompagnonBinding,
            listObjet: List<ObjetObtenu>,
            type:List<TypeObjet>,
            inventaire: Inventaire
        ) {
            val viewPager = binding.viewPagerCompagnon
            val tabLayout = binding.tabLayout

            val pages = setPageInventaire(inventaire)
            val tabTitles = listOf(R.string.tab_jouet, R.string.tab_nourriture)

            viewPager.adapter = ViewPagerAdapterInventaire(binding = binding, viewPager.context, pages, listObjet, type)

            viewPager.getChildAt(0).apply {
                if (this is RecyclerView) {
                    this.setOnTouchListener { _, _ -> false }
                }
            }

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = viewPager.context.getString(tabTitles[position])
            }.attach()
        }

        private fun setPageInventaire(inventaire: Inventaire): List<ArrayList<GridConsommableData>> {
            val nourritureList = inventaire.obtenirObjets(TypeObjet.NOURRITURE)
                .sortedWith(compareBy<ObjetObtenu> { it.getType() }.thenBy { it.getNom() })
            val InventaireNourritureList = Inventaire.setToGridDataArray(nourritureList)

            val jouetList = inventaire.obtenirObjets(TypeObjet.JOUET)
                .sortedWith(compareBy<ObjetObtenu> { it.getType() }.thenBy { it.getNom() })
            val InventaireJouetsList = Inventaire.setToGridDataArray(jouetList)

            return listOf(InventaireJouetsList, InventaireNourritureList)
        }
    }


}

