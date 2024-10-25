package com.app.vakna.controller

import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.GererTachesActivity
import com.app.vakna.MainActivity
import com.app.vakna.ModifierTacheActivity
import com.app.vakna.R
import com.app.vakna.adapters.ListAdapterBoutons
import com.app.vakna.adapters.ListData
import com.app.vakna.databinding.ActivityGererTacheBinding
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.dao.TacheDAO

class ControllerGererTache(private val binding: ActivityGererTacheBinding) {

    val context = binding.root.context
    private lateinit var listAdapter: ListAdapterBoutons
    private var dao = TacheDAO(context)

    init {

        // Setup RecyclerView
        setUpRecyclerView()

        // Add dividers and set the adapter
        ajoutDividers(binding.listeTaches)

        val boutonRetour = binding.boutonRetour
        boutonRetour.setOnClickListener {
            if (context is GererTachesActivity) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("navigateTo", "Taches")
                context.startActivity(intent)
            }
        }
    }

    private fun setUpRecyclerView() {
        val data =
            GestionnaireDeTaches.setToListDataArray(GestionnaireDeTaches(context).obtenirTaches())

        val dataTrier = data.filter { !it.estArchivee }
            .sortedWith(compareByDescending<ListData>
            {
                when (it.importance) {
                    "ELEVEE" -> 3
                    "MOYENNE" -> 2
                    "FAIBLE" -> 1
                    else -> 0
                }
            }.thenBy { it.name })

        listAdapter = ListAdapterBoutons(
            ArrayList(dataTrier),
            onArchiveClick = { nomTache ->
                showArchiveDialog(nomTache)
            },
            onModifierClick = { nomTache ->
                if (context is GererTachesActivity) {
                    val intent = Intent(context, ModifierTacheActivity::class.java)
                    intent.putExtra("NOM_TACHE", nomTache) // Pass the task name
                    context.startActivity(intent)
                }
            })
        binding.listeTaches.adapter = listAdapter
    }

    private fun showArchiveDialog(nomTache: String) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_archive, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val nomTacheTextView = dialogView.findViewById<TextView>(R.id.dialogTexteWarning)

        // Update the TextView content dynamically with the task name
        nomTacheTextView.text =
            "Voulez-vous vraiment archiver la tâche \"$nomTache\" ? Vous ne pourrez plus réactiver cette tâche! " +
                    "Cependant vous pourrez toujours la revoir dans la page d'archive."

        dialogView.findViewById<Button>(R.id.boutonAnnuler).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.boutonArchiver).setOnClickListener {
            ControllerArchiverTache(binding).archiverTache(nomTache)
            setUpRecyclerView()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun ajoutDividers(listeBinding: RecyclerView) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        // Add dividers between items in the list
        listeBinding.layoutManager = layoutManager

        listeBinding.addItemDecoration(
            DividerItemDecoration(context, layoutManager.orientation).apply {
                setDrawable(context.getDrawable(R.drawable.divider_item)!!)
            }
        )
    }
}