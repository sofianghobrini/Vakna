package com.app.vakna.ui.ajouter

import android.content.Context
import com.app.vakna.controller.ControllerAjouterTache
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.vakna.databinding.FragmentAjouterBinding
import com.app.vakna.modele.dao.TacheDAO

class AjouterFragment : Fragment() {

    private var _binding: FragmentAjouterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dao = TacheDAO(this.requireContext())

        //val files = context?.filesDir
        _binding = FragmentAjouterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val confirmButton = binding.boutonCreerTache
        val annulerButton = binding.boutonAnnulerCreation

        // Confirmation button click listener
        confirmButton.setOnClickListener {
            val controller = ControllerAjouterTache(root, dao)
            controller.ConfirmTache()
            val navController = findNavController()
            navController.popBackStack()
        }

        annulerButton.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
