package com.app.vakna

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.vakna.databinding.ActivityMainBinding
import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.GestionnaireDeCompagnons
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.dao.AccesJson
import com.app.vakna.notifications.NotificationReceiver

import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.TacheDAO
import com.app.vakna.ui.compagnon.CompagnonFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var context: Context
    private lateinit var gestionnaireTaches: GestionnaireDeTaches
    private lateinit var gestionnaire: GestionnaireDeCompagnons
    private var compagnon: Compagnon? = null

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

        context = binding.root.context

        gestionnaireTaches = GestionnaireDeTaches(context)

        gestionnaire = GestionnaireDeCompagnons(CompagnonDAO(context))

        val accesJson = AccesJson("taches",this)
        if (!accesJson.fichierExiste()) {
            accesJson.ecrireFichierJson("""{"taches": []}""")
        }

        compagnon = gestionnaire.obtenirCompagnons().first()

        val lastLaunchTime = getLastLaunchTime()

        compagnon?.let {
            diminuerHumeurCompagnon(it.id, lastLaunchTime)
            diminuerFaimCompagnon(it.id, lastLaunchTime)
        }

        gestionnaireTaches.verifierTacheNonAccomplies()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_taches,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        scheduleNotification(this)

        val navigateTo = intent.getStringExtra("navigateTo")
        if (navigateTo == "CompagnonFragment") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.compagnon_container, CompagnonFragment())
                .commit()
        }
    }

    override fun onStop () {
        super.onStop()
        saveCurrentLauchTime()
    }

    private fun getLastLaunchTime(): Long? {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val lastLaunchMillis = sharedPref.getLong("lastLaunchTime", -1)

        return if (lastLaunchMillis != -1L) {
            lastLaunchMillis
        } else {
            null
        }
    }

    private fun saveCurrentLauchTime() {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong("lastLaunchTime", System.currentTimeMillis())
            apply()
        }
    }

    private fun scheduleNotification(context: Context) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val triggerAtMillis = SystemClock.elapsedRealtime() + 15000
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent)
    }

    private fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean("isFirstLaunch", true)
    }

    private fun setFirstLaunch(isFirst: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isFirstLaunch", isFirst)
        editor.apply()  // Save the changes
    }
    private fun diminuerFaimCompagnon(id: Int, lastLaunch: Long?) {
        compagnon?.let {
            gestionnaire.baisserNivFaim(id, context, lastLaunch)
        }
    }
    private fun diminuerHumeurCompagnon(id: Int, lastLaunch: Long?) {
        compagnon?.let {
            gestionnaire.baisserNivHumeur(id, context, lastLaunch)
        }
    }

}
