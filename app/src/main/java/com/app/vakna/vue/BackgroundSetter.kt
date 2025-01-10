package com.app.vakna.vue

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.vakna.R

object BackgroundSetter {
    fun applyConstraintLayoutBackground(activity: Activity, layout: Int) {
        val constraintLayout = activity.findViewById<ConstraintLayout>(layout)

        // Définir le drawable approprié
        val backgroundDrawable = if (isDarkThemeEnabled(activity)) {
            R.drawable.background_color_main_dark // Nom du fichier pour le mode sombre
        } else {
            R.drawable.background_color_main // Nom du fichier pour le mode clair
        }

        // Appliquer le drawable comme arrière-plan
        constraintLayout.setBackgroundResource(backgroundDrawable)
    }

    private fun isDarkThemeEnabled(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("darkTheme", false)
    }
}