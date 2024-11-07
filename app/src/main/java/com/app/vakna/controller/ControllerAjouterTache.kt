package com.app.vakna.controller

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Toast
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

    init {
        // Définir le texte des boutons en utilisant les ressources de chaînes
        binding.boutonCreerTache.text = context.getString(R.string.create_task_button)
        binding.boutonAnnulerCreation.text = context.getString(R.string.cancel_task_creation_button)

        // Bouton pour confirmer la création de la tâche
        binding.boutonCreerTache.setOnClickListener {
            if (validerFormulaire()) {
                Log.d("AjoutTache", "Validation réussie")
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
            if (checkedId == R.id.radioHebdomadaire) {
                afficherPopUp_semaine()
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
        return enumValueOf<TypeTache>(type)
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
            derniereValidation = LocalDate.now(),
            estTerminee = false
        )

        // Gestionnaire de tâches pour ajouter la nouvelle tâche
        val gestionnaireDeTaches = GestionnaireDeTaches(binding.root.context)
        gestionnaireDeTaches.ajouterTache(tache)
    }

    /**
     * Affiche un popup pour sélectionner les jours de la semaine.
     */
    private fun afficherPopUp_semaine() {
        // Charger le layout personnalisé pour le popup
        val dialogView = LayoutInflater.from(context).inflate(R.layout.popup_jour_semaine, null)

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
        buttonValider.text = context.getString(R.string.button_validate)

        buttonValider.setOnClickListener {
            // Récupérer les jours sélectionnés
            val selectedDays = mutableListOf<String>()
            if (checkLundi.isChecked) selectedDays.add(context.getString(R.string.day_monday))
            if (checkMardi.isChecked) selectedDays.add(context.getString(R.string.day_tuesday))
            if (checkMercredi.isChecked) selectedDays.add(context.getString(R.string.day_wednesday))
            if (checkJeudi.isChecked) selectedDays.add(context.getString(R.string.day_thursday))
            if (checkVendredi.isChecked) selectedDays.add(context.getString(R.string.day_friday))
            if (checkSamedi.isChecked) selectedDays.add(context.getString(R.string.day_saturday))
            if (checkDimanche.isChecked) selectedDays.add(context.getString(R.string.day_sunday))

            // Fermer le dialog après la sélection
            dialog.dismiss()

            // Afficher un Toast avec les jours sélectionnés
            Toast.makeText(
                context,
                context.getString(R.string.popup_selected_days, selectedDays.joinToString()),
                Toast.LENGTH_SHORT
            ).show()
        }

        // Afficher le popup
        dialog.show()
    }
}
