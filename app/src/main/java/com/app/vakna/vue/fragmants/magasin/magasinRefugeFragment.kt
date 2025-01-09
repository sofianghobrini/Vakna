package com.app.vakna.ui.magasin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.adapters.GridCompagnonsAdapter
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.databinding.FragmentObjetBinding
import com.app.vakna.modele.dao.refugestore.RefugeStore
import com.app.vakna.modele.gestionnaires.ShopRefuge

class magasinRefugeFragment : Fragment() {

    private var _binding: FragmentObjetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObjetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()
        val shopRefuge= ShopRefuge(context)
        val listRefuge = shopRefuge.obtenirRefugesStore()

        val sortedRefuge = listRefuge.sortedWith(compareBy<RefugeStore> { it.getPrix() }.thenBy { it.getNom() })
        val gridRefuge = ShopRefuge.setToGridDataArray(sortedRefuge)

        val adapter = GridCompagnonsAdapter(context, gridRefuge)
        binding.gridViewItems.adapter = adapter
    }
}