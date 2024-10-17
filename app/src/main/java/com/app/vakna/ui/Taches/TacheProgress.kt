package com.app.vakna.ui.Taches;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.app.vakna.R
import com.app.vakna.databinding.FragmentTachesBinding;

public class TacheProgress {
    private var _binding: FragmentTachesBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapterJ: ListAdapter
    private lateinit var listAdapterH: ListAdapter
    private lateinit var listAdapterM: ListAdapter
    private var completedJournalier = 0
    private var completedHebdomadaire = 0
    private var completedMensuel = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTachesBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val imageButton: ImageButton = root.findViewById(R.id.boutonAjouterTache)
        imageButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.navigation_ajouter)
        }

        return root
    }
}
