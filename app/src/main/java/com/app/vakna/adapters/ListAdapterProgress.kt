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
import com.app.vakna.modele.GestionnaireDeTaches

// Adapter pour la liste de tâches terminées
class ListAdapterProgress(
    dataArrayList: ArrayList<ListData>,
    private val progressBar: ProgressBar? = null,
    private val context: Context
) : ListAdapter(dataArrayList) {

    private var completedTasks = 0
    private var gestionnaire = GestionnaireDeTaches(context)
    init {
        completedTasks = dataArrayList.count { it.estTermine == true }
        updateProgressBar()
    }

    // Crée une vue pour chaque élément de la liste
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
        gestionnaire.setCompagnon(1)
        gestionnaire.obtenirTaches()

        // Si la tâche est terminée, on désactive le switch
        holder.listTermine?.let { switchTermine ->
            switchTermine.isChecked = listData.estTermine ?: false
            if (switchTermine.isChecked) {
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

    // Met à jour la barre de progression
    private fun updateProgressBar() {
        val progressPercentage = (completedTasks.toDouble() / itemCount) * 100
        progressBar?.progress = progressPercentage.toInt()
    }

    // Affiche une boîte de dialogue de confirmation
    private fun showConfirmationDialog(nomTache: String, onConfirm: (Boolean) -> Unit) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_confirme_termine, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val textView = dialogView.findViewById<TextView>(R.id.dialogTexteWarning)

        textView.text = "Vous avez bien terminé la tâche $nomTache?"

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
