package com.app.vakna.ui.magasin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.app.vakna.R
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.vue.fragmants.magasin.MagasinAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MagasinNourritureJouetFragment : Fragment() {

    private var _binding: FragmentMagasinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMagasinBinding.inflate(inflater, container, false)

        setupViewSwipe()

        return binding.root
    }

    private fun setupViewSwipe() {
        val fragments = listOf(
            magasinNourritureFragment(),
            magasinJouetFragment()
        )

        val adapter = MagasinAdapter(requireActivity() as FragmentActivity, fragments)
        binding.grid.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.grid.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_nourriture)
                1 -> getString(R.string.tab_jouet)
                else -> ""
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
