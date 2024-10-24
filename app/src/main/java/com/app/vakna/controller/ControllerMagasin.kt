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
    private val shopDAO = ObjetDAO(context)
    private val shop = Shop(context)

    init {
        val jouet1 = Objet(0, "Jouet 1", 15, 5, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet2 = Objet(1, "Jouet 2", 20, 6, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet3 = Objet(2, "Jouet 3", 25, 7, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet4 = Objet(3, "Jouet 4", 30, 8, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet5 = Objet(4, "Jouet 5", 35, 9, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet6 = Objet(5, "Jouet 6", 40, 10, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet7 = Objet(6, "Jouet 7", 45, 11, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet8 = Objet(7, "Jouet 8", 50, 12, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet9 = Objet(8, "Jouet 9", 55, 13, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet10 = Objet(9, "Jouet 10", 60, 14, TypeObjet.JOUET, "jouet", "placeholder")

        val jouetsItems = listOf(jouet1, jouet2, jouet3, jouet4, jouet5, jouet6, jouet7, jouet8, jouet9, jouet10)

        val kebab = Objet(10, "Kebab", 8, 10, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val pizza = Objet(11, "Pizza", 12, 15, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val burger = Objet(12, "Burger", 10, 8, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val sandwich = Objet(13, "Sandwich", 7, 5, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val salade = Objet(14, "Salade", 6, 4, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val pasta = Objet(15, "Pasta", 11, 9, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val sushi = Objet(16, "Sushi", 14, 13, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val steak = Objet(17, "Steak", 20, 18, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val nourritureItems = listOf(kebab, pizza, burger, sandwich, salade, pasta, sushi, steak)

        val allItems = jouetsItems + nourritureItems

        allItems.forEach {
            Log.e("testBug", it.toString())
            shopDAO.inserer(it)
        }

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