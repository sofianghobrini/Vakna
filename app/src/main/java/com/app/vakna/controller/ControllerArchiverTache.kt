package com.app.vakna.controller

import android.os.Build
import androidx.annotation.RequiresApi
import com.app.vakna.databinding.ActivityGererBinding
import com.app.vakna.modele.gestionnaires.GestionnaireDeTaches

/**
 * Contrôleur pour archiver une tâche.
 * @param binding Le binding de l'activité Gerer, qui permet d'accéder aux vues de l'interface utilisateur.
 */
class ControllerArchiverTache(private val binding: ActivityGererBinding) {

    private val context = binding.root.context

    /**
     * Méthode pour archiver une tâche donnée.
     * @param nom Le nom de la tâche à archiver.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun archiverTache(nom: String) {
        val gestionnaire = GestionnaireDeTaches(context)
        gestionnaire.archiverTache(nom)
    }
}
