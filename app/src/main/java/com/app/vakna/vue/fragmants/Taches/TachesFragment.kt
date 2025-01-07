package com.app.vakna.vue.fragmants.Taches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.vakna.controller.ControllerTaches
import com.app.vakna.databinding.FragmentTachesBinding


class TachesFragment : Fragment() {

    private var _binding: FragmentTachesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTachesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ControllerTaches(binding)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
