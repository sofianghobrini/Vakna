package com.app.vakna.controller

import android.content.Intent
import com.app.vakna.CreerCompagnonActivity
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityCreerCompagnonBinding

class ControllerCreerCompagnon(private val binding: ActivityCreerCompagnonBinding) {

    init {
        com.bumptech.glide.Glide.with(binding.root)
            .asGif()
            .load(R.drawable.dragon)
            .into(binding.dragonGif)

        val boutonConfirmer = binding.boutonCreerCompagnon
        boutonConfirmer.setOnClickListener {
            CreerCompagnon()
            val context = binding.root.context
            if (context is CreerCompagnonActivity) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("navigateTo", "Taches")
                context.startActivity(intent)
            }
        }
    }

    fun CreerCompagnon(){
        /* TODO */
    }
}