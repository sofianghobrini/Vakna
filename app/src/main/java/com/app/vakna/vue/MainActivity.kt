package com.app.vakna.vue

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.vakna.R
import com.app.vakna.controller.ControllerMain
import com.app.vakna.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        handlePremierLancement()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ControllerMain(binding, intent)
        applyConstraintLayoutBackground()
        setUpNavMenu()
    }

    override fun onStop () {
        super.onStop()
        saveCurrentLauchTime()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(base))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun applyConstraintLayoutBackground() {
        val constraintLayout = binding.root.findViewById<ConstraintLayout>(R.id.main_layout)

        // Vérifier si le thème sombre est activé
        val isDarkTheme = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            .getBoolean("darkTheme", false)

        // Définir le drawable approprié
        val backgroundDrawable = if (isDarkTheme) {
            R.drawable.background_color_main_dark // Nom du fichier pour le mode sombre
        } else {
            R.drawable.background_color_main // Nom du fichier pour le mode clair
        }

        // Appliquer le drawable comme arrière-plan
        constraintLayout.setBackgroundResource(backgroundDrawable)
    }

    private fun handlePremierLancement() {
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        if (premierLancement()) {
            val intent = Intent(this, CreerCompagnonActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun premierLancement(): Boolean {
        return sharedPreferences.getBoolean("isFirstLaunch", true)
    }

    private fun setUpNavMenu() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_quetes,
                R.id.navigation_compagnon,
                R.id.navigation_magasin
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_quetes -> {
                    navController.navigate(R.id.navigation_quetes)
                    true
                }

                R.id.navigation_compagnon -> {
                    navController.navigate(R.id.navigation_compagnon)
                    true
                }

                R.id.navigation_magasin -> {
                    navController.navigate(R.id.navigation_magasin)
                    true
                }

                else -> false
            }
        }
    }
    private fun isDarkThemeEnabled(): Boolean {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("darkTheme", false)
    }
    private fun saveCurrentLauchTime() {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong("lastLaunchTime", System.currentTimeMillis())
            apply()
        }
    }
}