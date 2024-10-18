// ListAdapterWithProgress.kt
package com.app.vakna.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.app.vakna.R
import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.dao.TacheDAO

class ListAdapterProgress(
    dataArrayList: ArrayList<ListData>,
    private val progressBar: ProgressBar? = null,
    private val context: Context
) : ListAdapter(dataArrayList) {

    private var completedTasks = 0
    private var gestionnaire = GestionnaireDeTaches(TacheDAO(context))
    init {
        // Initialize the count of completed tasks
        completedTasks = dataArrayList.count { it.estTermine == true }
        updateProgressBar() // Update progress bar based on initial data
    }

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
            }

            switchTermine.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) {
                    showConfirmationDialog(listData.name) { confirmed ->
                        if (confirmed) {
                            listData.estTermine = true
                            gestionnaire.finirTache(listData.name)
                            switchTermine.isEnabled = false
                            completedTasks++
                            updateProgressBar()
                        } else {
                            switchTermine.isChecked = false
                        }
                    }
                }
            }
        }
    }

    private fun updateProgressBar() {
        val progressPercentage = (completedTasks.toDouble() / itemCount) * 100
        progressBar?.progress = progressPercentage.toInt()
    }

    private fun showConfirmationDialog(nomTache: String, onConfirm: (Boolean) -> Unit) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_confirme_termine, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val textView = dialogView.findViewById<TextView>(R.id.dialogTexteWarning)

        textView.text = "Si vous confirmez que vous avez bien terminé la tâche $nomTache?"

        dialogView.findViewById<Button>(R.id.boutonAnnuler).setOnClickListener {
            dialog.dismiss()
            onConfirm(false)
        }

        dialogView.findViewById<Button>(R.id.boutonTerminer).setOnClickListener {
            dialog.dismiss()
            onConfirm(true)
        }

        dialog.show()
    }
}
