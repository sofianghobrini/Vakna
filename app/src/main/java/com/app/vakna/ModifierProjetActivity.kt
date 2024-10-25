package com.app.vakna

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerModifierProjet
import com.app.vakna.databinding.ActivityModifierProjetBinding
import com.app.vakna.modele.GestionnaireDeProjets
import com.app.vakna.modele.Importance
import com.app.vakna.modele.Projet

class ModifierProjetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModifierProjetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityModifierProjetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val projetName = intent.getStringExtra("NOM_PROJET") ?: "Projet inconnu"

        var gestionnaire = GestionnaireDeProjets(binding.root.context)

        val projet = gestionnaire.obtenirProjets(projetName).first()

        if (projet != null) {
            preFillFields(projet)
        } else {
            Toast.makeText(this, "Le projet n'existe pas", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.titreModifierProjet.text = "Modifier le projet \"$projetName\""

        binding.boutonModifierProjet.setOnClickListener {
            ControllerModifierProjet(binding, projetName).modifierProjet()
            val intent = Intent(this, GererProjetsActivity::class.java)
            startActivity(intent)
        }

        binding.boutonAnnulerCreation.setOnClickListener {
            val intent = Intent(this, GererProjetsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun preFillFields(projet: Projet) {
        binding.contenuInclude.inputNomProjet.setText(projet.nom)
        val taskTypePosition = projet.type.ordinal
        binding.contenuInclude.selectTypeProjet.setSelection(taskTypePosition)

        // Set the importance RadioButton
        when (projet.importance) {
            Importance.FAIBLE -> binding.contenuInclude.radioImportanceProjet.check(R.id.radioFaible)
            Importance.MOYENNE -> binding.contenuInclude.radioImportanceProjet.check(R.id.radioMoyen)
            Importance.ELEVEE -> binding.contenuInclude.radioImportanceProjet.check(R.id.radioElevee)
        }
    }
}