package com.app.vakna.ui.magasin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.adapters.GridCompagnonsAdapter
import com.app.vakna.adapters.GridConsommableAdapter
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.adapters.GridData
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.*
import com.app.vakna.modele.Shop

class magasinNourritureFragment : Fragment() {

    private lateinit var binding: FragmentMagasinBinding // Généré via View Binding.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialiser le binding.
        binding = FragmentMagasinBinding.inflate(inflater, container, false)
        val context = binding.root.context

        // Initialiser le shop et obtenir les objets de type jouet.
        val shop = Shop(context)
        val nourriture : List<Objet> = shop.listerObjet(TypeObjet.NOURRITURE)

        // Transformer les objets en `GridData`.
        val gridDataList: ArrayList<GridConsommableData> = Shop.setToGridDataArray(nourriture)

        // Configurer l'adaptateur pour le GridView.
        val adapter = GridConsommableAdapter(requireContext(), gridDataList)
        binding.gridViewItems.adapter = adapter

        return binding.root
    }
}