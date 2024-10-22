package com.app.vakna.controller

import android.widget.GridView
import com.app.vakna.R
import com.app.vakna.adapters.GridAdapter
import com.app.vakna.adapters.GridData
import com.app.vakna.databinding.FragmentMagasinBinding
import com.google.android.material.tabs.TabLayout


class ControllerMagasin(private val binding: FragmentMagasinBinding) {

    private val context = binding.root.context

    private val tabItems = mapOf(
        "Jouets" to listOf(
            GridData(R.drawable.placeholder, "Jouet 1", "5", "10c"),
            GridData(R.drawable.placeholder, "Jouet 2", "8", "15c"),
            GridData(R.drawable.placeholder, "Jouet 3", "12", "20c"),
            GridData(R.drawable.placeholder, "Jouet 1", "5", "10c"),
            GridData(R.drawable.placeholder, "Jouet 2", "8", "15c"),
            GridData(R.drawable.placeholder, "Jouet 3", "12", "20c"),
            GridData(R.drawable.placeholder, "Jouet 1", "5", "10c"),
            GridData(R.drawable.placeholder, "Jouet 2", "8", "15c"),
            GridData(R.drawable.placeholder, "Jouet 3", "12", "20c"),
            GridData(R.drawable.placeholder, "Jouet 1", "5", "10c"),
            GridData(R.drawable.placeholder, "Jouet 2", "8", "15c"),
            GridData(R.drawable.placeholder, "Jouet 3", "12", "20c")
        ),
        "Nourriture" to listOf(
            GridData(R.drawable.placeholder, "Kebab", "15", "5c"),
            GridData(R.drawable.placeholder, "Pizza", "30", "8c"),
            GridData(R.drawable.placeholder, "Burger", "25", "7c"),
            GridData(R.drawable.placeholder, "Kebab", "15", "5c"),
            GridData(R.drawable.placeholder, "Pizza", "30", "8c"),
            GridData(R.drawable.placeholder, "Burger", "25", "7c"),
            GridData(R.drawable.placeholder, "Kebab", "15", "5c"),
            GridData(R.drawable.placeholder, "Pizza", "30", "8c"),
            GridData(R.drawable.placeholder, "Burger", "25", "7c"),
            GridData(R.drawable.placeholder, "Kebab", "15", "5c"),
            GridData(R.drawable.placeholder, "Pizza", "30", "8c"),
            GridData(R.drawable.placeholder, "Burger", "25", "7c")
        )
    )

    init {
        val tabLayout = binding.tabLayout
        val gridView = binding.gridViewItems

        tabItems.keys.forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it))
        }

        // Display the first category's items by default
        val premierItem = tabItems.values.first()
        setupGridView(premierItem, gridView)

        // Handle tab selection to change the displayed grid items
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val itemsSelectione = tabItems[it.text]
                    itemsSelectione?.let { items -> setupGridView(items, gridView) }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // Use the correct data type (List<GridData>) in setupGridView
    private fun setupGridView(items: List<GridData>, gridView: GridView) {
        val adapter = GridAdapter(context, items)
        gridView.adapter = adapter
    }
}