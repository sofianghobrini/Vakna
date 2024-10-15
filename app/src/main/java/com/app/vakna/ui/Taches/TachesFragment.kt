package com.app.vakna.ui.Taches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vakna.R
import com.app.vakna.databinding.FragmentTachesBinding


class TachesFragment : Fragment() {

    private var _binding: FragmentTachesBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapterJ: ListAdapter
    private lateinit var listAdapterH: ListAdapter
    var dataArrayListJ = ArrayList<ListData>()
    var dataArrayListH = ArrayList<ListData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(TachesViewModel::class.java)

        _binding = FragmentTachesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val iconListJ = intArrayOf(
            R.drawable.type_icon_exemple,
            R.drawable.type_icon_exemple,
            R.drawable.type_icon_exemple
        )
        val nameListJ = arrayOf(
            "50 minutes de travail sur projet X",
            "Faire 100 pompes",
            "Lire 20 pages d'un livre")
        val typeListJ = arrayOf("Projet", "Sport", "Literature")
        val importanceListJ = arrayOf(true, false, false)
        for (i in iconListJ.indices) {
            val listDataJ = ListData(
                nameListJ[i],
                typeListJ[i], importanceListJ[i], iconListJ[i]
            )
            dataArrayListJ.add(listDataJ)
        }

        listAdapterJ = ListAdapter(dataArrayListJ)
        val layoutManagerJ = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // Mise en place d'un divider pour les items de la liste
        binding.listeTachesJournalier.layoutManager = layoutManagerJ

        binding.listeTachesJournalier.addItemDecoration(
            DividerItemDecoration(requireContext(), layoutManagerJ.orientation).apply {
                setDrawable(requireContext().getDrawable(R.drawable.divider_item)!!) // Set your custom divider
            }
        )
        binding.listeTachesJournalier.adapter = listAdapterJ

        // Separation diff Frequences

        val iconListH = intArrayOf(
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
            val listDataH = ListData(
                nameListH[i],
                typeListH[i], importanceListH[i], iconListH[i]
            )
            dataArrayListH.add(listDataH)
        }

        listAdapterH = ListAdapter(dataArrayListH)
        val layoutManagerH = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // Mise en place d'un divider pour les items de la liste
        binding.listeTachesHebdomadaire.layoutManager = layoutManagerH

        binding.listeTachesHebdomadaire.addItemDecoration(
            DividerItemDecoration(requireContext(), layoutManagerH.orientation).apply {
                setDrawable(requireContext().getDrawable(R.drawable.divider_item)!!) // Set your custom divider
            }
        )
        binding.listeTachesHebdomadaire.adapter = listAdapterH

        val layoutManagerM = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // Mise en place d'un divider pour les items de la liste
        binding.listeTachesMensuel.layoutManager = layoutManagerM

        binding.listeTachesMensuel.addItemDecoration(
            DividerItemDecoration(requireContext(), layoutManagerH.orientation).apply {
                setDrawable(requireContext().getDrawable(R.drawable.divider_item)!!) // Set your custom divider
            }
        )

        val imageButton: ImageButton = root.findViewById(R.id.boutonAjouterTache)
        imageButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.navigation_ajouter)
        }

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
