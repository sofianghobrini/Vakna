package com.app.vakna.controller

import com.app.vakna.databinding.ActivityGererTacheBinding
import com.app.vakna.modele.GestionnaireDeTaches

/**
 * Contrôleur pour archiver une tâche.
 * @param binding Le binding de l'activité Gerer, qui permet d'accéder aux vues de l'interface utilisateur.
 */
class ControllerArchiverTache(private val binding: ActivityGererTacheBinding) {

    /**
     * Méthode pour archiver une tâche donnée.
     * @param nom Le nom de la tâche à archiver.
     */
    fun archiverTache(nom: String) {
        // Crée une instance du GestionnaireDeTaches et archive la tâche spécifiée
        val gestionnaire = GestionnaireDeTaches(binding.root.context)
        gestionnaire.archiverTache(nom)
    }
}
