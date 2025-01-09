package com.app.vakna.vue.fragmants.magasin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.vakna.controller.ControllerMagasin
import com.app.vakna.databinding.FragmentMagasinBinding

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
