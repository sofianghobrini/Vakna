package com.app.vakna.ui.magasin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.adapters.GridCompagnonsAdapter
import com.app.vakna.adapters.GridData
import com.app.vakna.adapters.GridRefugesAdapter
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.RefugeStore
import com.app.vakna.modele.ShopRefuge

class magasinRefugeFragment : Fragment() {

    private lateinit var binding: FragmentMagasinBinding // Généré avec view binding.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMagasinBinding.inflate(inflater, container, false)
        val context = binding.root.context
        val shopRefuge= ShopRefuge(context)
        val listRefuge = shopRefuge.getRefugesStore()
        // Exemple de configuration de la vue.

        val refuge: Set<RefugeStore> = listRefuge
        val gridDataList: ArrayList<GridData> = refuge.map { it.toGridData() } as ArrayList<GridData>
        val adapter = GridRefugesAdapter(requireContext(), gridDataList)
        binding.gridViewItems.adapter = adapter

        return binding.root
    }
}