package com.app.vakna.ui.magasin

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import com.app.vakna.R
import com.app.vakna.adapters.GridConsommableAdapter
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.databinding.FragmentObjetBinding
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.gestionnaires.Shop
import com.app.vakna.vue.DetailsObjetActivity
import com.app.vakna.vue.MainActivity


class magasinJouetFragment : Fragment() {
    private var _binding: FragmentObjetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObjetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()

        val shop = Shop(context)
        val nourritureList: List<Objet> = shop.listerObjet(TypeObjet.JOUET)

        val sortedNourriture = nourritureList.sortedWith(compareBy<Objet> { it.getPrix() }.thenBy { it.getNom() })
        val gridDataList: ArrayList<GridConsommableData> = Shop.setToGridDataArray(sortedNourriture)

        view.isFocusable = true
        view.isClickable = true
        // Configurer l'adaptateur pour le GridView.
        val adapter = GridConsommableAdapter(requireContext(), gridDataList)
        binding.gridViewItems.adapter = adapter
        binding.gridViewItems.setOnItemClickListener { adapterView, view, i, l ->
            view.setOnClickListener {
                Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(context, "AAAAA", Toast.LENGTH_SHORT).show()
        }
    }}