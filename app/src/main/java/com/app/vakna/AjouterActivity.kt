package com.app.vakna

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerAjouterTache
import com.app.vakna.databinding.ActivityAjouterBinding
import com.app.vakna.modele.dao.TacheDAO

class AjouterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAjouterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAjouterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = TacheDAO(this)

        val confirmButton: Button = binding.boutonCreerTache
        val annulerButton: Button = binding.boutonAnnulerCreation

        confirmButton.setOnClickListener {
            val controller = ControllerAjouterTache(binding.root, dao)
            controller.ConfirmTache()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigateTo", "Taches")
            startActivity(intent)
        }

        annulerButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigateTo", "Taches")
            startActivity(intent)
        }
    }
}