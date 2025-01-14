package com.app.vakna.controller

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.vakna.vue.CreerCompagnonActivity
import com.app.vakna.vue.MainActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityCreerCompagnonBinding
import com.app.vakna.modele.dao.compagnon.Compagnon
import com.app.vakna.modele.gestionnaires.MagasinCompagnons
import com.app.vakna.modele.dao.compagnon.CompagnonDAO
import com.bumptech.glide.Glide

/**
 * Contrôleur pour gérer la création d'un nouveau compagnon
 * @param binding Le binding associé à l'activité de création du compagnon
 */
class ControllerCreerCompagnon(private val binding: ActivityCreerCompagnonBinding) {

    private val idInitial = 1
    private val REQUEST_CODE = 101
    private val context: Context = binding.root.context
    private var magasinCompagnons = MagasinCompagnons(context)

    init {
        demandeDesPermissions()

        affichageEspece()

        binding.boutonCreerCompagnon.isEnabled = false

        binding.inputNomCompagnon.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.boutonCreerCompagnon.isEnabled = !s.isNullOrEmpty()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.boutonCreerCompagnon.setOnClickListener {
            creationDuCompagnon()
        }

        // Configurer l'éditeur de texte pour détecter l'appui sur "Enter"
        binding.inputNomCompagnon.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                if (binding.boutonCreerCompagnon.isEnabled) {
                    creationDuCompagnon()
                }
                true
            } else {
                false
            }
        }
    }

    private fun demandeDesPermissions() {
        val permissionsNeeded = arrayOf(
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.SCHEDULE_EXACT_ALARM,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.USE_EXACT_ALARM
        )

        val permissionsToRequest = permissionsNeeded.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(context as CreerCompagnonActivity, permissionsToRequest, REQUEST_CODE)
        }
    }

    private fun affichageEspece() {
        val especeList = listOf("Dragon", "Lapin", "Chat", "Licorne", "Serpent", "Ecureuil")
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, especeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.EspeceSelect.adapter = adapter

        afficherImageCompagnon(especeList.first())

        binding.EspeceSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val especeSelectionnee = especeList[position]
                afficherImageCompagnon(especeSelectionnee)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    /**
     * Fonction privée pour affiche le visuel du campagnon
     */
    private fun afficherImageCompagnon(espece: String) {
        val imageRes = when (espece) {
            "Dragon" -> magasinCompagnons.obtenirCompagnon("Dragon")!!.apparenceDefaut()
            "Lapin" -> magasinCompagnons.obtenirCompagnon("Lapin")!!.apparenceDefaut()
            "Chat" -> magasinCompagnons.obtenirCompagnon("Chat")!!.apparenceDefaut()
            "Licorne" -> magasinCompagnons.obtenirCompagnon("Licorne")!!.apparenceDefaut()
            "Serpent" -> magasinCompagnons.obtenirCompagnon("Serpent")!!.apparenceDefaut()
            "Ecureuil" -> magasinCompagnons.obtenirCompagnon("Ecureuil")!!.apparenceDefaut()
            else -> magasinCompagnons.obtenirCompagnon("Dragon")!!.apparenceDefaut()
        }
        Glide.with(context)
            .load(imageRes)
            .into(binding.dragonGif)
    }

    private fun creationDuCompagnon() {
        creerCompagnon()
        setPremierLancement()
        navigateToMainActivity()
    }

    /**
     * Fonction privée pour créer un nouveau compagnon
     */
    private fun creerCompagnon()  {
        val nomCompagnon = binding.inputNomCompagnon.text.toString().trim()
        val nomEspece = binding.EspeceSelect.selectedItem.toString()
        val nouveauCompagnon = Compagnon(
            id = idInitial,
            nom = nomCompagnon,
            faim = 50,
            humeur = 50,
            xp = 0,
            espece = nomEspece,
            personnalite = Compagnon.personnalite_compagnon(),
            actif = true
        )

        val compagnonDAO = CompagnonDAO(context)
        val insertionReussie = compagnonDAO.inserer(nouveauCompagnon)
        assert(insertionReussie) { "Un compagnon existe déjà dans la base de données!" }
        Toast.makeText(context, context.getString(R.string.compagnon_creation_valide), Toast.LENGTH_SHORT).show()
    }

    private fun setPremierLancement() {
        val sharedPreferences = context.getSharedPreferences(
            "AppPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean("isFirstLaunch", false)
        editor.apply()
    }

    /**
     * Fonction privée pour naviguer vers l'activité principale après création
     */
    private fun navigateToMainActivity() {
        val context = binding.root.context
        if (context is CreerCompagnonActivity) {
            val intent = Intent(context, MainActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            context.startActivity(intent)
            context.finish()
        }
    }
}
