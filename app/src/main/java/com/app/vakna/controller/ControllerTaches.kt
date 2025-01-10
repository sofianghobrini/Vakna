package com.app.vakna.controller

import android.content.Intent
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.vue.AjouterActivity
import com.app.vakna.vue.GererActivity
import com.app.vakna.vue.MainActivity
import com.app.vakna.R
import com.app.vakna.vue.SettingsActivity
import com.app.vakna.adapters.ListAdapterProgress
import com.app.vakna.adapters.ListData
import com.app.vakna.databinding.FragmentTachesBinding
import com.app.vakna.modele.dao.Frequence
import com.app.vakna.modele.gestionnaires.GestionnaireDeTaches

/**
 * Contrôleur pour gérer l'affichage des tâches (quotidiennes, hebdomadaires et mensuelles)
 * dans le fragment des tâches.
 */
class ControllerTaches(private val binding: FragmentTachesBinding) {

    private val context = binding.root.context
    private var gestionnaire = GestionnaireDeTaches(context)

    init {
        gestionnaire.verifierTacheNonAccomplies()
        setupRecyclerView()

        binding.boutonAjouterTache.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, AjouterActivity::class.java)
                context.startActivity(intent)
            }
        }

        binding.boutonGererTache.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, GererActivity::class.java)
                context.startActivity(intent)
            }
        }

        binding.boutonSettings.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, SettingsActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    /**
     * Méthode pour configurer les RecyclerViews pour les tâches quotidiennes, hebdomadaires et mensuelles.
     */
    private fun setupRecyclerView() {
        creerListAdapter(Frequence.QUOTIDIENNE, binding.listeTachesJournalier, binding.progressTachesJournalier)
        creerListAdapter(Frequence.HEBDOMADAIRE, binding.listeTachesHebdomadaire, binding.progressTachesHebdomadaire)
        creerListAdapter(Frequence.MENSUELLE, binding.listeTachesMensuel, binding.progressTachesMensuel)
    }

    /**
     * Créer et configurer un adaptateur pour les RecyclerViews en fonction de la fréquence.
     * Trie les tâches selon leur statut d'achèvement et leur importance.
     *
     * @param frequence : Frequence - La fréquence des tâches (quotidienne, hebdomadaire, mensuelle)
     * @param listeTaches : RecyclerView - Le RecyclerView à remplir
     * @param progressBar : ProgressBar - La barre de progression associée
     */
    private fun creerListAdapter(frequence: Frequence, listeTaches: RecyclerView, progressBar: ProgressBar) {
        val listeDesTaches = GestionnaireDeTaches.setToListDataArray(gestionnaire.obtenirTaches(frequence), context)

        val sortedTasks = listeDesTaches
            .filter { !it.estArchivee }
            .sortedWith(
                compareBy<ListData> { it.estTermine }
                    .thenByDescending {
                        when (it.importance) {
                            "ELEVEE" -> 3
                            "MOYENNE" -> 2
                            "FAIBLE" -> 1
                            else -> 0
                        }
                    }
                    .thenBy { it.name }
            )

        val listAdapter = ListAdapterProgress(ArrayList(sortedTasks), progressBar, context)

        addDividers(listeTaches)

        listeTaches.adapter = listAdapter
    }

    /**
     * Méthode pour ajouter des séparateurs dans les RecyclerViews
     * @param listeTaches : RecyclerView - Le RecyclerView auquel ajouter les séparateurs
     */
    private fun addDividers(listeTaches: RecyclerView){
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listeTaches.layoutManager = layoutManager

        // Ajout des séparateurs entre les éléments de la liste
        listeTaches.addItemDecoration(
            DividerItemDecoration(context, layoutManager.orientation).apply {
                setDrawable(context.getDrawable(R.drawable.divider_item)!!)
            }
        )
    }
}
