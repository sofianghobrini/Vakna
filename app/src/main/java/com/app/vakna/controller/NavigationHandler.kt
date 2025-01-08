package com.app.vakna.controller

import android.content.Context
import android.content.Intent
import androidx.navigation.findNavController
import com.app.vakna.R
import com.app.vakna.vue.MainActivity

class NavigationHandler {
    companion object {
        fun navigationFragmentVersFragment(context: Context, destination: Int) {
            if (context is MainActivity) {
                val navController = context.findNavController(R.id.nav_host_fragment_activity_main)
                navController.navigate(destination)
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