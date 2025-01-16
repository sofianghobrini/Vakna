package com.app.vakna.controller

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.app.vakna.R
import com.app.vakna.databinding.ActivityMainBinding
import com.app.vakna.modele.dao.compagnon.Compagnon
import com.app.vakna.modele.gestionnaires.GestionnaireDeCompagnons
import com.app.vakna.modele.gestionnaires.GestionnaireDeTaches
import com.app.vakna.modele.dao.AccesJson

class ControllerMain(private val binding: ActivityMainBinding, intent: Intent) {

    val context: Context = binding.root.context
    private val navigateTo = intent.getStringExtra("navigateTo")
    private var gestionnaireTaches: GestionnaireDeTaches
    private var gestionnaire: GestionnaireDeCompagnons
    private var compagnon: Compagnon? = null

    init {
        handleNavigationVersFragments()

        appliquerThemeSauvegarder()

        gestionnaireTaches = GestionnaireDeTaches(context)
        gestionnaireTaches.verifierTacheNonAccomplies()

        gestionnaire = GestionnaireDeCompagnons(context)

        val accesJson = AccesJson("taches",context)
        if (!accesJson.fichierExiste()) {
            accesJson.ecrireFichierJson("""{"taches": []}""")
        }

        compagnon = gestionnaire.obtenirActif()

        val dernierLancement = getDernierLancement()

        compagnon?.let {
            diminuerHumeurCompagnon(it.id, dernierLancement)
            diminuerFaimCompagnon(it.id, dernierLancement)
            gestionnaire.notifierSiFaimOuHumeurAtteintZero(it)
        }
    }

    private fun handleNavigationVersFragments() {
        when(navigateTo) {
            "CompagnonFragment" -> NavigationHandler.navigationFragmentVersFragment(context, R.id.navigation_compagnon)
        }
    }

    private fun appliquerThemeSauvegarder() {
        val sharedPreferences = context.getSharedPreferences("AppPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        val estDarkMode = sharedPreferences.getBoolean("darkTheme", false)
        val theme = if (estDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(theme)
    }

    private fun getDernierLancement(): Long? {
        val sharedPref = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val lastLaunchMillis = sharedPref.getLong("lastLaunchTime", -1)

        return if (lastLaunchMillis != -1L) {
            lastLaunchMillis
        } else {
            null
        }
    }

    private fun diminuerFaimCompagnon(id: Int, lastLaunch: Long?) {
        compagnon.let {
            gestionnaire.baisserNivFaim(id, lastLaunch)
        }
    }

    private fun diminuerHumeurCompagnon(id: Int, lastLaunch: Long?) {
        compagnon.let {
            gestionnaire.baisserNivHumeur(id, lastLaunch)
        }
    }
}