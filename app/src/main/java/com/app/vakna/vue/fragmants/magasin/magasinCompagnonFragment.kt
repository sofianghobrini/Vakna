package com.app.vakna.ui.magasin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.adapters.GridCompagnonsAdapter
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.dao.compagnonstore.CompagnonStore
import com.app.vakna.modele.gestionnaires.ShopCompagnons

class magasinCompagnonFragment : Fragment() {
    private var _binding: FragmentMagasinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMagasinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()
        val shopCompagnons = ShopCompagnons(context)
        val listCompagnons = shopCompagnons.obtenirCompagnons()

        val sortedCompagnons = listCompagnons.sortedWith(compareBy<CompagnonStore> { it.prix })
        val gridCompagnons = ShopCompagnons.setToGridDataArray(sortedCompagnons)

        val adapter = GridCompagnonsAdapter(context, gridCompagnons)
        binding.gridViewItems.adapter = adapter
    }
}
