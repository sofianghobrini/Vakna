package com.app.vakna.controller

import android.util.Log
import android.widget.RadioGroup
import android.widget.EditText
import android.widget.Spinner
import com.app.vakna.R
import com.app.vakna.databinding.ActivityModifierTacheBinding
import com.app.vakna.modele.Importance
import com.app.vakna.modele.TypeTache
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.Tache
import java.time.LocalDate

/**
 * Contrôleur pour la modification des tâches dans l'ActivityModifier.
 * @param binding : le binding pour accéder aux éléments de l'interface utilisateur
 * @param nomTacheOriginale : le nom de la tâche à modifier
 */
class ControllerModifierTache(
    private val binding: ActivityModifierTacheBinding,
    private val nomTacheOriginale: String
) {

    /**
     * Récupérer le nom de la tâche à partir de l'interface utilisateur
     * @return String : le nom de la tâche
     */
    private fun recupererNomTache(): String {
        val nomTacheEditText = binding.root.findViewById<EditText>(R.id.inputNomTache)
        return nomTacheEditText.text.toString().trim()  // Suppression des espaces inutiles
    }

    /**
     * Récupérer le type de la tâche depuis l'interface utilisateur (Spinner)
     * @return TypeTache : l'énumération correspondant au type de la tâche sélectionné
     */
    private fun recupererTypeTache(): TypeTache {
        val typeSpinner = binding.root.findViewById<Spinner>(R.id.selectTypeTache)
        val typeSelectionne = typeSpinner.selectedItem.toString()

        // Correspondance entre la chaîne sélectionnée et l'énumération TypeTache
        return when (typeSelectionne.uppercase()) {
            "SPORT" -> TypeTache.SPORT
            "ETUDES" -> TypeTache.ETUDES
            "PROFESSIONNELLE" -> TypeTache.PROFESSIONNELLE
            "VIEQUO" -> TypeTache.VIEQUO
            "PROJET" -> TypeTache.PROJET
            "PERSONNELLE" -> TypeTache.PERSONNELLE
            else -> TypeTache.AUTRE
        }
    }

    /**
     * Récupérer la fréquence de la tâche en fonction de l'option sélectionnée dans le RadioGroup
     * @return Frequence : l'énumération correspondant à la fréquence sélectionnée
     */
    private fun recupererFrequenceTache(): Frequence {
        val radioGroupFrequence = binding.root.findViewById<RadioGroup>(R.id.radioFrequenceTache)
        val selectedRadioButtonId = radioGroupFrequence.checkedRadioButtonId

        // Associer le bouton radio sélectionné à une valeur d'énumération
        return when (selectedRadioButtonId) {
            R.id.radioQuotidien -> Frequence.QUOTIDIENNE
            R.id.radioHebdomadaire -> Frequence.HEBDOMADAIRE
            else -> Frequence.MENSUELLE
        }
    }

    /**
     * Récupérer l'importance de la tâche en fonction de l'option sélectionnée dans le RadioGroup
     * @return Importance : l'énumération correspondant à l'importance sélectionnée
     */
    private fun recupererImportanceTache(): Importance {
        val radioGroupImportance = binding.root.findViewById<RadioGroup>(R.id.radioImportanceTache)
        val selectedRadioButtonId = radioGroupImportance.checkedRadioButtonId

        // Associer le bouton radio sélectionné à une valeur d'énumération
        return when (selectedRadioButtonId) {
            R.id.radioFaible -> Importance.FAIBLE
            R.id.radioMoyen -> Importance.MOYENNE
            R.id.radioElevee -> Importance.ELEVEE
            else -> Importance.MOYENNE
        }
    }

    /**
     * Méthode publique pour modifier une tâche. Elle récupère les nouvelles valeurs saisies, puis modifie
     * la tâche en conséquence.
     */
    fun modifierTache() {
        val nomTache = recupererNomTache()

        // Vérification si le nom de la tâche est vide
        if (nomTache.isBlank()) {
            return
        }

        val typeTache = recupererTypeTache()
        val importanceTache = recupererImportanceTache()
        val frequenceTache = recupererFrequenceTache()
        val derniereValidation = LocalDate.now()

        // Créer une nouvelle instance de Tache avec les valeurs modifiées
        val tache = Tache(nomTache, frequenceTache, importanceTache, typeTache, derniereValidation, false)

        // Initialiser le gestionnaire de tâches et modifier la tâche existante
        val gestionnaireDeTaches = GestionnaireDeTaches(binding.root.context)



        // Appeler la fonction de modification dans le gestionnaire de tâches
        gestionnaireDeTaches.modifierTache(nomTacheOriginale, tache)
    }
}
