package com.app.vakna.notifications

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.app.vakna.vue.AjouterActivity
import com.app.vakna.vue.MainActivity

class NotificationService : Service() {

    private val CHANNEL_ID = "vakna_channel"

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val message = intent?.getStringExtra("notificationMessage") ?: "Prends soin de ton compagnon !"
        showNotification(message)
        stopSelf()
        return START_NOT_STICKY
    }

    private fun showNotification(message: String) {
        val notificationManager = creerNotificationChannel()

        val notification = creerNotification(message)

        notificationManager.notify(1, notification)
    }

    private fun creerNotification(message: String): Notification {
        // Intention pour ouvrir l'application lorsqu'on clique sur la notification
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("navigateTo", "CompagnonFragment")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification Vakna")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_dialog_info) // Assurez-vous d'avoir une icône dans votre projet
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun creerNotificationChannel(): NotificationManager {
        val name = "Vakna Notifications"
        val descriptionText = "Notifications pour les rappels de compagnon"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        return notificationManager
    }
}
