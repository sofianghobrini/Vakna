package com.app.vakna.controller

import android.content.Intent
import android.util.Log
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.AjouterActivity
import com.app.vakna.GererActivity
import com.app.vakna.MainActivity
import com.app.vakna.ProjetsActivity
import com.app.vakna.R
import com.app.vakna.adapters.ListAdapterProgress
import com.app.vakna.adapters.ListData
import com.app.vakna.databinding.FragmentTachesBinding
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.ui.Taches.TachesFragment

/**
 * Contrôleur pour gérer l'affichage des tâches (quotidiennes, hebdomadaires et mensuelles)
 * dans le fragment des tâches.
 */
class ControllerTaches(private val binding: FragmentTachesBinding) {

    private val context = binding.root.context
    private var gestionnaire = GestionnaireDeTaches(context)

    // Initialisation du contrôleur et des boutons
    init {
        setupRecyclerView()

        // Bouton pour ajouter une nouvelle tâche
        binding.boutonAjouterTache.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, AjouterActivity::class.java)
                context.startActivity(intent)
            }
        }

        // Bouton pour gérer les tâches (archiver, supprimer, etc.)
        binding.boutonGererTache.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, GererActivity::class.java)
                context.startActivity(intent)
            }
        }
        binding.texteTitreGoProjets.setOnClickListener{
            if (context is MainActivity) {
            val intent = Intent(context, ProjetsActivity::class.java)
            context.startActivity(intent)
        }}
    }

    /**
     * Méthode pour configurer les RecyclerViews pour les tâches quotidiennes, hebdomadaires et mensuelles.
     */
    private fun setupRecyclerView() {
        createListAdapter(Frequence.QUOTIDIENNE, binding.listeTachesJournalier, binding.progressTachesJournalier)
        createListAdapter(Frequence.HEBDOMADAIRE, binding.listeTachesHebdomadaire, binding.progressTachesHebdomadaire)
        createListAdapter(Frequence.MENSUELLE, binding.listeTachesMensuel, binding.progressTachesMensuel)
    }

    /**
     * Créer et configurer un adaptateur pour les RecyclerViews en fonction de la fréquence.
     * Trie les tâches selon leur statut d'achèvement et leur importance.
     *
     * @param frequence : Frequence - La fréquence des tâches (quotidienne, hebdomadaire, mensuelle)
     * @param listeTaches : RecyclerView - Le RecyclerView à remplir
     * @param progressBar : ProgressBar - La barre de progression associée
     */
    private fun createListAdapter(frequence: Frequence, listeTaches: RecyclerView, progressBar: ProgressBar) {
        val taskList = GestionnaireDeTaches.setToListDataArray(gestionnaire.obtenirTaches(frequence))

        // Filtrer les tâches non archivées et les trier par statut et importance
        val sortedTasks = taskList
            .filter { !it.estArchivee }
            .sortedWith(
                compareBy<ListData> { it.estTermine ?: false } // Trier par tâche terminée
                    .thenByDescending {
                        when (it.importance) {
                            "ELEVEE" -> 3
                            "MOYENNE" -> 2
                            "FAIBLE" -> 1
                            else -> 0
                        }
                    }
                    .thenBy { it.name }  // Trier par nom alphabétique
            )

        // Création de l'adaptateur avec les tâches triées et la barre de progression
        val listAdapter = ListAdapterProgress(ArrayList(sortedTasks), progressBar, context)

        // Ajout des séparateurs dans la liste
        addDividers(listeTaches)

        // Affecter l'adaptateur au RecyclerView
        listeTaches.adapter = listAdapter
    }

    /**
     * Méthode pour ajouter des séparateurs dans les RecyclerViews
     * @param listeTaches : RecyclerView - Le RecyclerView auquel ajouter les séparateurs
     */
    private fun addDividers(listeTaches: RecyclerView) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listeTaches.layoutManager = layoutManager

        // Ajout des séparateurs entre les éléments de la liste
        listeTaches.addItemDecoration(
            DividerItemDecoration(context, layoutManager.orientation).apply {
                setDrawable(context.getDrawable(R.drawable.divider_item)!!)  // Séparateur personnalisé
            }
        )
    }
}
