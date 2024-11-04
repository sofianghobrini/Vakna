// ListAdapterWithProgress.kt
package com.app.vakna.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.app.vakna.R
import com.app.vakna.modele.GestionnaireDeCompagnons
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.dao.CompagnonDAO

// Adapter pour la liste de tâches terminées
class ListAdapterProgress(
    dataArrayList: ArrayList<ListData>,
    private val progressBar: ProgressBar? = null,
    private val context: Context
) : ListAdapter(dataArrayList) {

    private var completedTasks = 0
    private var gestionnaire = GestionnaireDeTaches(context)
    private var compagnons = GestionnaireDeCompagnons(CompagnonDAO(context))
    init {
        completedTasks = dataArrayList.count { it.estTermine }
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
        gestionnaire.setCompagnon(compagnons.obtenirCompagnons().first().id)
        gestionnaire.obtenirTaches()

        // Si la tâche est terminée, on désactive le switch
        holder.listTermine?.let { switchTermine ->
            switchTermine.isChecked = listData.estTermine
            if (switchTermine.isChecked) {
                switchTermine.isEnabled = false
                updateBackground(holder.cardView)
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
                            updateBackground(holder.cardView)
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

    // Change la couleur du fond du cardView de l'item
    private fun updateBackground(cardView: CardView?) {
        cardView?.setCardBackgroundColor(ContextCompat.getColor(context, R.color.tacheTermine))
    }

    // Affiche une boîte de dialogue de confirmation
    @SuppressLint("SetTextI18n")
    private fun showConfirmationDialog(nomTache: String, onConfirm: (Boolean) -> Unit) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_confirme_termine, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val textView = dialogView.findViewById<TextView>(R.id.dialogTexteWarning)

        var confirme = false

        textView.text = "Vous avez bien terminé la tâche $nomTache?"

        dialogView.findViewById<Button>(R.id.boutonAnnuler).setOnClickListener {
            dialog.dismiss()
            onConfirm(false)
        }

        dialogView.findViewById<Button>(R.id.boutonTerminer).setOnClickListener {
            confirme = true
            dialog.dismiss()
            onConfirm(true)
        }

        dialog.setOnDismissListener {
            if(!confirme) {
                onConfirm(false)
            }
        }

        dialog.show()
    }
}
