package com.app.vakna.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.vakna.R

// Adapter qui gère les boutons d'archivage et de modification des tâches
class ListAdapterArchive(
    dataArrayList: ArrayList<ListData>,
    private val onReactiveClick: (String) -> Unit
) : ListAdapter(dataArrayList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TachesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.liste_archives_taches, parent, false)
        return TachesViewHolder(view)
    }

    override fun onBindViewHolder(holder: TachesViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val listData = dataArrayList[position]

        holder.boutonReactiveTache?.setOnClickListener {
            onReactiveClick(listData.name)
        }
    }
}
