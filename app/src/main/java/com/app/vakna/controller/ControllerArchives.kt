package com.app.vakna.controller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.R
import com.app.vakna.adapters.ListAdapter
import com.app.vakna.adapters.ListAdapterArchive
import com.app.vakna.adapters.ListData
import com.app.vakna.databinding.ActivityArchivesBinding
import com.app.vakna.modele.gestionnaires.GestionnaireDeTaches
import com.app.vakna.vue.ArchivesActivity
import com.app.vakna.vue.MainActivity

class ControllerArchives(private val binding: ActivityArchivesBinding) {

    val context: Context = binding.root.context
    private lateinit var listAdapter: ListAdapter

    init {
        setUpRecyclerView()

        ajoutDividers(binding.listeTaches)

        val boutonRetour = binding.boutonRetour
        boutonRetour.setOnClickListener {
            if (context is ArchivesActivity) {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                context.finish()
            }
        }
    }

    private fun setUpRecyclerView() {
        val data = GestionnaireDeTaches.setToListDataArray(
            GestionnaireDeTaches(context).obtenirTaches(),
            context
        )

        val dataTrier = data.filter { it.estArchivee }
            .sortedWith(compareByDescending<ListData>
            {
                when (it.importance) {
                    "ELEVEE" -> 3
                    "MOYENNE" -> 2
                    "FAIBLE" -> 1
                    else -> 0
                }
            }.thenBy { it.name })

        listAdapter = ListAdapterArchive(
            ArrayList(dataTrier),
            onReactiveClick = { nomTache ->
                showReactiveDialog(nomTache)
            })

        binding.listeTaches.adapter = listAdapter
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun ajoutDividers(listeBinding: RecyclerView) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listeBinding.layoutManager = layoutManager

        listeBinding.addItemDecoration(
            DividerItemDecoration(context, layoutManager.orientation).apply {
                setDrawable(context.getDrawable(R.drawable.divider_item)!!)
            }
        )
    }

    private fun showReactiveDialog(nomTache: String) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_dearchive, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val nomTacheTextView = dialogView.findViewById<TextView>(R.id.dialogTexteWarning)
        nomTacheTextView.text = context.getString(R.string.dialog_texte_reactive, nomTache)

        dialogView.findViewById<Button>(R.id.boutonAnnuler).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.boutonReactiver).setOnClickListener {
            val gestionnaire = GestionnaireDeTaches(context)
            gestionnaire.reactiverTache(nomTache)
            setUpRecyclerView()
            dialog.dismiss()
        }

        dialog.show()
    }
}