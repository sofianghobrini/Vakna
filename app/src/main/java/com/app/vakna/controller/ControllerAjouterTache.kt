package com.app.vakna.controller

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.GridLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.app.vakna.vue.AjouterActivity
import com.app.vakna.vue.MainActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityAjouterBinding
import com.app.vakna.modele.dao.Frequence
import com.app.vakna.modele.dao.Importance
import com.app.vakna.modele.dao.TypeTache
import com.app.vakna.modele.dao.tache.Tache
import com.app.vakna.modele.gestionnaires.GestionnaireDeTaches
import java.time.LocalDate

/**
 * Contrôleur pour la gestion de l'ajout de tâches.
 * @param binding Le binding de l'activité Ajouter, pour accéder aux vues.
 */
@RequiresApi(Build.VERSION_CODES.O)
class ControllerAjouterTache(private val binding: ActivityAjouterBinding) {

    private val context = binding.root.context
    private var selectedDays: MutableList<Int>? = null

    init {
        binding.boutonCreerTache.text = context.getString(R.string.creation_quete)
        binding.boutonAnnulerCreation.text = context.getString(R.string.annuler_creation_quete)

        binding.boutonCreerTache.setOnClickListener {
            if (validerFormulaire()) {
                confirmerTache()

                naviguerVersMain()
            }
        }

        binding.boutonAnnulerCreation.setOnClickListener {
            naviguerVersMain()
        }

        val radioGroup = binding.contenuInclude.radioFrequenceTache
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioQuotidien -> {
                    selectedDays = mutableListOf()
                    setJoursInvisible()
                }

                R.id.radioHebdomadaire -> {
                    afficherPopUpSemaine()
                }

                R.id.radioMensuel -> {
                    afficherPopUpMensuel()
                }
            }
        }
    }

    /**
     * Méthode privée pour valider le formulaire d'ajout de tâche.
     */
    private fun validerFormulaire(): Boolean {

        if(!verifierNom()) {
            return false
        }

        if(!verifierFrequence()) {
            return false
        }

        if(!verifierImportance()) {
            return false
        }

        return true
    }

    private fun verifierNom(): Boolean {
        var valide = true
        val nomTacheEditText = binding.contenuInclude.inputNomTache
        if (nomTacheEditText.text.isNullOrEmpty()) {
            nomTacheEditText.error = binding.root.context.getString(R.string.erreur_nom_quete)
            valide = false
        }

        val gestionnaireDeTaches = GestionnaireDeTaches(context)
        gestionnaireDeTaches.obtenirTache(nomTacheEditText.text.toString())?.let {
            if(!it.estArchivee) {
                nomTacheEditText.error = "Il ne peut pas y avoir deux quêtes avec le même nom"
                valide = false
            }
        }
        return valide
    }

    private fun verifierFrequence(): Boolean {
        var valide = true
        val radioGroupFrequence = binding.contenuInclude.radioFrequenceTache
        val errorFrequenceTextView = binding.contenuInclude.errorFrequence
        if (radioGroupFrequence.checkedRadioButtonId == -1) {
            errorFrequenceTextView.visibility = View.VISIBLE
            errorFrequenceTextView.text =
                binding.root.context.getString(R.string.erreur_frequence_quete)
            valide = false
        } else {
            errorFrequenceTextView.visibility = View.GONE
        }
        return valide
    }

    private fun verifierImportance(): Boolean {
        var valide = true
        val radioGroupImportance = binding.contenuInclude.radioImportanceTache
        val errorImportanceTextView = binding.contenuInclude.errorImportance
        if (radioGroupImportance.checkedRadioButtonId == -1) {
            errorImportanceTextView.visibility = View.VISIBLE
            errorImportanceTextView.text =
                binding.root.context.getString(R.string.erreur_importance_quete)
            valide = false
        } else {
            errorImportanceTextView.visibility = View.GONE
        }
        return valide
    }

    /**
     * Méthode pour confirmer la création de la tâche.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun confirmerTache() {
        val tache = Tache(
            nom = recupererNomTache(),
            frequence = recupererFrequenceTache(),
            importance = recupererImportanceTache(),
            type = recupererTypeTache(),
            jours = selectedDays,
            derniereValidation = LocalDate.now(),
            prochaineValidation = when(recupererFrequenceTache()) {
                Frequence.QUOTIDIENNE -> {
                    LocalDate.now().plusDays(1).atStartOfDay()
                }
                Frequence.HEBDOMADAIRE -> {
                    LocalDate.now().plusWeeks(1).atStartOfDay()
                }
                Frequence.MENSUELLE -> {
                    LocalDate.now().plusMonths(1).atStartOfDay()
                }
            },
            estTerminee = false
        )

        val gestionnaireDeTaches = GestionnaireDeTaches(context)
        gestionnaireDeTaches.ajouterTache(tache)
    }

    /**
     * Méthode privée pour récupérer le nom de la tâche.
     */
    private fun recupererNomTache(): String {
        return binding.contenuInclude.inputNomTache.text.toString()
    }

    /**
     * Méthode privée pour récupérer le type de la tâche.
     */
    private fun recupererTypeTache(): TypeTache {
        val type = binding.contenuInclude.selectTypeTache.selectedItem.toString().uppercase()
        return when(type) {
            context.getString(R.string.type_personnelle) -> TypeTache.PERSONNELLE
            context.getString(R.string.type_professionnelle) -> TypeTache.PROFESSIONNELLE
            context.getString(R.string.type_projet) -> TypeTache.PROJET
            context.getString(R.string.type_etudes) -> TypeTache.ETUDES
            context.getString(R.string.type_sport) -> TypeTache.SPORT
            context.getString(R.string.type_autre) -> TypeTache.AUTRE
            else -> TypeTache.AUTRE
        }
    }

    /**
     * Méthode privée pour récupérer la fréquence de la tâche.
     */
    private fun recupererFrequenceTache(): Frequence {
        val selectedRadioButtonId = binding.contenuInclude.radioFrequenceTache.checkedRadioButtonId
        return when (selectedRadioButtonId) {
            R.id.radioQuotidien -> Frequence.QUOTIDIENNE
            R.id.radioHebdomadaire -> Frequence.HEBDOMADAIRE
            else -> Frequence.MENSUELLE
        }
    }

    /**
     * Méthode privée pour récupérer l'importance de la tâche.
     */
    private fun recupererImportanceTache(): Importance {
        val selectedRadioButtonId = binding.contenuInclude.radioImportanceTache.checkedRadioButtonId
        return when (selectedRadioButtonId) {
            R.id.radioFaible -> Importance.FAIBLE
            R.id.radioMoyen -> Importance.MOYENNE
            R.id.radioElevee -> Importance.ELEVEE
            else -> Importance.MOYENNE
        }
    }

    private fun naviguerVersMain() {
        if (context is AjouterActivity) {
            val intent = Intent(context, MainActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            context.startActivity(intent)
            context.finish()
        }
    }

    private fun afficherPopUpSemaine() {
        // Charger le layout personnalisé pour le popup
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_jour_semaine, null)

        // Créer le popup avec AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(context.getString(R.string.popup_titre_selectionner_jours))

        val dialog = dialogBuilder.create()

        // Initialiser les CheckBoxes et le bouton "Valider"
        val checkLundi = dialogView.findViewById<CheckBox>(R.id.checkbox_lundi)
        val checkMardi = dialogView.findViewById<CheckBox>(R.id.checkbox_mardi)
        val checkMercredi = dialogView.findViewById<CheckBox>(R.id.checkbox_mercredi)
        val checkJeudi = dialogView.findViewById<CheckBox>(R.id.checkbox_jeudi)
        val checkVendredi = dialogView.findViewById<CheckBox>(R.id.checkbox_vendredi)
        val checkSamedi = dialogView.findViewById<CheckBox>(R.id.checkbox_samedi)
        val checkDimanche = dialogView.findViewById<CheckBox>(R.id.checkbox_dimanche)
        val buttonValider = dialogView.findViewById<Button>(R.id.button_valider)

        buttonValider.setOnClickListener {
            selectedDays = mutableListOf()
            selectedDays?.let {
                if (checkLundi.isChecked) it.add(1)
                if (checkMardi.isChecked) it.add(2)
                if (checkMercredi.isChecked) it.add(3)
                if (checkJeudi.isChecked) it.add(4)
                if (checkVendredi.isChecked) it.add(5)
                if (checkSamedi.isChecked) it.add(6)
                if (checkDimanche.isChecked) it.add(7)
            }

            afficherJoursSemaine()

            dialog.dismiss()
        }

        val boutonDefaut = dialogView.findViewById<Button>(R.id.button_defaut)

        boutonDefaut.setOnClickListener {
            setJoursInvisible()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun afficherPopUpMensuel() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mensuel_perso, null)
        val gridLayout = dialogView.findViewById<GridLayout>(R.id.grid_layout)

        val dayButtons = (1..31).map { day -> creerListeJours(day) }

        gridLayout.removeAllViews()
        dayButtons.forEach { gridLayout.addView(it) }

        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        val dialog = dialogBuilder.create()

        val buttonConfirmDate = dialogView.findViewById<Button>(R.id.confirmer_date)
        selectedDays = mutableListOf()

        buttonConfirmDate.setOnClickListener {
            afficherJoursMois()
            dialog.dismiss()
        }

        val boutonDefaut = dialogView.findViewById<Button>(R.id.bouton_defaut)

        boutonDefaut.setOnClickListener {
            setJoursInvisible()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun creerListeJours(day: Int): Button {
        return Button(context).apply {
            text = day.toString()
            setBackgroundColor(resources.getColor(R.color.grisClair, null))
            val widthInPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                50f,
                resources.displayMetrics
            ).toInt()
            val heightInPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                30f,
                resources.displayMetrics
            ).toInt()
            val marginInPx =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics)
                    .toInt()
            layoutParams = GridLayout.LayoutParams().apply {
                width = widthInPx
                height = heightInPx
                setMargins(marginInPx, marginInPx, marginInPx, marginInPx)
                setPadding(0, 0, 0, 0)
            }
            textSize = 10f
            setOnClickListener {
                if (selectedDays!!.contains(day)) {
                    selectedDays?.remove(day)
                    setBackgroundColor(resources.getColor(R.color.grisClair, null))
                } else {
                    selectedDays?.add(day)
                    setBackgroundColor(resources.getColor(R.color.tacheTermine, null))
                }
            }
            var typedValue = TypedValue()
            context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, typedValue, true)
            setTextColor(typedValue.data)
            typedValue = TypedValue()
            context.theme.resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValue, true)
            setBackgroundColor(typedValue.data)
        }
    }

    private fun afficherJoursSemaine() {
        if (!selectedDays?.isEmpty()!!) {
            setJoursVisible()
            var jours = ""
            selectedDays?.forEach {
                when (it) {
                    1 -> jours += context.getString(R.string.lundi) + ", "
                    2 -> jours += context.getString(R.string.mardi) + ", "
                    3 -> jours += context.getString(R.string.mercredi) + ", "
                    4 -> jours += context.getString(R.string.jeudi) + ", "
                    5 -> jours += context.getString(R.string.vendredi) + ", "
                    6 -> jours += context.getString(R.string.samedi) + ", "
                    7 -> jours += context.getString(R.string.dimanche) + ", "
                }
            }
            jours = jours.subSequence(0, jours.length - 2).toString()
            binding.contenuInclude.labelJoursTache.text = jours
        } else {
            setJoursInvisible()
        }
    }

    private fun afficherJoursMois() {
        selectedDays?.sort()
        if (!selectedDays?.isEmpty()!!) {
            val selectedDates = selectedDays?.joinToString(", ")
            setJoursVisible()
            binding.contenuInclude.labelJoursTache.text = selectedDates
        } else {
            setJoursInvisible()
        }
    }

    private fun setJoursVisible() {
        binding.contenuInclude.titreJoursTache.visibility = View.VISIBLE
        binding.contenuInclude.labelJoursTache.visibility = View.VISIBLE
    }

    private fun setJoursInvisible() {
        selectedDays = null
        binding.contenuInclude.titreJoursTache.visibility = View.GONE
        binding.contenuInclude.labelJoursTache.visibility = View.GONE
    }
}