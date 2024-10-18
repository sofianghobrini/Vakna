package com.app.vakna

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.databinding.ActivityModifierBinding

class ModifierActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModifierBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityModifierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskName = intent.getStringExtra("NOM_TACHE") ?: "Tâche inconnue"

        binding.titreModifierTache.text = "Modifier la tâche \"$taskName\""

        binding.boutonModifierTache.setOnClickListener {
            Toast.makeText(this, "Bonjour :]", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, GererActivity::class.java)
            startActivity(intent)
        }

        binding.boutonAnnulerCreation.setOnClickListener {
            val intent = Intent(this, GererActivity::class.java)
            startActivity(intent)
        }
    }
}