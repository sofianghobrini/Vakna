package com.app.vakna.ui.Taches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.controller.ControllerProjets
import com.app.vakna.databinding.FragmentProjetsBinding


class ProjetsFragment : Fragment() {

    private var _binding: FragmentProjetsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjetsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ControllerProjets(binding)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
