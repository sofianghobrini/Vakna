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

        val taskName = intent.getStringExtra("NOM_TACHE") ?: "Tâche inconnue"

        var gestionnaire = GestionnaireDeTaches(binding.root.context)

        val task = gestionnaire.obtenirTaches(taskName).first()

        if (task != null) {
            preFillFields(task)
        } else {
            Toast.makeText(this, "La tâche n'existe pas", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.titreModifierTache.text = "Modifier la tâche \"$taskName\""

        binding.boutonModifierTache.setOnClickListener {
            ControllerModifierTache(binding, taskName).modifierTache()
            val intent = Intent(this, GererActivity::class.java)
            startActivity(intent)
        }

        binding.boutonAnnulerCreation.setOnClickListener {
            val intent = Intent(this, GererActivity::class.java)
            startActivity(intent)
        }
    }

    private fun preFillFields(task: Tache) {
        binding.contenuInclude.inputNomTache.setText(task.nom)
        val taskTypePosition = task.type.ordinal
        binding.contenuInclude.selectTypeTache.setSelection(taskTypePosition)

        when (task.frequence) {
            Frequence.QUOTIDIENNE -> binding.contenuInclude.radioFrequenceTache.check(R.id.radioQuotidien)
            Frequence.HEBDOMADAIRE -> binding.contenuInclude.radioFrequenceTache.check(R.id.radioHebdomadaire)
            Frequence.MENSUELLE -> binding.contenuInclude.radioFrequenceTache.check(R.id.radioMensuel)
        }

        // Set the importance RadioButton
        when (task.importance) {
            Importance.FAIBLE -> binding.contenuInclude.radioImportanceTache.check(R.id.radioFaible)
            Importance.MOYENNE -> binding.contenuInclude.radioImportanceTache.check(R.id.radioMoyen)
            Importance.ELEVEE -> binding.contenuInclude.radioImportanceTache.check(R.id.radioElevee)
        }
    }
}