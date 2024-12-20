package com.app.vakna.controller

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.GridLayout
import com.app.vakna.AjouterActivity
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityAjouterBinding
import com.app.vakna.modele.*
import java.time.LocalDate

/**
 * Contrôleur pour la gestion de l'ajout de tâches.
 * @param binding Le binding de l'activité Ajouter, pour accéder aux vues.
 */
class ControllerAjouterTache(private val binding: ActivityAjouterBinding) {

    private val context = binding.root.context
    private var selectedDays: MutableList<Int>? = null

    init {

        // Set button text using string resources
        binding.boutonCreerTache.text = context.getString(R.string.create_task_button)
        binding.boutonAnnulerCreation.text = context.getString(R.string.cancel_task_creation_button)

        // Bouton pour confirmer la création de la tâche
        binding.boutonCreerTache.setOnClickListener {
            if (validerFormulaire()) {
                confirmerTache()

                // Naviguer vers l'écran principal après l'ajout de la tâche
                if (context is AjouterActivity) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("navigateTo", context.getString(R.string.navigate_to_tasks))

                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    context.startActivity(intent)
                    context.finish()
                }
            }
        }

        // Bouton pour annuler la création de la tâche et revenir à l'écran principal
        binding.boutonAnnulerCreation.setOnClickListener {
            if (context is AjouterActivity) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("navigateTo", context.getString(R.string.navigate_to_tasks))

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                context.startActivity(intent)
                context.finish()
            }
        }

        val radioGroup = binding.contenuInclude.radioFrequenceTache
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioQuotidien -> {
                    selectedDays = mutableListOf()
                    binding.contenuInclude.titreJoursTache.visibility = View.GONE
                    binding.contenuInclude.labelJoursTache.visibility = View.GONE
                }

                R.id.radioHebdomadaire -> {
                    afficherPopUp_semaine()
                }

                R.id.radioMensuel -> {
                    afficherPopUp_mensuel()
                }
            }
        }
    }

    /**
     * Méthode privée pour valider le formulaire d'ajout de tâche.
     */
    private fun validerFormulaire(): Boolean {
        var valide = true

        // Vérification du nom de la tâche
        val nomTacheEditText = binding.contenuInclude.inputNomTache
        if (nomTacheEditText.text.isNullOrEmpty()) {
            nomTacheEditText.error = binding.root.context.getString(R.string.task_name_error)
            valide = false
        }

        val gestionnaireDeTaches = GestionnaireDeTaches(binding.root.context)
        gestionnaireDeTaches.obtenirTache(nomTacheEditText.text.toString())?.let {
            nomTacheEditText.error = "Il ne peut pas y avoir deux quêtes avec le même nom"
            valide = false
        }

        // Vérification de la fréquence sélectionnée
        val radioGroupFrequence = binding.contenuInclude.radioFrequenceTache
        val errorFrequenceTextView = binding.contenuInclude.errorFrequence
        if (radioGroupFrequence.checkedRadioButtonId == -1) {
            errorFrequenceTextView.visibility = View.VISIBLE
            errorFrequenceTextView.text = binding.root.context.getString(R.string.frequency_error)
            valide = false
        } else {
            errorFrequenceTextView.visibility = View.GONE
        }

        // Vérification de l'importance sélectionnée
        val radioGroupImportance = binding.contenuInclude.radioImportanceTache
        val errorImportanceTextView = binding.contenuInclude.errorImportance
        if (radioGroupImportance.checkedRadioButtonId == -1) {
            errorImportanceTextView.visibility = View.VISIBLE
            errorImportanceTextView.text = binding.root.context.getString(R.string.importance_error)
            valide = false
        } else {
            errorImportanceTextView.visibility = View.GONE
        }

        Log.d("ValidationFormulaire", "Formulaire valide: $valide")
        return valide
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

    /**
     * Méthode pour confirmer la création de la tâche.
     */
    private fun confirmerTache() {
        // Création de l'objet Tache avec les informations récupérées
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

        // Gestionnaire de tâches pour ajouter la nouvelle tâche
        val gestionnaireDeTaches = GestionnaireDeTaches(binding.root.context)
        gestionnaireDeTaches.ajouterTache(tache)
    }
    private fun afficherPopUp_semaine() {
        // Charger le layout personnalisé pour le popup
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_jour_semaine, null)

        // Créer le popup avec AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(context.getString(R.string.popup_title_select_days))

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
            selectedDays = mutableListOf<Int>()
            selectedDays?.let {
                if (checkLundi.isChecked) it.add(1)
                if (checkMardi.isChecked) it.add(2)
                if (checkMercredi.isChecked) it.add(3)
                if (checkJeudi.isChecked) it.add(4)
                if (checkVendredi.isChecked) it.add(5)
                if (checkSamedi.isChecked) it.add(6)
                if (checkDimanche.isChecked) it.add(7)
            }

            // Fermer le dialog après la sélection
            dialog.dismiss()

            binding.contenuInclude.titreJoursTache.visibility = View.VISIBLE
            binding.contenuInclude.labelJoursTache.visibility = View.VISIBLE
            var jours = ""
            selectedDays?.forEach {
                when(it) {
                    1 -> jours += context.getString(R.string.j_lundi) + ", "
                    2 -> jours += context.getString(R.string.j_Mardi) + ", "
                    3 -> jours += context.getString(R.string.j_Mercredi) + ", "
                    4 -> jours += context.getString(R.string.j_Jeudi) + ", "
                    5 -> jours += context.getString(R.string.j_Vendredi) + ", "
                    6 -> jours += context.getString(R.string.j_Samedi) + ", "
                    7 -> jours += context.getString(R.string.j_Dimanche) + ", "
                }
            }
            jours = jours.subSequence(0, jours.length-2).toString()
            binding.contenuInclude.labelJoursTache.text = jours
        }

        val boutonDefaut = dialogView.findViewById<Button>(R.id.button_defaut)

        boutonDefaut.setOnClickListener {
            selectedDays = null
            dialog.dismiss()
            binding.contenuInclude.titreJoursTache.visibility = View.GONE
            binding.contenuInclude.labelJoursTache.visibility = View.GONE
        }

        // Afficher le popup
        dialog.show()
    }

    private fun afficherPopUp_mensuel() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mensuel_perso, null)
        val gridLayout = dialogView.findViewById<GridLayout>(R.id.grid_layout)

        val dayButtons = (1..31).map { day ->
            Button(context).apply {
                text = day.toString()
                setBackgroundColor(resources.getColor(R.color.grisClair, null))
                val widthInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics).toInt()
                val heightInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics).toInt()
                val marginInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics).toInt()
                layoutParams = GridLayout.LayoutParams().apply {
                    width = widthInPx
                    height = heightInPx
                    setMargins(marginInPx, marginInPx, marginInPx, marginInPx)
                    setPadding(0,0,0,0)
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
            }
        }

        gridLayout.removeAllViews()
        dayButtons.forEach { gridLayout.addView(it) }

        // Créer le popup avec AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        val dialog = dialogBuilder.create()

        // Référencer le DatePicker et le bouton d'ajout
        val buttonConfirmDate = dialogView.findViewById<Button>(R.id.confirmer_date)
        selectedDays = mutableListOf<Int>()

        // Bouton de confirmation pour finaliser la sélection
        buttonConfirmDate.setOnClickListener {
            selectedDays?.sort()
            val selectedDates = selectedDays?.joinToString(", ")
            binding.contenuInclude.titreJoursTache.visibility = View.VISIBLE
            binding.contenuInclude.labelJoursTache.visibility = View.VISIBLE
            binding.contenuInclude.labelJoursTache.text = selectedDates
            dialog.dismiss()
        }

        val boutonDefaut = dialogView.findViewById<Button>(R.id.bouton_defaut)

        boutonDefaut.setOnClickListener {
            selectedDays = null
            dialog.dismiss()
            binding.contenuInclude.titreJoursTache.visibility = View.GONE
            binding.contenuInclude.labelJoursTache.visibility = View.GONE
        }

        // Afficher le popup
        dialog.show()
    }
}
