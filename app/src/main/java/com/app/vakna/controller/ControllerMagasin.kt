package com.app.vakna.controller

import com.app.vakna.R
import com.app.vakna.adapters.GridConsommableAdapter
import com.app.vakna.adapters.GridCompagnonsAdapter
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.CompagnonStore
import com.app.vakna.modele.Objet
import com.app.vakna.modele.Shop
import com.app.vakna.modele.ShopCompagnons
import com.app.vakna.modele.TypeObjet
import com.app.vakna.modele.dao.CompagnonStoreDAO
import com.app.vakna.modele.dao.InventaireDAO
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ControllerMagasin(private val binding: FragmentMagasinBinding) {

    private val context = binding.root.context
    private val inventaireDAO = InventaireDAO(context)
    private val shopCompagnons = ShopCompagnons(context)
    private val shop = Shop(context)
    private val listCompagnons = shopCompagnons.getCompagnons()

    init {

        afficherNombreDeCoins()

        setupShopTabs()

        binding.switchMagasinCompagnon.setOnClickListener {
            setupCompagnonTab()
        }

        binding.switchMagasinConsom.setOnClickListener {
            setupShopTabs()
        }

        binding.buttonRefresh.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objetsShop = shop.getObjetsEnLigne()

                CoroutineScope((Dispatchers.Main)).launch {
                    setupGridView(objetsShop)
                }
            }
        }

    }

    private fun setupShopTabs() {
        binding.tabLayout.removeAllTabs()
        val distinctTypeList = shop.getObjets().map { it.getType() }.distinct()

        distinctTypeList.forEach {
            val tabTitle = getTabTitle(it)
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(tabTitle))
        }

        val initialItems = shop.getObjetsParType(distinctTypeList.first())
        setupGridView(initialItems)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedTypeName = tab?.text.toString()

                when (selectedTypeName) {
                    context.getString(R.string.tab_companions) -> setupGridViewCompagnons(listCompagnons)
                    context.getString(R.string.tab_toys) -> {
                        val selectedType = TypeObjet.JOUET
                        val filteredItems = shop.getObjetsParType(selectedType)
                        setupGridView(filteredItems)
                    }
                    context.getString(R.string.tab_food) -> {
                        val selectedType = TypeObjet.NOURRITURE
                        val filteredItems = shop.getObjetsParType(selectedType)
                        setupGridView(filteredItems)
                    }
                    else -> {
                        try {
                            val selectedType = TypeObjet.valueOf(selectedTypeName)
                            val filteredItems = shop.getObjetsParType(selectedType)
                            setupGridView(filteredItems)
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupCompagnonTab() {
        binding.tabLayout.removeAllTabs()
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_companions))

        setupGridViewCompagnons(listCompagnons)
    }

    private fun getTabTitle(type: TypeObjet): String {
        return when (type) {
            TypeObjet.JOUET -> context.getString(R.string.tab_toys)
            TypeObjet.NOURRITURE -> context.getString(R.string.tab_food)
            else -> type.name
        }
    }

    private fun setupGridView(items: List<Objet>) {
        val sortedItems = items.sortedWith(compareBy<Objet> { it.getPrix() }.thenBy { it.getNom() })

        val gridItems = Shop.setToGridDataArray(sortedItems)

        val adapter = GridConsommableAdapter(context, gridItems)
        binding.gridViewItems.adapter = adapter
    }

    private fun setupGridViewCompagnons(compagnons: List<CompagnonStore>) {
        val sortedCompagnons = compagnons.sortedWith(compareBy<CompagnonStore> {it.prix}.thenBy { it.nom })

        val gridCompagnons = ShopCompagnons.setToGridDataArray(sortedCompagnons)
        val adapter = GridCompagnonsAdapter(context, gridCompagnons)
        binding.gridViewItems.adapter = adapter
    }

    private fun afficherNombreDeCoins() {
        val nombreDeCoins = inventaireDAO.obtenirPieces()
        val texteNombreCoins = binding.texteNombreCoins
        texteNombreCoins.text = "$nombreDeCoins"
    }
}