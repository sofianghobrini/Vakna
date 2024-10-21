package com.app.vakna.controller
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.EditText
import android.widget.Spinner
import com.app.vakna.R
import com.app.vakna.modele.Importance
import com.app.vakna.modele.TypeTache
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.Tache
import com.app.vakna.modele.dao.TacheDAO
import java.time.LocalDate


class ControllerModifierTache(private val view: View, private val nomTacheOriginale: String) {

    // Méthode privée pour récupérer le nom de la tâche
    private fun recupererNomTache(): String {
        val nomTacheEditText = view.findViewById<EditText>(R.id.inputNomTache)
        return nomTacheEditText.text.toString()
    }

    // Méthode privée pour récupérer le type de la tâche
    private fun recupererTypeTache(): TypeTache {
        val type = view.findViewById<Spinner>(R.id.selectTypeTache)
        val typeDeLaTache = type.selectedItem.toString()
        return when (typeDeLaTache) {
            "Sport" -> TypeTache.SPORT
            "Etudes" -> TypeTache.ETUDES
            "Professionnelle" -> TypeTache.PROFESSIONNELLE
            "VieQuo" -> TypeTache.VIEQUO
            "Projet" -> TypeTache.PROJET
            "Personnelle" -> TypeTache.PERSONNELLE
            else -> TypeTache.AUTRE
        }
    }

    // Méthode privée pour récupérer la fréquence de la tâche
    private fun recupererFrequenceTache(): Frequence {
        val radioGroupFrequence = view.findViewById<RadioGroup>(R.id.radioFrequenceTache)
        val selectedRadioButtonId = radioGroupFrequence.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.radioQuotidien -> Frequence.QUOTIDIENNE
            R.id.radioHebdomadaire -> Frequence.HEBDOMADAIRE
            else -> Frequence.MENSUELLE
        }
    }

    // Méthode privée pour récupérer l'importance de la tâche
    private fun recupererImportanceTache(): Importance  {
        val radioGroupFrequence = view.findViewById<RadioGroup>(R.id.radioImportanceTache)
        val selectedRadioButtonId = radioGroupFrequence.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.radioFaible -> Importance.FAIBLE
            R.id.radioMoyen -> Importance.MOYENNE
            R.id.radioElevee -> Importance.ELEVEE
            else -> Importance.MOYENNE
        }
    }

    // Méthode publique pour envoyer les informations de la tâche
    fun modifierTache(){
        val nomTache = recupererNomTache()
        if (nomTache.isBlank()) {
            // Afficher un message d'erreur ou retourner une valeur d'erreur
            return
        }
        val typeTache = recupererTypeTache()
        val importanceTache = recupererImportanceTache()
        val frequenceTache = recupererFrequenceTache()
        val derniereValidation = LocalDate.now()
        val tache = Tache(nomTache, frequenceTache, importanceTache, typeTache, derniereValidation,false)
        val gestionnaireDeTaches = GestionnaireDeTaches(view.context)
        Log.i("test", "Ancien nom : $nomTacheOriginale, Nouveau nom : $nomTache")
        Log.i("test", gestionnaireDeTaches.obtenirTaches().toString())
        gestionnaireDeTaches.modifierTache(nomTacheOriginale, tache)
    }

}


