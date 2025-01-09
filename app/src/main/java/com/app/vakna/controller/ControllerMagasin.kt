package com.app.vakna.controller

import com.app.vakna.R
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.dao.refugestore.RefugeStore
import com.app.vakna.modele.gestionnaires.Shop
import com.app.vakna.modele.gestionnaires.ShopCompagnons
import com.app.vakna.modele.gestionnaires.ShopRefuge
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.InventaireDAO
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.adapters.GridData
import com.app.vakna.adapters.ViewPagerAdapter
import com.app.vakna.adapters.ViewPagerAdapterConsommable


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
                    }
                }
            } else {
                Toast.makeText(context, context.getString(R.string.pas_de_connection), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun afficherNombreDeCoins() {
        val nombreDeCoins = inventaireDAO.obtenirPieces()
        val texteNombreCoins = binding.texteNombreCoins
        texteNombreCoins.text = "$nombreDeCoins"
    }

    private fun setupViewSwipeNourritureJouet() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val pages = SetPageConsommable()

        val tabTitles = listOf("Jouets", "Nourriture")

        viewPager.adapter = ViewPagerAdapterConsommable(context, pages)

        viewPager.getChildAt(0).apply {
            if(this is RecyclerView) {
                this.setOnTouchListener { _, _ -> false}
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }



    private fun setupViewSwipeCompagnonRefuge() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val pages = SetPageCompagnon()

        val tabTitles = listOf("Compagnon", "Refuges")

        viewPager.adapter = ViewPagerAdapter(context, pages)

        viewPager.getChildAt(0).apply {
            if(this is RecyclerView) {
                this.setOnTouchListener { _, _ -> false}
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }


    private fun SetPageConsommable(): List<ArrayList<GridConsommableData>> {
        val nourritureList: List<Objet> = shop.listerObjet(TypeObjet.NOURRITURE)
        val sortedNourriture =
            nourritureList.sortedWith(compareBy<Objet> { it.getPrix() }.thenBy { it.getNom() })
        val gridNourritureList: ArrayList<GridConsommableData> =
            Shop.setToGridDataArray(sortedNourriture)

        val jouetList: List<Objet> = shop.listerObjet(TypeObjet.JOUET)
        val sortedJouets =
            jouetList.sortedWith(compareBy<Objet> { it.getPrix() }.thenBy { it.getNom() })
        val gridJouetsList: ArrayList<GridConsommableData> = Shop.setToGridDataArray(sortedJouets)

        val pages = listOf(
            gridNourritureList,
            gridJouetsList
        )
        return pages
    }


    private fun SetPageCompagnon(): List<ArrayList<GridData>> {
        val shopCompagnons = ShopCompagnons(context)
        val listCompagnons = shopCompagnons.obtenirCompagnons()

        val sortedCompagnons = listCompagnons.sortedWith(compareBy { it.prix })
        val gridCompagnons = ShopCompagnons.setToGridDataArray(sortedCompagnons)

        val shopRefuge = ShopRefuge(context)
        val listRefuge = shopRefuge.obtenirRefugesStore()

        val sortedRefuge =
            listRefuge.sortedWith(compareBy<RefugeStore> { it.getPrix() }.thenBy { it.getNom() })
        val gridRefuge = ShopRefuge.setToGridDataArray(sortedRefuge)

        val pages = listOf(
            gridCompagnons,
            gridRefuge
        )
        return pages
    }

    private fun isInternetAvailable(context: Context): Boolean {
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