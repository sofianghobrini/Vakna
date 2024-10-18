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
import com.app.vakna.adapters.ListData
import com.app.vakna.databinding.ActivityGererBinding
import com.app.vakna.ui.Taches.TachesFragment
import com.app.vakna.ui.ajouter.AjouterFragment

class GererActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGererBinding
    private lateinit var listAdapter: ListAdapterBoutons

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
            finish() 
        }
    }

    private fun setUpRecyclerView() {
        listAdapter = ListAdapterBoutons(ajoutData()) { nomTache ->
            showArchiveDialog(nomTache)
        }

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
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun ajoutData(): ArrayList<ListData> {
        val dataArrayList = ArrayList<ListData>()
        val iconList = intArrayOf(
            R.drawable.type_icon_exemple,
            R.drawable.type_icon_exemple,
            R.drawable.type_icon_exemple
        )
        val nameList = arrayOf(
            "50 minutes de travail sur projet X",
            "Faire 100 pompes",
            "Lire 20 pages d'un livre"
        )
        val typeList = arrayOf("Projet", "Sport", "Literature")
        val importanceList = arrayOf("faible", "moyen", "elevee")
        val terminer = arrayOf(false, true, false)
        for (i in iconList.indices) {
            val listData = ListData(
                nameList[i],
                typeList[i], importanceList[i], iconList[i], terminer[i]
            )
            dataArrayList.add(listData)
        }
        return dataArrayList
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
