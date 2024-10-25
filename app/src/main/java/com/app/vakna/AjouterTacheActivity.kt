package com.app.vakna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerAjouterTache
import com.app.vakna.databinding.ActivityAjouterTacheBinding

class AjouterTacheActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAjouterTacheBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAjouterTacheBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ControllerAjouterTache(binding)
    }
}