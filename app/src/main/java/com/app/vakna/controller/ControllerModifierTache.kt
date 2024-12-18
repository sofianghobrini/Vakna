package com.app.vakna.controller

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
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
    private var selectedDays: MutableList<Int>? = null

    init{
        val taskName = intent.getStringExtra("NOM_TACHE") ?: context.getString(R.string.task_unknown)

        val gestionnaire = GestionnaireDeTaches(binding.root.context)


        // Récupérer la tâche à partir du gestionnaire
        tacheOriginel = gestionnaire.obtenirTache(taskName)!!

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
        val gestionnaireDeTaches = GestionnaireDeTaches(context)

        val nomTache = recupererNomTache()

        if (nomTache.isBlank()) {
            Toast.makeText(context, context.getString(R.string.task_name_required), Toast.LENGTH_SHORT).show()
            return
        }

        gestionnaireDeTaches.obtenirTache(nomTache)?.let {
            Toast.makeText(context, "Il ne peut pas y avoir deux quêtes avec le même nom", Toast.LENGTH_SHORT).show()
            return
        }

        val typeTache = recupererTypeTache()
        val importanceTache = recupererImportanceTache()
        val frequenceTache = recupererFrequenceTache()
        val joursTache = selectedDays
        val derniereValidation = LocalDate.now()
        val prochaineValidation = when(recupererFrequenceTache()) {
            Frequence.QUOTIDIENNE -> {
                LocalDate.now().plusDays(1).atStartOfDay()
            }
            Frequence.HEBDOMADAIRE -> {
                LocalDate.now().plusWeeks(1).atStartOfDay()
            }
            Frequence.MENSUELLE -> {
                LocalDate.now().plusMonths(1).atStartOfDay()
            }
        }
        val estTermine = tacheOriginel.estTerminee

        val tache = Tache(nomTache, frequenceTache, importanceTache, typeTache, joursTache, derniereValidation, prochaineValidation, estTermine)

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
                if (checkLundi.isChecked) it.add(0)
                if (checkMardi.isChecked) it.add(1)
                if (checkMercredi.isChecked) it.add(2)
                if (checkJeudi.isChecked) it.add(3)
                if (checkVendredi.isChecked) it.add(4)
                if (checkSamedi.isChecked) it.add(5)
                if (checkDimanche.isChecked) it.add(6)
            }

            // Fermer le dialog après la sélection
            dialog.dismiss()

            binding.contenuInclude.titreJoursTache.visibility = View.VISIBLE
            binding.contenuInclude.labelJoursTache.visibility = View.VISIBLE
            var jours = ""
            selectedDays?.forEach {
                when(it) {
                    0 -> jours += "Lundi, "
                    1 -> jours += "Mardi, "
                    2 -> jours += "Mercredi, "
                    3 -> jours += "Jeudi, "
                    4 -> jours += "Vendredi, "
                    5 -> jours += "Samedi, "
                    6 -> jours += "Dimanche, "
                }
            }
            jours = jours.subSequence(0, jours.length-2).toString()
            binding.contenuInclude.labelJoursTache.text = jours
        }

        // Afficher le popup
        dialog.show()
    }

    private fun afficherPopUp_mensuel() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mensuel_perso, null)

        // Créer le popup avec AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle(context.getString(R.string.popup_title_choose_dates))

        val dialog = dialogBuilder.create()

        // Référencer le DatePicker et le bouton d'ajout
        val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
        val buttonAjouterDate = dialogView.findViewById<Button>(R.id.button_date)
        val buttonConfirmDate = dialogView.findViewById<Button>(R.id.confirmer_date)
        selectedDays = mutableListOf<Int>()

        // Gestion de l'ajout de la date sélectionnée
        buttonAjouterDate.setOnClickListener {
            val day = datePicker.dayOfMonth
            if (!selectedDays!!.contains(day)) {
                selectedDays!!.add(day)
            } else {
                Toast.makeText(context, "Le jour "+ day +" à déjà était sélectionné" , Toast.LENGTH_SHORT).show()
            }
        }

        // Bouton de confirmation pour finaliser la sélection
        buttonConfirmDate.setOnClickListener {
            binding.contenuInclude.titreJoursTache.visibility = View.VISIBLE
            binding.contenuInclude.labelJoursTache.visibility = View.VISIBLE
            var jours = ""
            selectedDays?.forEach {
                jours += "$it, "
            }
            jours = jours.subSequence(0, jours.length-2).toString()
            binding.contenuInclude.labelJoursTache.text = jours
            dialog.dismiss()
        }

        // Afficher le popup
        dialog.show()
    }
}
