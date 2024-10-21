package com.app.vakna.adapters

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.app.vakna.R
import com.app.vakna.modele.GestionnaireDeTaches

// Adapter gérant la progression des tâches complétées
class ListAdapterProgress(
    dataArrayList: ArrayList<ListData>,
    private val progressBar: ProgressBar? = null,
    private val context: Context
) : ListAdapter(dataArrayList) {

    private var completedTasks = 0
    private val gestionnaire = GestionnaireDeTaches(context)

    init {
        // Initialiser les tâches complétées au début
        completedTasks = dataArrayList.count { it.estTermine == true }
        updateProgressBar() // Mise à jour de la barre de progression initiale
    }

    override fun onBindViewHolder(holder: TachesViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val listData = dataArrayList[position]

        holder.listTermine?.let { switchTermine ->
            switchTermine.isChecked = listData.estTermine ?: false
            switchTermine.isEnabled = !switchTermine.isChecked

            // Gestion du clic sur le switch de tâche terminée
            switchTermine.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
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

    // Mise à jour de la barre de progression selon le nombre de tâches terminées
    private fun updateProgressBar() {
        val progressPercentage = (completedTasks.toDouble() / itemCount) * 100
        progressBar?.progress = progressPercentage.toInt()
    }

    // Affichage d'un dialogue de confirmation pour marquer une tâche comme terminée
    private fun showConfirmationDialog(nomTache: String, onConfirm: (Boolean) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirme_termine, null)

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
