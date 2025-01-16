package com.app.vakna.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("notificationMessage") ?: "Prends soin de ton compagnon !"
        val serviceIntent = Intent(context, NotificationService::class.java).apply {
            putExtra("notificationMessage", message)
        }
        context.startService(serviceIntent)
    }
}
