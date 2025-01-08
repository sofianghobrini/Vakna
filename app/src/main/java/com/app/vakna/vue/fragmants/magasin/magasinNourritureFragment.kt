package com.app.vakna.ui.magasin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.adapters.GridConsommableAdapter
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.*
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.gestionnaires.Shop

class magasinNourritureFragment : Fragment() {
    private var _binding: FragmentMagasinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMagasinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()

        val shop = Shop(context)
        val nourritureList: List<Objet> = shop.listerObjet(TypeObjet.NOURRITURE)

        val sortedNourriture = nourritureList.sortedWith(compareBy<Objet> { it.getPrix() }.thenBy { it.getNom() })
        val gridDataList: ArrayList<GridConsommableData> = Shop.setToGridDataArray(sortedNourriture)

        // Configurer l'adaptateur pour le GridView.
        val adapter = GridConsommableAdapter(requireContext(), gridDataList)
        binding.gridViewItems.adapter = adapter
    }
}