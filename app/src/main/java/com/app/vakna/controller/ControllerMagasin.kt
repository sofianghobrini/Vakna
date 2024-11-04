package com.app.vakna.controller

import android.util.Log
import android.widget.GridView
import com.app.vakna.R
import com.app.vakna.adapters.GridAdapter
import com.app.vakna.adapters.GridAdapterInventaire
import com.app.vakna.adapters.GridData
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.Inventaire
import com.app.vakna.modele.Objet
import com.app.vakna.modele.ObjetObtenu
import com.app.vakna.modele.Shop
import com.app.vakna.modele.TypeObjet
import com.app.vakna.modele.dao.InventaireDAO
import com.app.vakna.modele.dao.ObjetDAO
import com.google.android.material.tabs.TabLayout


class ControllerMagasin(private val binding: FragmentMagasinBinding) {

    private val context = binding.root.context
    private val inventaireDAO = InventaireDAO(context)
    private val shop = Shop(context)

    init {

        afficherNombreDeCoins()

        val distinctTypeList = shop.getObjets().map { it.getType() }
            .distinct()

        distinctTypeList.forEach {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(it.name))
        }

        val items = shop.getObjetsParType(distinctTypeList.first())
        setupGridView(items)


        // Handle tab selection to change the displayed grid items
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedTypeName = tab?.text.toString()

                val selectedType = TypeObjet.valueOf(selectedTypeName)

                val filteredItems = shop.getObjetsParType(selectedType)

                setupGridView(filteredItems)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    // Use the correct data type (List<GridData>) in setupGridView
    private fun setupGridView(items: List<Objet>) {
        val sortedItems = items.sortedWith(compareBy<Objet> { it.getType() }.thenBy { it.getId() })

        // Convert sorted items to GridData format
        val gridItems = Shop.setToGridDataArray(sortedItems)

        val adapter = GridAdapter(context, gridItems)
        binding.gridViewItems.adapter = adapter
    }
    private fun afficherNombreDeCoins() {
        val nombreDeCoins = inventaireDAO.obtenirPieces()
        val texteNombreCoins = binding.texteNombreCoins
        texteNombreCoins.text = "$nombreDeCoins"
    }
}