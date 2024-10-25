package com.app.vakna

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerGererTache
import com.app.vakna.databinding.ActivityGererTacheBinding

class GererTachesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGererTacheBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityGererTacheBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val root: View = binding.root

        ControllerGererTache(binding)
    }
}
