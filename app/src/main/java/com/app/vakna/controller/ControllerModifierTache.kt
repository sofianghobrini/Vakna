package com.app.vakna.controller

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.app.vakna.GererActivity
import com.app.vakna.ModifierActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityModifierBinding
import com.app.vakna.modele.Importance
import com.app.vakna.modele.TypeTache
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.Tache
import java.time.LocalDate

/**
 * Contrôleur pour la modification des tâches dans l'ActivityModifier.
 * @param binding : le binding pour accéder aux éléments de l'interface utilisateur
 * @param intent : l'intention qui contient le nom de la tâche à modifier
 */
class ControllerModifierTache(
    private val binding: ActivityModifierBinding,
    intent: Intent
) {

    private val context = binding.root.context
    private var tacheOriginel: Tache

    init{
        val taskName = intent.getStringExtra("NOM_TACHE") ?: context.getString(R.string.task_unknown)

        val gestionnaire = GestionnaireDeTaches(binding.root.context)


        // Récupérer la tâche à partir du gestionnaire
        tacheOriginel = gestionnaire.obtenirTaches(taskName).first()

        preFillFields(tacheOriginel)

        binding.titreModifierTache.text = context.getString(R.string.modifier_task_title, taskName)

        binding.boutonModifierTache.text = context.getString(R.string.edit_task_button)
        binding.boutonAnnulerCreation.text = context.getString(R.string.cancel)

        binding.boutonModifierTache.setOnClickListener {
            modifierTache()
            if (context is ModifierActivity) {
                val intent = Intent(context, GererActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                context.startActivity(intent)
                context.finish()
            }
        }

        binding.boutonAnnulerCreation.setOnClickListener {
            if (context is ModifierActivity) {
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

    private fun preFillFields(task: Tache) {
        binding.contenuInclude.inputNomTache.setText(task.nom)
        val taskTypePosition = task.type.ordinal
        binding.contenuInclude.selectTypeTache.setSelection(taskTypePosition)

        when (task.frequence) {
            Frequence.QUOTIDIENNE -> binding.contenuInclude.radioFrequenceTache.check(R.id.radioQuotidien)
            Frequence.HEBDOMADAIRE -> binding.contenuInclude.radioFrequenceTache.check(R.id.radioHebdomadaire)
            Frequence.MENSUELLE -> binding.contenuInclude.radioFrequenceTache.check(R.id.radioMensuel)
        }

        // Set the importance RadioButton
        when (task.importance) {
            Importance.FAIBLE -> binding.contenuInclude.radioImportanceTache.check(R.id.radioFaible)
            Importance.MOYENNE -> binding.contenuInclude.radioImportanceTache.check(R.id.radioMoyen)
            Importance.ELEVEE -> binding.contenuInclude.radioImportanceTache.check(R.id.radioElevee)
        }
    }

    private fun recupererNomTache(): String {
        val nomTacheEditText = binding.root.findViewById<EditText>(R.id.inputNomTache)
        return nomTacheEditText.text.toString().trim()
    }

    private fun recupererTypeTache(): TypeTache {
        val typeSpinner = binding.root.findViewById<Spinner>(R.id.selectTypeTache)
        val typeSelectionne = typeSpinner.selectedItem.toString()

        return when (typeSelectionne.uppercase()) {
            context.getString(R.string.type_personnelle).uppercase() -> TypeTache.PERSONNELLE
            context.getString(R.string.type_professionnelle).uppercase() -> TypeTache.PROFESSIONNELLE
            context.getString(R.string.type_projet).uppercase() -> TypeTache.PROJET
            context.getString(R.string.type_etudes).uppercase() -> TypeTache.ETUDES
            context.getString(R.string.type_sport).uppercase() -> TypeTache.SPORT
            context.getString(R.string.type_viequo).uppercase() -> TypeTache.VIEQUO
            else -> TypeTache.AUTRE
        }
    }

    private fun recupererFrequenceTache(): Frequence {
        val radioGroupFrequence = binding.root.findViewById<RadioGroup>(R.id.radioFrequenceTache)
        val selectedRadioButtonId = radioGroupFrequence.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.radioQuotidien -> Frequence.QUOTIDIENNE
            R.id.radioHebdomadaire -> Frequence.HEBDOMADAIRE
            else -> Frequence.MENSUELLE
        }
    }

    private fun recupererImportanceTache(): Importance {
        val radioGroupImportance = binding.root.findViewById<RadioGroup>(R.id.radioImportanceTache)
        val selectedRadioButtonId = radioGroupImportance.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.radioFaible -> Importance.FAIBLE
            R.id.radioMoyen -> Importance.MOYENNE
            R.id.radioElevee -> Importance.ELEVEE
            else -> Importance.MOYENNE
        }
    }

    fun modifierTache() {
        val nomTache = recupererNomTache()

        if (nomTache.isBlank()) {
            Toast.makeText(context, context.getString(R.string.task_name_required), Toast.LENGTH_SHORT).show()
            return
        }

        val typeTache = recupererTypeTache()
        val importanceTache = recupererImportanceTache()
        val frequenceTache = recupererFrequenceTache()
        val derniereValidation = LocalDate.now()
        val estTermine = tacheOriginel.estTerminee

        val tache = Tache(nomTache, frequenceTache, importanceTache, typeTache, derniereValidation, estTermine)
        val gestionnaireDeTaches = GestionnaireDeTaches(context)

        val resultat = gestionnaireDeTaches.modifierTache(tacheOriginel.nom, tache)

        if (resultat) {
            Toast.makeText(context, context.getString(R.string.task_edit_success), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.task_edit_failed), Toast.LENGTH_SHORT).show()
        }
    }
    private fun afficherPopUp_semaine() {
        // Charger le layout personnalisé pour le popup
        val dialogView = LayoutInflater.from(context).inflate(R.layout.popup_jour_semaine, null)

        // Créer le popup avec AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Choisissez les jours")

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
            // Récupérer les jours sélectionnés
            val selectedDays = mutableListOf<String>()
            if (checkLundi.isChecked) selectedDays.add("Lundi")
            if (checkMardi.isChecked) selectedDays.add("Mardi")
            if (checkMercredi.isChecked) selectedDays.add("Mercredi")
            if (checkJeudi.isChecked) selectedDays.add("Jeudi")
            if (checkVendredi.isChecked) selectedDays.add("Vendredi")
            if (checkSamedi.isChecked) selectedDays.add("Samedi")
            if (checkDimanche.isChecked) selectedDays.add("Dimanche")

            // Fermer le dialog après la sélection
            dialog.dismiss()

            // Afficher un Toast avec les jours sélectionnés
            Toast.makeText(context, "Jours sélectionnés : ${selectedDays.joinToString()}", Toast.LENGTH_SHORT).show()
        }

        // Afficher le popup
        dialog.show()
    }
}
