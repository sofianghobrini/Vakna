package com.app.vakna.controller

import android.app.AlertDialog
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Spinner
import android.widget.Toast
import com.app.vakna.vue.GererActivity
import com.app.vakna.vue.ModifierActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityModifierBinding
import com.app.vakna.modele.dao.Importance
import com.app.vakna.modele.dao.TypeTache
import com.app.vakna.modele.gestionnaires.GestionnaireDeTaches
import com.app.vakna.modele.dao.Frequence
import com.app.vakna.modele.dao.tache.Tache
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
        val taskName = intent.getStringExtra("NOM_TACHE") ?: context.getString(R.string.quete_inconnue)

        val gestionnaire = GestionnaireDeTaches(binding.root.context)

        tacheOriginel = gestionnaire.obtenirTache(taskName)!!

        remplirChamps(tacheOriginel)

        binding.titreModifierTache.text = context.getString(R.string.modifier_titre_tache, taskName)

        binding.boutonModifierTache.setOnClickListener {
            modifierTache()
            if (context is ModifierActivity) {
                val intentNav = Intent(context, GererActivity::class.java)
                intentNav.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                context.startActivity(intentNav)
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
                    afficherPopUpSemaine()
                }

                R.id.radioMensuel -> {
                    afficherPopUpMensuel()
                }
            }
        }
    }

    private fun remplirChamps(task: Tache) {
        binding.contenuInclude.inputNomTache.setText(task.nom)
        val taskTypePosition = task.type.ordinal
        binding.contenuInclude.selectTypeTache.setSelection(taskTypePosition)

        when (task.frequence) {
            Frequence.QUOTIDIENNE -> binding.contenuInclude.radioFrequenceTache.check(R.id.radioQuotidien)
            Frequence.HEBDOMADAIRE -> binding.contenuInclude.radioFrequenceTache.check(R.id.radioHebdomadaire)
            Frequence.MENSUELLE -> binding.contenuInclude.radioFrequenceTache.check(R.id.radioMensuel)
        }

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
        return when(typeSelectionne.uppercase()) {
            context.getString(R.string.type_personnelle) -> TypeTache.PERSONNELLE
            context.getString(R.string.type_professionnelle) -> TypeTache.PROFESSIONNELLE
            context.getString(R.string.type_projet) -> TypeTache.PROJET
            context.getString(R.string.type_etudes) -> TypeTache.ETUDES
            context.getString(R.string.type_sport) -> TypeTache.SPORT
            context.getString(R.string.type_autre) -> TypeTache.AUTRE
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
            Toast.makeText(context, context.getString(R.string.nom_quete_requis), Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, context.getString(R.string.modif_quete_valide), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.modif_quete_erreur), Toast.LENGTH_SHORT).show()
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
