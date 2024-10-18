package com.app.vakna.ui.gerer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.R
import com.app.vakna.databinding.FragmentGererBinding
import com.app.vakna.adapters.ListAdapterBoutons
import com.app.vakna.adapters.ListData


class GererFragment : Fragment() {

    private var _binding: FragmentGererBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: ListAdapterBoutons

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGererBinding.inflate(inflater, container, false)
        val root: View = binding.root

        SetUpRecyclerView()

        return root
    }

    private fun SetUpRecyclerView() {

        listAdapter = ListAdapterBoutons(AjoutData()) { nomTache ->
            showArchiveDialog(nomTache)
        }

        AjoutDividers(binding.listeTaches)

        binding.listeTaches.adapter = listAdapter

    }

    private fun showArchiveDialog(nomTache: String) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_archive, null)

        val dialog = AlertDialog.Builder(requireContext())
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

    private fun AjoutData(): ArrayList<ListData> {
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

    private fun AjoutDividers(listeBinding: RecyclerView) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        // Mise en place d'un divider pour les items de la liste
        listeBinding.layoutManager = layoutManager

        listeBinding.addItemDecoration(
            DividerItemDecoration(requireContext(), layoutManager.orientation).apply {
                setDrawable(requireContext().getDrawable(R.drawable.divider_item)!!) // Set your custom divider
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}