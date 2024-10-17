package com.app.vakna.ui.compagnon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.controlleur.CompagnonController
import com.app.vakna.databinding.FragmentCompagnonBinding

class CompagnonFragment : Fragment() {

    private var _binding: FragmentCompagnonBinding? = null
    private val binding get() = _binding!!

    private lateinit var compagnonController: CompagnonController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompagnonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initializer le controller du compagnon
        compagnonController = CompagnonController(this, binding, requireContext())

        // Initializer le compagnon
        compagnonController.initializeCompagnon()

        // Gérer le clic sur le bouton pour modifier le nom du compagnon
        binding.editNameButton.setOnClickListener {
            compagnonController.showEditNameDialog()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
