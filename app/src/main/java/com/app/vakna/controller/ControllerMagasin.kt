package com.app.vakna.controller

import androidx.fragment.app.FragmentActivity
import com.app.vakna.R
import com.app.vakna.adapters.GridConsommableAdapter
import com.app.vakna.adapters.GridCompagnonsAdapter
import com.app.vakna.adapters.GridRefugesAdapter
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.dao.compagnonstore.CompagnonStore
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.dao.refugestore.RefugeStore
import com.app.vakna.modele.gestionnaires.Shop
import com.app.vakna.modele.gestionnaires.ShopCompagnons
import com.app.vakna.modele.gestionnaires.ShopRefuge
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.InventaireDAO
import com.app.vakna.vue.fragmants.magasin.MagasinAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.app.vakna.ui.magasin.magasinCompagnonFragment
import com.app.vakna.ui.magasin.magasinJouetFragment
import com.app.vakna.ui.magasin.magasinNourritureFragment
import com.app.vakna.ui.magasin.magasinRefugeFragment


class ControllerMagasin(private val binding: FragmentMagasinBinding) {

    private val context = binding.root.context
    private val inventaireDAO = InventaireDAO(context)
    private val shopCompagnons = ShopCompagnons(context)
    private val shopRefuge = ShopRefuge(context)
    private val shop = Shop(context)
    private val listCompagnons = shopCompagnons.obtenirCompagnons()
    private val listRefugesStore = shopRefuge.obtenirRefugesStore().toList()

    init {

        afficherNombreDeCoins()

        setupViewSwipeNourritureJouet()

        binding.switchMagasinCompagnon.setOnClickListener {
            binding.switchMagasinCompagnon.backgroundTintList = context.getColorStateList(R.color.tacheTermine)
            binding.switchMagasinConsom.backgroundTintList = null
            setupViewSwipeCompagnonRefuge()
        }

        binding.switchMagasinConsom.setOnClickListener {
            binding.switchMagasinConsom.backgroundTintList = context.getColorStateList(R.color.tacheTermine)
            binding.switchMagasinCompagnon.backgroundTintList = null
            setupViewSwipeNourritureJouet()
        }

        binding.buttonRefresh.setOnClickListener {
            if (isInternetAvailable(context)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val objetsShop = shop.obtenirObjetsEnLigne(context)

                    CoroutineScope((Dispatchers.Main)).launch {
                        setupGridView(objetsShop)
                    }
                }
            } else {
                Toast.makeText(context, context.getString(R.string.pas_de_connection), Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun setupGridView(items: List<Objet>) {
        val sortedItems = items.sortedWith(compareBy<Objet> { it.getPrix() }.thenBy { it.getNom() })

        val gridItems = Shop.setToGridDataArray(sortedItems)

        val adapter = GridConsommableAdapter(context, gridItems)
        binding.grid.gridViewItems.adapter = adapter
    }

    private fun setupGridViewCompagnons(compagnons: List<CompagnonStore>) {
        val sortedCompagnons = compagnons.sortedWith(compareBy {it.prix})

        val gridCompagnons = ShopCompagnons.setToGridDataArray(sortedCompagnons)
        val adapter = GridCompagnonsAdapter(context, gridCompagnons)
        binding.grid.gridViewItems.adapter = adapter
    }

    private fun setupGridViewRefuges(refuges: List<RefugeStore>) {
        val sortedRefuge = refuges.sortedWith(compareBy<RefugeStore> {it.getPrix()}.thenBy { it.getNom() })

        val gridRefuge = ShopRefuge.setToGridDataArray(sortedRefuge)
        val adapter = GridRefugesAdapter(context, gridRefuge)
        binding.grid.gridViewItems.adapter = adapter
    }

    private fun afficherNombreDeCoins() {
        val nombreDeCoins = inventaireDAO.obtenirPieces()
        val texteNombreCoins = binding.texteNombreCoins
        texteNombreCoins.text = "$nombreDeCoins"
    }

    private fun setupViewSwipeNourritureJouet() {
        val fragments = listOf(
            magasinJouetFragment(),      // Onglet pour les jouets
            magasinNourritureFragment()      // Onglet pour la nourriture
        )

        val adapter = MagasinAdapter((context as FragmentActivity), fragments)
        binding.grid.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.grid.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> context.getString(R.string.tab_jouet)
                1 -> context.getString(R.string.tab_nourriture)
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
        binding.grid.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.grid.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> context?.getString(R.string.tab_compagnon)
                1 -> context?.getString(R.string.refuges)
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