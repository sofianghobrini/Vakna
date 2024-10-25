package com.app.vakna

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerGererProjet
import com.app.vakna.databinding.ActivityGererProjetBinding

class GererProjetsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGererProjetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityGererProjetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val root: View = binding.root

        ControllerGererProjet(binding)
    }
}
