package com.app.vakna.controller

import com.app.vakna.databinding.ActivityGererProjetBinding
import com.app.vakna.modele.GestionnaireDeProjets

/**
 * Contrôleur pour archiver une tâche.
 * @param binding Le binding de l'activité Gerer, qui permet d'accéder aux vues de l'interface utilisateur.
 */
class ControllerArchiverProjet(private val binding: ActivityGererProjetBinding) {

    /**
     * Méthode pour archiver un projet donnée.
     * @param nom Le nom du projet à archiver.
     */
    fun archiverProjet(nom: String) {
        // Crée une instance du GestionnaireDeProjets et archive la tâche spécifiée
        val gestionnaire = GestionnaireDeProjets(binding.root.context)
        gestionnaire.archiverProjet(nom)
    }
}
