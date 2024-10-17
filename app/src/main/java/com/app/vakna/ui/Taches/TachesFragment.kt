package com.app.vakna.ui.Taches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.R
import com.app.vakna.databinding.FragmentTachesBinding


class TachesFragment : Fragment() {

    private var _binding: FragmentTachesBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapterJ: ListAdapter
    private lateinit var listAdapterH: ListAdapter
    private lateinit var listAdapterM: ListAdapter
    private var completedJournalier = 0
    private var completedHebdomadaire = 0
    private var completedMensuel = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTachesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        SetUpRecyclerView()

        val imageButton: ImageButton = root.findViewById(R.id.boutonAjouterTache)
        imageButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.navigation_ajouter)
        }

        return root
    }

    private fun SetUpRecyclerView() {

        val dataJournalier = AjoutData()
        listAdapterJ = ListAdapter(dataJournalier, binding.progressTachesJournalier)

        AjoutDividers(binding.listeTachesJournalier)

        binding.listeTachesJournalier.adapter = listAdapterJ

        // Separation diff Frequences

        val dataHebdo = AjoutData()
        listAdapterH = ListAdapter(dataHebdo, binding.progressTachesHebdomadaire)

        AjoutDividers(binding.listeTachesHebdomadaire)

        binding.listeTachesHebdomadaire.adapter = listAdapterH

        // Separation diff Frequences

        val dataMensuel = AjoutData()
        listAdapterM = ListAdapter(dataMensuel, binding.progressTachesMensuel)

        AjoutDividers(binding.listeTachesMensuel)

        binding.listeTachesMensuel.adapter = listAdapterM

        completedJournalier = dataJournalier.count { it.estTermine }
        binding.progressTachesJournalier.progress = ((completedJournalier.toDouble() / dataJournalier.count()) * 100).toInt()
        completedHebdomadaire = dataHebdo.count { it.estTermine }
        binding.progressTachesHebdomadaire.progress = ((completedHebdomadaire.toDouble() / dataHebdo.count()) * 100).toInt()
        completedMensuel = dataMensuel.count { it.estTermine }
        binding.progressTachesMensuel.progress = ((completedMensuel.toDouble() / dataMensuel.count()) * 100).toInt()
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
        val importanceList = arrayOf(true, false, false)
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
