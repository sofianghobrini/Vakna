package com.app.vakna.notifications

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.vakna.AjouterTacheActivity
import java.util.Timer
import java.util.TimerTask

class NotificationService : Service() {

    private val CHANNEL_ID = "default"
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private val TAG = "Timers"
    private val SECS_NOTIF = 1 // time interval in seconds
    private val handler = Handler()


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startTimer()
        return START_STICKY
    }

    override fun onCreate() {
        Log.e(TAG, "onCreate")
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        stopTimerTask()
        super.onDestroy()
    }

    private fun startTimer() {
        timer = Timer()

        initializeTimerTask()

        // Aprés 1000ms le timer va s'activer toutes les SECS_NOTIF secondes
        timer?.schedule(timerTask, 1000, (SECS_NOTIF * 1000).toLong())
    }

    private fun stopTimerTask() {
        timer?.cancel()
        timer = null
    }

    private fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    showTestNotification()
                }
            }
        }
    }

    public fun showTestNotification() {
        val notificationManager = creerNotificationChannel()

        val notification = creerTestNotification()

        notificationManager.notify(1, notification)
    }

    private fun creerTestNotification(): Notification {
        val intent = Intent(this, AjouterTacheActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, "default")
            .setContentTitle("Tu nous manque!")
            .setContentText("Ça fait longtemps que tu n'as pas accomplis de tâches!")
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Tu peux réessayer d'utiliser l'application en créeant une nouvelle tâche et en la completant!")
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_input_add, "Ajouter Tâche",
                pendingIntent)
            .build()
    }

    private fun creerNotificationChannel(): NotificationManager {
        val name = "Vakna"
        val descriptionText = "Notification de Vakna"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        return notificationManager
    }
}