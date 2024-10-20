package com.app.vakna.controller
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.EditText
import android.widget.Spinner
import com.app.vakna.AjouterActivity
import com.app.vakna.CreerCompagnonActivity
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityAjouterBinding
import com.app.vakna.modele.Importance
import com.app.vakna.modele.TypeTache
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.Tache
import com.app.vakna.modele.dao.TacheDAO
import java.time.LocalDate


class ControllerAjouterTache(private val binding: ActivityAjouterBinding) {

    init {
        val confirmButton: Button = binding.boutonCreerTache
        val annulerButton: Button = binding.boutonAnnulerCreation
        val context = binding.root.context

        confirmButton.setOnClickListener {
            if (validerFormulaire()) {
                Log.e("test", "yup")
                ConfirmTache()

                if (context is AjouterActivity) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("navigateTo", "Taches")
                    context.startActivity(intent)
                }
            }
        }

        annulerButton.setOnClickListener {
            if (context is AjouterActivity) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("navigateTo", "Taches")
                context.startActivity(intent)
            }
        }
    }

    private fun validerFormulaire(): Boolean {
        var valide = true

        val nomTacheEditText = binding.contenuInclude.inputNomTache
        if (nomTacheEditText.text.isNullOrEmpty()) {
            nomTacheEditText.error = "Le nom de la tâche est obligatoire"
            valide = false
        }

        val radioGroupFrequence = binding.contenuInclude.radioFrequenceTache
        val errorFrequenceTextView = binding.contenuInclude.errorFrequence
        if (radioGroupFrequence.checkedRadioButtonId == -1) {
            errorFrequenceTextView.visibility = View.VISIBLE
            valide = false
        } else {
            errorFrequenceTextView.visibility = View.GONE
        }

        val radioGroupImportance = binding.contenuInclude.radioImportanceTache
        val errorImportanceTextView = binding.contenuInclude.errorImportance
        if (radioGroupImportance.checkedRadioButtonId == -1) {
            errorImportanceTextView.visibility = View.VISIBLE
            valide = false
        } else {
            errorImportanceTextView.visibility = View.GONE
        }

        Log.e("testValide", valide.toString())
        return valide
    }

    // Méthode privée pour récupérer le nom de la tâche
    private fun recupererNomTache(): String {
        val nomTacheEditText = binding.contenuInclude.inputNomTache
        return nomTacheEditText.text.toString()
    }

    // Méthode privée pour récupérer le type de la tâche
    private fun recupererTypeTache(): TypeTache {
        val type = binding.contenuInclude.selectTypeTache
        val typeDeLaTache = type.selectedItem.toString()
        return when (typeDeLaTache) {
            "Sport" -> TypeTache.PERSONNELLE
            "Etude" -> TypeTache.PROFESSIONNELLE
            else -> TypeTache.AUTRE
        }
    }

    // Méthode privée pour récupérer la fréquence de la tâche
    private fun recupererFrequenceTache(): Frequence {
        val radioGroupFrequence = binding.contenuInclude.radioFrequenceTache
        val selectedRadioButtonId = radioGroupFrequence.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.radioQuotidien -> Frequence.QUOTIDIENNE
            R.id.radioHebdomadaire -> Frequence.HEBDOMADAIRE
            else -> Frequence.MENSUELLE
        }
    }

    // Méthode privée pour récupérer l'importance de la tâche
    private fun recupererImportanceTache(): Importance  {
        val radioGroupFrequence = binding.contenuInclude.radioImportanceTache
        val selectedRadioButtonId = radioGroupFrequence.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.radioFaible -> Importance.FAIBLE
            R.id.radioMoyen -> Importance.MOYENNE
            R.id.radioElevee -> Importance.ELEVEE
            else -> Importance.MOYENNE
        }
    }

    // Méthode publique pour envoyer les informations de la tâche
    fun ConfirmTache(){
        val dao = TacheDAO(binding.root.context)
        val nomTache = recupererNomTache()
        val typeTache = recupererTypeTache()
        val importanceTache = recupererImportanceTache()
        val frequenceTache = recupererFrequenceTache()
        val derniereValidation = LocalDate.now()
        val tache = Tache(nomTache, frequenceTache, importanceTache, typeTache, derniereValidation,false)
        val gestionnaireDeTaches = GestionnaireDeTaches(dao)
        gestionnaireDeTaches.ajouterTache(tache)
    }
}