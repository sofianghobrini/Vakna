package com.app.vakna.ui.magasin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.app.vakna.R
import com.app.vakna.controller.ControllerMagasin
import com.app.vakna.databinding.FragmentMagasinBinding
import com.google.android.material.tabs.TabLayoutMediator

class MagasinFragment : Fragment() {

    private var _binding: FragmentMagasinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMagasinBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ControllerMagasin(binding)

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
