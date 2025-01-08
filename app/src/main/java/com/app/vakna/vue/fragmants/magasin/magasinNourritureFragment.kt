package com.app.vakna.vue.fragmants.magasin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.adapters.GridConsommableAdapter
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.gestionnaires.MagasinObjets

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
        val magasinObjets = MagasinObjets(context)
        val nourriture : List<Objet> = magasinObjets.listerObjet(TypeObjet.NOURRITURE)

        // Transformer les objets en `GridData`.
        val gridDataList: ArrayList<GridConsommableData> = MagasinObjets.setToGridDataArray(nourriture)

        // Configurer l'adaptateur pour le GridView.
        val adapter = GridConsommableAdapter(requireContext(), gridDataList)
        binding.gridViewItems.adapter = adapter

        return binding.root
    }
}