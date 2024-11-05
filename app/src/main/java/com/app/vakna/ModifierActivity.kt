package com.app.vakna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerModifierTache
import com.app.vakna.databinding.ActivityModifierBinding

class ModifierActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModifierBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityModifierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ControllerModifierTache(binding, intent)
    }
}