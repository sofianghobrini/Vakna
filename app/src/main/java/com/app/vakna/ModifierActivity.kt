package com.app.vakna

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerModifierTache
import com.app.vakna.databinding.ActivityModifierBinding
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.Importance
import com.app.vakna.modele.Tache

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