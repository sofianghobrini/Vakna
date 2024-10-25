package com.app.vakna.controller

import android.widget.RadioGroup
import android.widget.EditText
import android.widget.Spinner
import com.app.vakna.R
import com.app.vakna.databinding.ActivityModifierProjetBinding
import com.app.vakna.modele.Importance
import com.app.vakna.modele.TypeTache
import com.app.vakna.modele.GestionnaireDeProjets
import com.app.vakna.modele.Projet
import java.time.LocalDate

/**
 * Contrôleur pour la modification des projets dans l'ActivityModifier.
 * @param binding : le binding pour accéder aux éléments de l'interface utilisateur
 * @param nomProjetOriginal : le nom du projet à modifier
 */
class ControllerModifierProjet(
    private val binding: ActivityModifierProjetBinding,
    private val nomProjetOriginal: String
) {

    /**
     * Récupérer le nom du projet à partir de l'interface utilisateur
     * @return String : le nom du projet
     */
    private fun recupererNomProjet(): String {
        val nomProjetEditText = binding.root.findViewById<EditText>(R.id.inputNomProjet)
        return nomProjetEditText.text.toString().trim()  // Suppression des espaces inutiles
    }

    /**
     * Récupérer le type du projet depuis l'interface utilisateur (Spinner)
     * @return TypeTache : l'énumération correspondant au type du projet sélectionné
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
     * Récupérer l'importance du projet en fonction de l'option sélectionnée dans le RadioGroup
     * @return Importance : l'énumération correspondant à l'importance sélectionnée
     */
    private fun recupererImportanceProjet(): Importance {
        val radioGroupImportance = binding.root.findViewById<RadioGroup>(R.id.radioImportanceProjet)
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
    fun modifierProjet() {
        val nomProjet = recupererNomProjet()

        // Vérification si le nom du projet est vide
        if (nomProjet.isBlank()) {
            return
        }

        val TypeTache = recupererTypeTache()
        val importanceProjet = recupererImportanceProjet()
        val derniereValidation = LocalDate.now()

        // Créer une nouvelle instance de Projet avec les valeurs modifiées
        val Projet = Projet(nomProjet, importanceProjet, TypeTache, derniereValidation, false)

        // Initialiser le gestionnaire de tâches et modifier la tâche existante
        val gestionnaireDeProjets = GestionnaireDeProjets(binding.root.context)



        // Appeler la fonction de modification dans le gestionnaire de tâches
        gestionnaireDeProjets.modifierProjet(nomProjetOriginal, Projet)
    }
}
