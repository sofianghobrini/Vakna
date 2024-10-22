package com.app.vakna.ui.compagnon

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.controller.ControllerCompagnon
import com.app.vakna.databinding.FragmentCompagnonBinding
import com.app.vakna.modele.dao.AccesJson

class CompagnonFragment : Fragment() {

    private var _binding: FragmentCompagnonBinding? = null
    private val binding get() = _binding!!

    private lateinit var compagnonController: ControllerCompagnon

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompagnonBinding.inflate(inflater, container, false)

        // Initialize the controller and compagnon
        compagnonController = ControllerCompagnon(binding)
        compagnonController.initializeCompagnon()

        // Handle click on the button to modify compagnon name
        binding.editNameButton.setOnClickListener {
            compagnonController.showEditNameDialog()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        Log.i("test", AccesJson("compagnons", binding.root.context).lireFichierJson())
    }
}
