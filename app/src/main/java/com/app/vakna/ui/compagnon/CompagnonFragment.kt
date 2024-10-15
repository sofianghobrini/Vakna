package com.app.vakna.ui.compagnon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.app.vakna.databinding.FragmentCompagnonBinding
import com.bumptech.glide.Glide
import com.app.vakna.R
import com.google.android.material.tabs.TabLayout

class CompagnonFragment : Fragment() {

    private var _binding: FragmentCompagnonBinding? = null
    private val binding get() = _binding!!

    private val jouetsItems = listOf(
        "Jouet 1", "Jouet 2", "Jouet 3", "Jouet 4",
        "Jouet 5", "Jouet 6", "Jouet 7", "Jouet 8",
        "Jouet 9", "Jouet 10"
    )

    private val nourritureItems = listOf(
        "Kebab", "Pizza", "Burger", "Sandwich",
        "Salade", "Kebab", "Kebab", "Pasta",
        "Sushi", "Steak"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompagnonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Load GIF using Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.dragon)
            .into(binding.dragonGif)

        // Reference the GridView
        val gridView: GridView = binding.gridViewItems

        // Set up the TabLayout with two tabs
        val tabLayout: TabLayout = binding.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Jouets"))
        tabLayout.addTab(tabLayout.newTab().setText("Nourriture"))

        // Default view is "Jouets"
        setupGridView(jouetsItems, gridView)

        // Handle tab selection between "Jouets" and "Nourriture"
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.text) {
                        "Jouets" -> setupGridView(jouetsItems, gridView)
                        "Nourriture" -> setupGridView(nourritureItems, gridView)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return root
    }

    private fun setupGridView(items: List<String>, gridView: GridView) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        gridView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}