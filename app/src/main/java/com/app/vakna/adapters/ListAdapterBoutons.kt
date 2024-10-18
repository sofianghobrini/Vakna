// ListAdapterWithProgress.kt
package com.app.vakna.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import com.app.vakna.R

class ListAdapterBoutons(
    dataArrayList: ArrayList<ListData>,
    private val onArchiveClick: (String) -> Unit,
    private val onModifierClick: (String) -> Unit
) : ListAdapter(dataArrayList) {

    private var completedTasks = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TachesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.liste_gerer_taches, parent, false)
        return TachesViewHolder(view)
    }
    override fun onBindViewHolder(holder: TachesViewHolder, position: Int) {
        val listData = dataArrayList[position]
        holder.listTypeIcon.setImageResource(listData.icon)
        holder.listName.text = listData.name
        holder.listType.text = listData.type
        holder.listImportance.text = listData.importance

        holder.boutonArchiverTache?.setOnClickListener {
            onArchiveClick(listData.name)
        }
        holder.boutonModifierTache?.setOnClickListener {
            onModifierClick(listData.name)
        }
    }
}
