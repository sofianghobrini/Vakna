package com.app.vakna.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import com.app.vakna.R
import com.app.vakna.vue.MainActivity

class NavigationHandler {
    companion object {
        fun navigationFragmentVersFragment(context: Context, destination: Int, tabSelectionne: String? = null) {
            if (context is MainActivity) {
                val navController = context.findNavController(R.id.nav_host_fragment_activity_main)
                val bundle = tabSelectionne?.let {
                    Bundle().apply { putString("tabSelectionne", it) }
                }
                navController.navigate(destination, bundle)
            }
        }

        fun navigationActiviteVersFragment(context: Context, destination: String) {
            val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra("navigateTo", destination)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }
}