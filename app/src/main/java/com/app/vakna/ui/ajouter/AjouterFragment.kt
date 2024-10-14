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
        val taskNameInput = binding.taskNameInput
        val taskTypeSpinner = binding.taskTypeSpinner
        val otherRadioButton = binding.radioOther
        val otherInput = binding.taskOtherInput
        val confirmButton = binding.confirmationButton

        // Populate spinner with task types
        val taskTypes = arrayOf("Selection", "Travail", "Personnel", "Urgent")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, taskTypes)
        taskTypeSpinner.adapter = adapter

        // Confirmation button click listener
        confirmButton.setOnClickListener {
            val taskName = taskNameInput.text.toString()
            val taskType = if (otherRadioButton.isChecked) {
                otherInput.text.toString()
            } else {
                taskTypeSpinner.selectedItem.toString()
            }

            Toast.makeText(requireContext(), "Tâche ajoutée: $taskName, Type: $taskType", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
