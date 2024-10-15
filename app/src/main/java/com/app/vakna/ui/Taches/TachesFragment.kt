package com.app.vakna.ui.Taches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.vakna.databinding.FragmentTachesBinding

class TachesFragment : Fragment() {

    private var _binding: FragmentTachesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(TachesViewModel::class.java)

        _binding = FragmentTachesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Example of tasks for Journalier and Hebdomadaire
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
