package com.app.vakna.ui.ajouter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.vakna.databinding.FragmentAjouterBinding
class AjouterFragment : Fragment() {

    private var _binding: FragmentAjouterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAjouterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Handle task form inputs
        val tacheNomInput = binding.inputNomTache
        val tacheTypeSpinner = binding.selectTypeTache
        val tacheRadioFrequence = binding.radioFrequenceTache
        val tacheRadioImportance = binding.radioImportanceTache
        val confirmButton = binding.boutonCreerTache
        val annulerButton = binding.boutonAnnulerCreation

        // Confirmation button click listener
        confirmButton.setOnClickListener {
            val tacheNom = tacheNomInput.text.toString()
            val tacheType = tacheTypeSpinner.selectedItem.toString()
            val selectedRadioFrequence = binding.root.findViewById<RadioButton>(tacheRadioFrequence.checkedRadioButtonId)
            val selectedRadioImportance = binding.root.findViewById<RadioButton>(tacheRadioImportance.checkedRadioButtonId)
            if (selectedRadioFrequence == null || tacheNom.isEmpty() || selectedRadioImportance == null) {
                Toast.makeText(requireContext(), "La fréquence ou le nom se sont pas choisi!", Toast.LENGTH_SHORT).show()
            } else {
                val tacheFrequence = selectedRadioFrequence.text.toString()
                val tacheImportance = selectedRadioImportance.text.toString()
                Toast.makeText(requireContext(), "Tâche ajoutée: $tacheNom, Type: $tacheType, Frequence: $tacheFrequence, Importance: $tacheImportance", Toast.LENGTH_SHORT).show()
            }
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
