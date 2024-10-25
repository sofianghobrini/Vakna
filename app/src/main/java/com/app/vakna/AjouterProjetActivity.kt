package com.app.vakna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerAjouterProjet
import com.app.vakna.databinding.ActivityAjouterProjetBinding

class AjouterProjetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAjouterProjetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAjouterProjetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ControllerAjouterProjet(binding)
    }
}
