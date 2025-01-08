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
import com.app.vakna.modele.gestionnaires.MagasinObjets
import com.app.vakna.modele.gestionnaires.MagasinCompagnons
import com.app.vakna.modele.gestionnaires.MagasinRefuge
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.InventaireDAO
import com.app.vakna.vue.fragmants.magasin.MagasinAdapter
import com.app.vakna.vue.fragmants.magasin.magasinCompagnonFragment
import com.app.vakna.vue.fragmants.magasin.magasinJouetFragment
import com.app.vakna.vue.fragmants.magasin.magasinNourritureFragment
import com.app.vakna.vue.fragmants.magasin.magasinRefugeFragment
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
    private val magasinCompagnons = MagasinCompagnons(context)
    private val magasinRefuge = MagasinRefuge(context)
    private val magasinObjets = MagasinObjets(context)
    private val listCompagnons = magasinCompagnons.obtenirCompagnons()
    private val listRefugesStore = magasinRefuge.obtenirRefugesStore()

    init {
        afficherNombreDeCoins()

        setUpConsommableTab()

        binding.switchMagasinCompagnon.setOnClickListener {
            binding.switchMagasinCompagnon.backgroundTintList = context.getColorStateList(R.color.tacheTermine)
            binding.switchMagasinConsom.backgroundTintList = null
            setupCompagnonTab()
        }

        binding.switchMagasinConsom.setOnClickListener {
            binding.switchMagasinConsom.backgroundTintList = context.getColorStateList(R.color.tacheTermine)
            binding.switchMagasinCompagnon.backgroundTintList = null
            setUpConsommableTab()
        }

        binding.buttonRefresh.setOnClickListener {
            if (verifierConnection(context)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val objetsShop = magasinObjets.obtenirObjetsEnLigne(context)

                    CoroutineScope((Dispatchers.Main)).launch {
                        setupGridView(objetsShop)
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

    private fun setUpConsommableTab() {
        val tabLayout = binding.tabLayout
        tabLayout.removeAllTabs()

        val distinctTypeList = magasinObjets.obtenirObjets().map { it.getType() }.distinct()

        distinctTypeList.forEach {
            val tabTitle = getTitreTab(it)
            val newTab = tabLayout.newTab()
            tabLayout.addTab(newTab.setText(tabTitle))
        }

        val itemsInitial = magasinObjets.obtenirObjets(distinctTypeList.first())
        setupGridView(itemsInitial)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedTypeName = tab?.text.toString()

                when (selectedTypeName) {
                    context.getString(R.string.tab_jouet) -> {
                        val selectedType = TypeObjet.JOUET
                        val filteredItems = magasinObjets.obtenirObjets(selectedType)
                        setupGridView(filteredItems)
                    }
                    context.getString(R.string.tab_nourriture) -> {
                        val selectedType = TypeObjet.NOURRITURE
                        val filteredItems = magasinObjets.obtenirObjets(selectedType)
                        setupGridView(filteredItems)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun getTitreTab(type: TypeObjet): String {
        return when (type) {
            TypeObjet.JOUET -> context.getString(R.string.tab_jouet)
            TypeObjet.NOURRITURE -> context.getString(R.string.tab_nourriture)
        }
    }

    private fun setupCompagnonTab() {
        binding.tabLayout.removeAllTabs()
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_compagnon))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Refuges"))

        setupGridViewCompagnons(listCompagnons)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedTypeName = tab?.text.toString()

                when (selectedTypeName) {
                    context.getString(R.string.tab_compagnon) -> {
                        setupGridViewCompagnons(listCompagnons)
                    }

                    context.getString(R.string.refuges) -> {
                        setupGridViewRefuges(listRefugesStore)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupGridViewCompagnons(compagnons: List<CompagnonStore>) {
        val sortedCompagnons = compagnons.sortedWith(compareBy {it.prix})

        val gridCompagnons = MagasinCompagnons.setToGridDataArray(sortedCompagnons)
        val adapter = GridCompagnonsAdapter(context, gridCompagnons)
        binding.gridViewItems.adapter = adapter
    }

    private fun setupGridView(items: List<Objet>) {
        val sortedItems = items.sortedWith(compareBy<Objet> { it.getPrix() }.thenBy { it.getNom() })

        val gridItems = MagasinObjets.setToGridDataArray(sortedItems)

        val adapter = GridConsommableAdapter(context, gridItems)
        binding.gridViewItems.adapter = adapter
    }

    private fun setupGridViewRefuges(refuges: List<RefugeStore>) {
        val sortedRefuge = refuges.sortedWith(compareBy<RefugeStore> {it.getPrix()}.thenBy { it.getNom() })

        val gridRefuge = MagasinRefuge.setToGridDataArray(sortedRefuge)
        val adapter = GridRefugesAdapter(context, gridRefuge)
        binding.gridViewItems.adapter = adapter
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
                0 -> context?.getString(R.string.tab_nourriture)
                2 -> context?.getString(R.string.tab_jouet)
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
                0 -> context?.getString(R.string.tab_compagnon)
                1 -> context.getString(R.string.refuges)
                else -> ""
            }
        }.attach()
    }

    fun verifierConnection(context: Context): Boolean {
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