package com.app.vakna.vue.fragmants.magasin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.adapters.GridCompagnonsAdapter
import com.app.vakna.adapters.GridData
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.dao.compagnonstore.CompagnonStore
import com.app.vakna.modele.gestionnaires.MagasinCompagnons

class magasinCompagnonFragment : Fragment() {

    private lateinit var binding: FragmentMagasinBinding // Généré avec view binding.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMagasinBinding.inflate(inflater, container, false)
        val context = binding.root.context
        val magasinCompagnons = MagasinCompagnons(context)
        val listCompagnons = magasinCompagnons.obtenirCompagnons()
        // Exemple de configuration de la vue.

        val companions: List<CompagnonStore> = listCompagnons
        val gridDataList: ArrayList<GridData> = companions.map { it.toGridData() } as ArrayList<GridData>
        val adapter = GridCompagnonsAdapter(requireContext(), gridDataList)
        binding.gridViewItems.adapter = adapter

        return binding.root
    }
}
