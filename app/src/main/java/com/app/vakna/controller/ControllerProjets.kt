package com.app.vakna.controller

import android.content.Intent
import android.util.Log
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.AjouterProjetActivity
import com.app.vakna.GererProjetsActivity
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.app.vakna.adapters.ListAdapterProgress
import com.app.vakna.adapters.ListData
import com.app.vakna.databinding.FragmentProjetsBinding
import com.app.vakna.modele.GestionnaireDeProjets
import com.app.vakna.ui.Taches.TachesFragment

/**
 * Contrôleur pour gérer l'affichage des tâches (quotidiennes, hebdomadaires et mensuelles)
 * dans le fragment des tâches.
 */

class ControllerProjets(private val binding: FragmentProjetsBinding) {

    private val context = binding.root.context
    private var gestionnaire = GestionnaireDeProjets(context)

    // Initialisation du contrôleur et des boutons
    init {
        setupRecyclerView()

        // Bouton pour ajouter une nouvelle tâche
        binding.boutonAjouterProjet.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, AjouterProjetActivity::class.java)
                context.startActivity(intent)
            }
        }

        // Bouton pour gérer les tâches (archiver, supprimer, etc.)
        binding.boutonGererProjet.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, GererProjetsActivity::class.java)
                context.startActivity(intent)
            }
        }

        binding.texteTitreTaches.setOnClickListener {
            if (context is MainActivity) {
                val fragment = TachesFragment()  // Créez une instance de votre fragment

                // Utilisez le FragmentManager pour remplacer le fragment actuel
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, fragment)  // Remplacez 'fragment_container_view' par l'ID de votre conteneur de fragment
                    .commit()
            }
        }
    }

/**
     * Méthode pour configurer les RecyclerViews pour les tâches quotidiennes, hebdomadaires et mensuelles.
     */

    private fun setupRecyclerView() {
        createListAdapter(binding.listeProjets, binding.progressProjets)
    }

/**
     * Créer et configurer un adaptateur pour les RecyclerViews en fonction de la fréquence.
     * Trie les projets selon leur statut d'achèvement et leur importance.
     *
     * @param listeProjets : RecyclerView - Le RecyclerView à remplir
     * @param progressBar : ProgressBar - La barre de progression associée
     */

    private fun createListAdapter(listeProjets: RecyclerView, progressBar: ProgressBar) {
        val projetList = GestionnaireDeProjets.setToListDataArray(gestionnaire.obtenirProjets())

        // Filtrer les projets non archivées et les trier par statut et importance
        val sortedProjets = projetList
            .filter { !it.estArchivee }
            .sortedWith(
                compareBy<ListData> { it.estTermine ?: false } // Trier par projet terminé
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
        val listAdapter = ListAdapterProgress(ArrayList(sortedProjets), progressBar, context)

        // Ajout des séparateurs dans la liste
        addDividers(listeProjets)

        // Affecter l'adaptateur au RecyclerView
        listeProjets.adapter = listAdapter
    }

/**
     * Méthode pour ajouter des séparateurs dans les RecyclerViews
     * @param listeProjets : RecyclerView - Le RecyclerView auquel ajouter les séparateurs
     */

    private fun addDividers(listeProjets: RecyclerView) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listeProjets.layoutManager = layoutManager

        // Ajout des séparateurs entre les éléments de la liste
        listeProjets.addItemDecoration(
            DividerItemDecoration(context, layoutManager.orientation).apply {
                setDrawable(context.getDrawable(R.drawable.divider_item)!!)  // Séparateur personnalisé
            }
        )
    }
}
