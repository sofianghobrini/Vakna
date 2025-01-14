package com.app.vakna.controller

import com.app.vakna.R
import com.app.vakna.adapters.GridCompagnonsAdapter
import com.app.vakna.adapters.GridRefugesAdapter
import com.app.vakna.databinding.FragmentMagasinBinding
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.dao.refugestore.RefugeStore
import com.app.vakna.modele.gestionnaires.MagasinObjets
import com.app.vakna.modele.gestionnaires.MagasinCompagnons
import com.app.vakna.modele.gestionnaires.MagasinRefuge
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
import com.app.vakna.adapters.GridAdapter
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.adapters.GridData
import com.app.vakna.adapters.ViewPagerAdapter
import com.app.vakna.adapters.ViewPagerAdapterConsommable


class ControllerMagasin(private val binding: FragmentMagasinBinding, private val tabSelectionne: String?) {

    private val context = binding.root.context
    private val inventaireDAO = InventaireDAO(context)
    private val magasinCompagnons = MagasinCompagnons(context)
    private val magasinRefuge = MagasinRefuge(context)
    private val magasinObjets = MagasinObjets(context)
    private val listCompagnons = magasinCompagnons.obtenirCompagnons()
    private val listRefugesStore = magasinRefuge.obtenirRefugesStore()

    init {
        afficherNombreDeCoins()

        setUpTabSelectionne()

        binding.switchMagasinCompagnon.setOnClickListener {
            menuCompagnonsSelectionne()
            setupViewSwipeCompagnonRefuge()
        }

        binding.switchMagasinConsom.setOnClickListener {
            menuConsommableSelectionne()
            setupViewSwipeNourritureJouet()
        }

        binding.buttonRefresh.setOnClickListener {
            if (verifierConnection(context)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val objetsShop = magasinObjets.obtenirObjetsEnLigne(context)

                    CoroutineScope((Dispatchers.Main)).launch {
                        //setupGridView(objetsShop)
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

    private fun setUpTabSelectionne() {

        when (tabSelectionne) {
            "Nourriture" -> {
                setupViewSwipeNourritureJouet(defaultTab = 1)
            }
            "Compagnons" -> {
                menuCompagnonsSelectionne()
                setupViewSwipeCompagnonRefuge(defaultTab = 0)
            }
            "Refuges" -> {
                menuCompagnonsSelectionne()
                setupViewSwipeCompagnonRefuge(defaultTab = 1)
            }
            else -> {
                setupViewSwipeNourritureJouet(defaultTab = 0)
            }
        }
    }


    private fun menuCompagnonsSelectionne() {
        binding.switchMagasinCompagnon.backgroundTintList =
            context.getColorStateList(R.color.purple_500)
        binding.switchMagasinConsom.backgroundTintList =
            context.getColorStateList(R.color.gray)
    }

    private fun menuConsommableSelectionne() {
        binding.switchMagasinConsom.backgroundTintList =
            context.getColorStateList(R.color.purple_500)
        binding.switchMagasinCompagnon.backgroundTintList =
            context.getColorStateList(R.color.gray)
    }

    private fun setupViewSwipeNourritureJouet(defaultTab: Int = 0) {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val pages = SetPageConsommable()
        val tabTitles = listOf(R.string.tab_jouet, R.string.tab_nourriture)

        viewPager.adapter = ViewPagerAdapterConsommable(context, pages)

        viewPager.getChildAt(0).apply {
            if (this is RecyclerView) {
                this.setOnTouchListener { _, _ -> false }
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = context?.getString(tabTitles[position])
        }.attach()

        // Sélectionne l'onglet par défaut
        viewPager.setCurrentItem(defaultTab, false)
    }



    private fun setupViewSwipeCompagnonRefuge(defaultTab: Int = 0) {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val pages = SetPageCompagnon()
        val tabTitles = listOf(R.string.tab_compagnon, R.string.refuges)

        val adapterProvider: (Context, List<GridData>) -> GridAdapter = { context, items ->
            if (tabLayout.selectedTabPosition == 0) {
                GridCompagnonsAdapter(context, ArrayList(items))
            } else {
                GridRefugesAdapter(context, ArrayList(items))
            }
        }

        viewPager.adapter = ViewPagerAdapter(context, pages, adapterProvider)

        viewPager.getChildAt(0).apply {
            if (this is RecyclerView) {
                this.setOnTouchListener { _, _ -> false }
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = context?.getString(tabTitles[position])
        }.attach()

        viewPager.setCurrentItem(defaultTab, false)
    }



    private fun SetPageConsommable(): List<ArrayList<GridConsommableData>> {
        val nourritureList: List<Objet> = magasinObjets.listerObjet(TypeObjet.NOURRITURE)
        val sortedNourriture =
            nourritureList.sortedWith(compareBy<Objet> { it.getPrix() }.thenBy { it.getNom() })
        val gridNourritureList: ArrayList<GridConsommableData> =
           MagasinObjets.setToGridDataArray(sortedNourriture)

        val jouetList: List<Objet> = magasinObjets.listerObjet(TypeObjet.JOUET)
        val sortedJouets =
            jouetList.sortedWith(compareBy<Objet> { it.getPrix() }.thenBy { it.getNom() })
        val gridJouetsList: ArrayList<GridConsommableData> = MagasinObjets.setToGridDataArray(sortedJouets)

        val pages = listOf(
            gridJouetsList,
            gridNourritureList
        )
        return pages
    }


    private fun SetPageCompagnon(): List<ArrayList<GridData>> {
        val sortedCompagnons = listCompagnons.sortedWith(compareBy { it.prix })
        val gridCompagnons = MagasinCompagnons.setToGridDataArray(sortedCompagnons)

        val sortedRefuge = listRefugesStore.sortedWith(compareBy<RefugeStore> { it.getPrix() }.thenBy { it.getNom() })
        val gridRefuge = MagasinRefuge.setToGridDataArray(sortedRefuge)

        val pages = listOf(
            gridCompagnons,
            gridRefuge
        )
        return pages
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