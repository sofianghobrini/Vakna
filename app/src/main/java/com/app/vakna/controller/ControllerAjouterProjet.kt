package com.app.vakna.controller

import android.content.Intent
import android.util.Log
import android.view.View
import com.app.vakna.AjouterProjetActivity
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityAjouterProjetBinding
import com.app.vakna.modele.*
import java.time.LocalDate

/**
 * Contrôleur pour la gestion de l'ajout de projets.
 * @param binding Le binding de l'activité Ajouter, pour accéder aux vues.
 */
class ControllerAjouterProjet(private val binding: ActivityAjouterProjetBinding) {

    init {
        val context = binding.root.context

        // Bouton pour confirmer la création du projet
        binding.boutonCreerProjet.setOnClickListener {
            if (validerFormulaire()) {
                Log.d("AjoutProjet", "Validation réussie")
                confirmerProjet()

                // Naviguer vers l'écran principal après l'ajout du projet
                if (context is AjouterProjetActivity) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("navigateTo", "Projets")
                    context.startActivity(intent)
                }
            }
        }

        // Bouton pour annuler la création du projet et revenir à l'écran principal
        binding.boutonAnnulerCreation.setOnClickListener {
            if (context is AjouterProjetActivity) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("navigateTo", "Projets")
                context.startActivity(intent)
            }
        }
    }

    /**
     * Méthode privée pour valider le formulaire d'ajout de projet.
     */
    private fun validerFormulaire(): Boolean {
        var valide = true

        // Vérification du nom du projet
        val nomProjetEditText = binding.contenuInclude.inputNomProjet
        if (nomProjetEditText.text.isNullOrEmpty()) {
            nomProjetEditText.error = "Le nom du projet est obligatoire"
            valide = false
        }


        // Vérification de l'importance sélectionnée
        val radioGroupImportance = binding.contenuInclude.radioImportanceProjet
        val errorImportanceTextView = binding.contenuInclude.errorImportance
        if (radioGroupImportance.checkedRadioButtonId == -1) {
            errorImportanceTextView.visibility = View.VISIBLE
            valide = false
        } else {
            errorImportanceTextView.visibility = View.GONE
        }

        Log.d("ValidationFormulaire", "Formulaire valide: $valide")
        return valide
    }

    /**
     * Méthode privée pour récupérer le nom du projet.
     */
    private fun recupererNomProjet(): String {
        return binding.contenuInclude.inputNomProjet.text.toString()
    }

    /**
     * Méthode privée pour récupérer le type du projet.
     */
    private fun recupererTypeProjet(): TypeTache {
        val type = binding.contenuInclude.selectTypeProjet.selectedItem.toString().uppercase()
        return enumValueOf<TypeTache>(type)
    }

    /**
     * Méthode privée pour récupérer l'importance du projet.
     */
    private fun recupererImportanceProjet(): Importance {
        val selectedRadioButtonId = binding.contenuInclude.radioImportanceProjet.checkedRadioButtonId
        return when (selectedRadioButtonId) {
            R.id.radioFaible -> Importance.FAIBLE
            R.id.radioMoyen -> Importance.MOYENNE
            R.id.radioElevee -> Importance.ELEVEE
            else -> Importance.MOYENNE
        }
    }

    /**
     * Méthode pour confirmer la création du projet.
     */
    private fun confirmerProjet() {

        // Création de l'objet Projet avec les informations récupérées
        val Projet = Projet(
            nom = recupererNomProjet(),
            importance = recupererImportanceProjet(),
            type = recupererTypeProjet(),
            derniereValidation = LocalDate.now(),
            estTermine = false
        )

        // Gestionnaire de tâches pour ajouter la nouvelle tâche
        val gestionnaireDeProjets = GestionnaireDeProjets(binding.root.context)
        gestionnaireDeProjets.ajouterProjet(Projet)
    }
}
