package com.app.vakna

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.app.vakna.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: androidx.appcompat.widget.Toolbar = binding.settingsToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setCurrentLanguageRadioButton()

        binding.languageGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedLanguage = when (checkedId) {
                R.id.radioEnglish -> "en"
                R.id.radioFrench -> "fr"
                else -> "en"
            }
            LocaleHelper.saveLanguagePreference(this, selectedLanguage)
            restartApp()
        }

        binding.themeSwitch.isChecked = isDarkThemeEnabled()
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            setTheme(isChecked)
        }

        binding.notificationsSwitch.isChecked = areNotificationsEnabled()
        binding.notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveNotificationPreference(isChecked)
        }
    }

    private fun setCurrentLanguageRadioButton() {
        val currentLanguage = LocaleHelper.getLanguage(this)
        when (currentLanguage) {
            "en" -> binding.languageGroup.check(R.id.radioEnglish)
            "fr" -> binding.languageGroup.check(R.id.radioFrench)
        }
    }

    private fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun isDarkThemeEnabled(): Boolean {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("darkTheme", false)
    }

    private fun setTheme(isDark: Boolean) {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("darkTheme", isDark)
            apply()
        }
        val themeMode = if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    private fun areNotificationsEnabled(): Boolean {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("notifications", true)
    }

    private fun saveNotificationPreference(isEnabled: Boolean) {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("notifications", isEnabled)
            apply()
        }
    }
}