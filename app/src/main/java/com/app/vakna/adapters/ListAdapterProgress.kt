// ListAdapterWithProgress.kt
package com.app.vakna.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import com.app.vakna.R

class ListAdapterProgress(
    dataArrayList: ArrayList<ListData>,
    private val progressBar: ProgressBar? = null
) : ListAdapter(dataArrayList) {

    private var completedTasks = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TachesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.liste_termine_taches, parent, false)
        return TachesViewHolder(view)
    }
    override fun onBindViewHolder(holder: TachesViewHolder, position: Int) {
        val listData = dataArrayList[position]
        holder.listTypeIcon.setImageResource(listData.icon)
        holder.listName.text = listData.name
        holder.listType.text = listData.type
        holder.listImportance.text = listData.importance

        holder.listTermine?.let { switchTermine ->
            switchTermine.isChecked = listData.estTermine ?: false
            if (switchTermine.isChecked) {
                completedTasks++
            }
            switchTermine.setOnCheckedChangeListener { _, isChecked ->
                listData.estTermine = isChecked
                if (progressBar != null) {
                    if (isChecked) {
                        completedTasks++
                    } else {
                        completedTasks--
                    }
                    updateProgressBar()
                }
            }
        }
    }

    private fun updateProgressBar() {
        val progressPercentage = (completedTasks.toDouble() / itemCount) * 100
        progressBar?.progress = progressPercentage.toInt()
    }
}
