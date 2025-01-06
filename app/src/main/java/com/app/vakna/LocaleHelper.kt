package com.app.vakna

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.util.Locale

object LocaleHelper {

    private const val LANGUAGE_PREFERENCE_KEY = "language"
    private const val APP_PREFERENCES = "AppPreferences"

    fun getLanguage(context: Context): String {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString("language", "en") ?: "en"
    }

    fun setLocale(context: Context): Context {
        val language = getLanguage(context)
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    fun getLanguagePreference(context: Context): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getString(LANGUAGE_PREFERENCE_KEY, Locale.getDefault().language) ?: "en"
    }

    fun saveLanguagePreference(context: Context, language: String) {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putString("language", language)
            apply()
        }
    }
}
