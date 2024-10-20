package com.app.vakna.controller

import android.content.Intent
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.AjouterActivity
import com.app.vakna.GererActivity
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.app.vakna.adapters.ListAdapterProgress
import com.app.vakna.databinding.FragmentTachesBinding
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.dao.TacheDAO

class ControllerTaches(private val binding: FragmentTachesBinding) {

    private val context = binding.root.context
    private var gestionnaire = GestionnaireDeTaches(TacheDAO(binding.root.context))

    init {
        SetUpRecyclerView()

        val boutonAjouter = binding.boutonAjouterTache
        boutonAjouter.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, AjouterActivity::class.java)
                context.startActivity(intent)
            }
        }

        val boutonGerer = binding.boutonGererTache
        boutonGerer.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, GererActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    private fun SetUpRecyclerView() {
        CreerListeAdapter(Frequence.QUOTIDIENNE, binding.listeTachesJournalier, binding.progressTachesJournalier)

        CreerListeAdapter(Frequence.HEBDOMADAIRE, binding.listeTachesHebdomadaire, binding.progressTachesHebdomadaire)

        CreerListeAdapter(Frequence.MENSUELLE, binding.listeTachesMensuel, binding.progressTachesMensuel)
    }

    private fun CreerListeAdapter(frequence: Frequence, listeTaches: RecyclerView, progressBar: ProgressBar) {
        val data = GestionnaireDeTaches.setToListDataArray(gestionnaire.obtenirTaches(frequence))
        val listAdapter = ListAdapterProgress(data, progressBar, context)

        AjoutDividers(listeTaches)

        listeTaches.adapter = listAdapter
    }

    private fun AjoutDividers(listeBinding: RecyclerView) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listeBinding.layoutManager = layoutManager

        listeBinding.addItemDecoration(
            DividerItemDecoration(context, layoutManager.orientation).apply {
                setDrawable(context.getDrawable(R.drawable.divider_item)!!)
            }
        )
    }
}