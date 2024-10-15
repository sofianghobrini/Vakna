package com.app.vakna.ui.ajouter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.vakna.databinding.FragmentAjouterBinding
class AjouterFragment : Fragment() {

    private var _binding: FragmentAjouterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(AjouterViewModel::class.java)

        _binding = FragmentAjouterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Handle task form inputs
        val tacheNomInput = binding.inputNomTache
        val tacheTypeSpinner = binding.selectTypeTache
        val tacheRadioFrequence = binding.radioFrequenceTache
        val confirmButton = binding.boutonCreerTache
        val annulerButton = binding.boutonAnnulerCreation

        // Populate spinner with task types
        val taskTypes = arrayOf("Selection", "Travail", "Personnel", "Urgent")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, taskTypes)
        tacheTypeSpinner.adapter = adapter

        // Confirmation button click listener
        confirmButton.setOnClickListener {
            val tacheNom = tacheNomInput.text.toString()
            val tacheType = tacheTypeSpinner.selectedItem.toString()
            val selectedRadioButton = binding.root.findViewById<RadioButton>(tacheRadioFrequence.checkedRadioButtonId)
            val tacheFrequence =  selectedRadioButton.text.toString()

            Toast.makeText(requireContext(), "Tâche ajoutée: $tacheNom, Type: $tacheType, Frequence: $tacheFrequence", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
