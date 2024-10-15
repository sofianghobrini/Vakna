package com.app.vakna.ui.Taches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.vakna.R
import com.app.vakna.databinding.FragmentTachesBinding

class TachesFragment : Fragment() {

    private var _binding: FragmentTachesBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapterJ: ListAdapter
    private lateinit var listAdapterH: ListAdapter
    private lateinit var listDataJ: ListData
    private lateinit var listDataH: ListData
    var dataArrayListJ = ArrayList<ListData?>()
    var dataArrayListH = ArrayList<ListData?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(TachesViewModel::class.java)

        _binding = FragmentTachesBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val iconListJ = intArrayOf(
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.icon
        )
        val nameListJ = arrayOf(
            "50 minutes de travail sur projet X",
            "Faire 100 pompes",
            "Lire 20 pages d'un livre")
        val typeListJ = arrayOf("Projet", "Sport", "Literature")
        val importanceListJ = arrayOf(true, false, false)
        for (i in iconListJ.indices) {
            listDataJ = ListData(
                nameListJ[i],
                typeListJ[i], importanceListJ[i], iconListJ[i]
            )
            dataArrayListJ.add(listDataJ)
        }
        listAdapterJ = ListAdapter(this, dataArrayListJ)
        binding.listeTachesJournalier.adapter = listAdapterJ

        // Separation diff Frequences

        val iconListH = intArrayOf(
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.icon
            R.drawable.type_icon_exemple,
            R.drawable.type_icon_exemple,
            R.drawable.type_icon_exemple
        )
        val nameListH = arrayOf(
            "Nettoyer la maison",
            "Aller au supermarché",
            "Faire du jogging 3 fois")
        val typeListH = arrayOf("Ménager", "Divers", "Sport")
        val importanceListH = arrayOf(true, false, false)
        for (i in iconListH.indices) {
            listDataH = ListData(
                nameListH[i],
                typeListH[i], importanceListH[i], iconListH[i]
            )
            dataArrayListH.add(listDataH)
        }
        listAdapterH = ListAdapter(this, dataArrayListH)
        binding.listeTachesHebdomadaire.adapter = listAdapterH

        /** Example of tasks for Journalier and Hebdomadaire
        val journalierTasks = listOf(
            "50 minutes de travail sur projet X",
            "Faire 100 pompes",
            "Lire 20 pages d'un livre"
        )

        val hebdomadaireTasks = listOf(
            "Nettoyer la maison",
            "Aller au supermarché",
            "Faire du jogging 3 fois"
        )

        // Set up adapters for ListViews
        val journalierAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, journalierTasks)
        val hebdomadaireAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, hebdomadaireTasks)

        // Bind adapters to ListViews
        binding.listeTachesJournalier.adapter = journalierAdapter
        binding.listeTachesHebdomadaire.adapter = hebdomadaireAdapter

        */

        homeViewModel.text.observe(viewLifecycleOwner) {
            // Use ViewModel if needed
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
