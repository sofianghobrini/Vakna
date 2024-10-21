package com.app.vakna

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.adapters.ListAdapterBoutons
import com.app.vakna.controller.ControllerArchiverTache
import com.app.vakna.databinding.ActivityGererBinding
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.dao.TacheDAO

class GererActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGererBinding
    private lateinit var listAdapter: ListAdapterBoutons
    private var dao = TacheDAO(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityGererBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val root: View = binding.root

        // Setup RecyclerView
        setUpRecyclerView()

        val tachesBouton: ImageButton = root.findViewById(R.id.boutonListeTaches)
        tachesBouton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigateTo", "Taches")
            startActivity(intent)
        }
    }

    private fun setUpRecyclerView() {
        val data = GestionnaireDeTaches.setToListDataArray(GestionnaireDeTaches(this).obtenirTaches())
        val dataTrier = data.filter { it.estArchivee == false }
        listAdapter = ListAdapterBoutons(ArrayList(dataTrier),
            onArchiveClick = {
                nomTache ->
                showArchiveDialog(nomTache)
            },
            onModifierClick = {
                nomTache ->
                val intent = Intent(this, ModifierActivity::class.java).apply {
                    putExtra("NOM_TACHE", nomTache) // Pass the task name
                }
                startActivity(intent)
            })
        // Add dividers and set the adapter
        ajoutDividers(binding.listeTaches)
        binding.listeTaches.adapter = listAdapter
    }

    private fun showArchiveDialog(nomTache: String) {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_archive, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val nomTacheTextView = dialogView.findViewById<TextView>(R.id.dialogTexteWarning)

        // Update the TextView content dynamically with the task name
        nomTacheTextView.text = "Voulez-vous vraiment archiver la tâche \"$nomTache\" ? Vous ne pourrez plus réactiver cette tâche! " +
                "Cependant vous pourrez toujours la revoir dans la page d'archive."

        dialogView.findViewById<Button>(R.id.boutonAnnuler).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.boutonArchiver).setOnClickListener {
            ControllerArchiverTache(binding.root).archiverTache(nomTache)
            setUpRecyclerView()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun ajoutDividers(listeBinding: RecyclerView) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // Add dividers between items in the list
        listeBinding.layoutManager = layoutManager

        listeBinding.addItemDecoration(
            DividerItemDecoration(this, layoutManager.orientation).apply {
                setDrawable(getDrawable(R.drawable.divider_item)!!) // Set your custom divider
            }
        )
    }
}
