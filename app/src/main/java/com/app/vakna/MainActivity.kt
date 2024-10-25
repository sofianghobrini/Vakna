package com.app.vakna

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
import com.app.vakna.modele.dao.AccesJson
import com.app.vakna.notifications.NotificationReceiver
import com.app.vakna.notifications.NotificationService

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

        val accesJsonTaches = AccesJson("taches",this)
        if (!accesJsonTaches.fichierExiste()) {
            accesJsonTaches.ecrireFichierJson("""{"taches": []}""")
        }

        val accesJsonProjets = AccesJson("projets",this)
        if (!accesJsonProjets.fichierExiste()) {
            accesJsonProjets.ecrireFichierJson("""{"projets": []}""")
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
//        scheduleNotification(this)
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

}
