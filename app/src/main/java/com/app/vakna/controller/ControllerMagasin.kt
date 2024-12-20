package com.app.vakna.controller

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.app.vakna.R
import com.app.vakna.adapters.GridConsommableAdapter
import com.app.vakna.adapters.GridCompagnonsAdapter
import com.app.vakna.adapters.GridRefugesAdapter
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.CompagnonStore
import com.app.vakna.modele.Objet
import com.app.vakna.modele.RefugeStore
import com.app.vakna.modele.Shop
import com.app.vakna.modele.ShopCompagnons
import com.app.vakna.modele.ShopRefuge
import com.app.vakna.modele.TypeObjet
import com.app.vakna.modele.dao.InventaireDAO
import com.app.vakna.ui.magasin.MagasinAdapter
import com.app.vakna.ui.magasin.magasinCompagnonFragment
import com.app.vakna.ui.magasin.magasinJouetFragment
import com.app.vakna.ui.magasin.magasinNourritureFragment
import com.app.vakna.ui.magasin.magasinRefugeFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast


class ControllerMagasin(private val binding: FragmentMagasinBinding) {

    private val context = binding.root.context
    private val inventaireDAO = InventaireDAO(context)
    private val shopCompagnons = ShopCompagnons(context)
    private val shopRefuge = ShopRefuge(context)
    private val shop = Shop(context)
    private val listCompagnons = shopCompagnons.getCompagnons()
    private val listRefugesStore = shopRefuge.getRefugesStore().toList()

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
            if (isInternetAvailable(context)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val objetsShop = shop.getObjetsEnLigne(context)

                    CoroutineScope((Dispatchers.Main)).launch {
                        setupGridView(objetsShop)
                    }
                }
            } else {
                Toast.makeText(context, context.getString(R.string.pas_connection), Toast.LENGTH_SHORT).show()
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
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Refuges"))

        setupGridViewCompagnons(listCompagnons)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedTypeName = tab?.text.toString()

                when (selectedTypeName) {
                    context.getString(R.string.tab_companions) -> {
                        setupGridViewCompagnons(listCompagnons)
                    }

                    "Refuges" -> {
                        setupGridViewRefuges(listRefugesStore)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun getTabTitle(type: TypeObjet): String {
        return when (type) {
            TypeObjet.JOUET -> context.getString(R.string.tab_toys)
            TypeObjet.NOURRITURE -> context.getString(R.string.tab_food)
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

    private fun setupGridViewRefuges(refuges: List<RefugeStore>) {
        val sortedRefuge = refuges.sortedWith(compareBy<RefugeStore> {it.getPrix()}.thenBy { it.getNom() })

        val gridRefuge = ShopRefuge.setToGridDataArray(sortedRefuge)
        val adapter = GridRefugesAdapter(context, gridRefuge)
        binding.gridViewItems.adapter = adapter
    }

    private fun afficherNombreDeCoins() {
        val nombreDeCoins = inventaireDAO.obtenirPieces()
        val texteNombreCoins = binding.texteNombreCoins
        texteNombreCoins.text = "$nombreDeCoins"
    }

    private fun setupViewSwipeNourritureJouet() {
        val fragments = listOf(
            magasinNourritureFragment(),      // Onglet pour la nourriture
            magasinJouetFragment()       // Onglet pour les jouets
        )

        val adapter = MagasinAdapter((binding.root.context as FragmentActivity), fragments)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> context?.getString(R.string.tab_food)
                2 -> context?.getString(R.string.tab_toys)
                else -> ""
            }
        }.attach()
    }

    private fun setupViewSwipeCompagnonRefuge() {
        val fragments = listOf(
            magasinCompagnonFragment(), // Onglet pour les compagnons
            magasinRefugeFragment(),    // Onglet pour les refuges
        )

        val adapter = MagasinAdapter((binding.root.context as FragmentActivity), fragments)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> context?.getString(R.string.tab_companions)
                1 -> context.getString(R.string.refuges)
                else -> ""
            }
        }.attach()
    }
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }



}