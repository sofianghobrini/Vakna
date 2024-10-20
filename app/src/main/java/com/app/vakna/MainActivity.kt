package com.app.vakna

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.vakna.databinding.ActivityMainBinding
import com.app.vakna.modele.dao.AccesJson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        if (isFirstLaunch()) {
            setFirstLaunch(false)

            val intent = Intent(this, CreerCompagnonActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val accesJson = AccesJson("taches",this)
        if (!accesJson.fichierExiste()) {
            accesJson.ecrireFichierJson("""{"taches": []}""")
        }

        // Now access the toolbar after setContentView is called
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_taches,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        Log.i("test", AccesJson("taches",this).lireFichierJson())
    }

    override fun onResume() {
        super.onResume()
        Log.i("test", AccesJson("taches",this).lireFichierJson())
    }

    private fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean("isFirstLaunch", true)
    }

    private fun setFirstLaunch(isFirst: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isFirstLaunch", isFirst)
        editor.apply()  // Save the changes
    }

}
