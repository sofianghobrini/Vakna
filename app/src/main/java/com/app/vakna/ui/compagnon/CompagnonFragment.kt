package com.app.vakna.ui.compagnon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.controller.ControllerCompagnon
import com.app.vakna.databinding.FragmentCompagnonBinding

class CompagnonFragment : Fragment() {

    private var _binding: FragmentCompagnonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompagnonBinding.inflate(inflater, container, false)

        ControllerCompagnon(binding)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
