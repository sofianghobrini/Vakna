package com.app.vakna.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.vakna.R

// Adapter qui gère les boutons d'archivage et de modification des tâches
class ListAdapterGerer(
    dataArrayList: ArrayList<ListData>,
    private val onArchiveClick: (String) -> Unit,
    private val onModifierClick: (String) -> Unit
) : ListAdapter(dataArrayList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TachesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.liste_gerer_taches, parent, false)
        return TachesViewHolder(view)
    }

    override fun onBindViewHolder(holder: TachesViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val listData = dataArrayList[position]

        // Gestion du clic sur le bouton d'archivage
        holder.boutonArchiverTache?.setOnClickListener {
            onArchiveClick(listData.name)
        }

        // Gestion du clic sur le bouton de modification
        holder.boutonModifierTache?.setOnClickListener {
            onModifierClick(listData.name)
        }
    }
}
