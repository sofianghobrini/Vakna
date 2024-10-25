package com.app.vakna.controller

import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.GererProjetsActivity
import com.app.vakna.MainActivity
import com.app.vakna.ModifierProjetActivity
import com.app.vakna.R
import com.app.vakna.adapters.ListAdapterBoutons
import com.app.vakna.adapters.ListData
import com.app.vakna.databinding.ActivityGererProjetBinding
import com.app.vakna.modele.GestionnaireDeProjets
import com.app.vakna.modele.dao.ProjetDAO

class ControllerGererProjet(private val binding: ActivityGererProjetBinding) {

    val context = binding.root.context
    private lateinit var listAdapter: ListAdapterBoutons
    private var dao = ProjetDAO(context)

    init {

        // Setup RecyclerView
        setUpRecyclerView()

        // Add dividers and set the adapter
        ajoutDividers(binding.listeProjets)

        val boutonRetour = binding.boutonRetour
        boutonRetour.setOnClickListener {
            if (context is GererProjetsActivity) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("navigateTo", "Projets")
                context.startActivity(intent)
            }
        }
    }

    private fun setUpRecyclerView() {
        val data =
            GestionnaireDeProjets.setToListDataArray(GestionnaireDeProjets(context).obtenirProjets())

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
            onArchiveClick = { nomProjet ->
                showArchiveDialog(nomProjet)
            },
            onModifierClick = { nomProjet ->
                if (context is GererProjetsActivity) {
                    val intent = Intent(context, ModifierProjetActivity::class.java)
                    intent.putExtra("NOM_PROJET", nomProjet) // Pass the task name
                    context.startActivity(intent)
                }
            })
        binding.listeProjets.adapter = listAdapter
    }

    private fun showArchiveDialog(nomProjet: String) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_archive, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val nomProjetTextView = dialogView.findViewById<TextView>(R.id.dialogTexteWarning)

        // Update the TextView content dynamically with the task name
        nomProjetTextView.text =
            "Voulez-vous vraiment archiver le projet \"$nomProjet\" ? Vous ne pourrez plus réactiver ce projet! " +
                    "Cependant vous pourrez toujours le revoir dans la page d'archive."

        dialogView.findViewById<Button>(R.id.boutonAnnuler).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.boutonArchiver).setOnClickListener {
            ControllerArchiverProjet(binding).archiverProjet(nomProjet)
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