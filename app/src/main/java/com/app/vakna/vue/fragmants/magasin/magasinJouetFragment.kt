package com.app.vakna.vue.fragmants.magasin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.adapters.GridConsommableAdapter
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.gestionnaires.Shop
import com.app.vakna.modele.dao.TypeObjet

class magasinJouetFragment : Fragment() {
    private lateinit var binding: FragmentMagasinBinding // Généré via View Binding.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMagasinBinding.inflate(inflater, container, false)
        val context = binding.root.context


        val shop = Shop(context)
        val jouet : List<Objet> = shop.listerObjet(TypeObjet.JOUET)


        val gridDataList: ArrayList<GridConsommableData> = Shop.setToGridDataArray(jouet)


        val adapter = GridConsommableAdapter(requireContext(), gridDataList)
        binding.gridViewItems.adapter = adapter

        return binding.root
    }
}