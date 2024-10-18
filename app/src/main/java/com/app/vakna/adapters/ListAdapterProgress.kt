// ListAdapterWithProgress.kt
package com.app.vakna.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import com.app.vakna.R
import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.dao.TacheDAO

class ListAdapterProgress(
    dataArrayList: ArrayList<ListData>,
    private val progressBar: ProgressBar? = null,
    context: Context
) : ListAdapter(dataArrayList) {

    private var completedTasks = 0
    private var gestionnaire = GestionnaireDeTaches(TacheDAO(context))

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
        gestionnaire.setCompagnon(Compagnon(0, "Veolia la dragonne", espece = "Dragon"))
        gestionnaire.obtenirTaches()

        holder.listTermine?.let { switchTermine ->
            switchTermine.isChecked = listData.estTermine ?: false
            if (switchTermine.isChecked) {
                gestionnaire.finirTache(listData.name)
                switchTermine.isEnabled = false
                completedTasks++
            }
            updateProgressBar()

            switchTermine.setOnCheckedChangeListener { _, isChecked ->
                listData.estTermine = isChecked
                if (progressBar != null) {
                    if (isChecked) {
                        gestionnaire.finirTache(listData.name)
                        switchTermine.isEnabled = false
                        completedTasks++
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
