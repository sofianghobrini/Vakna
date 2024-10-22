package com.app.vakna.controller

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.app.vakna.CreerCompagnonActivity
import com.app.vakna.MainActivity
import com.app.vakna.databinding.ActivityCreerCompagnonBinding
import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.dao.CompagnonDAO
import com.bumptech.glide.Glide

/**
 * Contrôleur pour gérer la création d'un nouveau compagnon
 * @param binding Le binding associé à l'activité de création du compagnon
 */
class ControllerCreerCompagnon(private val binding: ActivityCreerCompagnonBinding) {

    init {
        // Charger le GIF du dragon à l'aide de Glide
        Glide.with(binding.root)
            .asGif()
            .load(com.app.vakna.R.drawable.dragon)
            .into(binding.dragonGif)

        // Désactiver le bouton de confirmation au début
        binding.boutonCreerCompagnon.isEnabled = false

        // Configurer un TextWatcher pour écouter les changements de texte
        binding.inputNomCompagnon.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Activer ou désactiver le bouton selon le contenu du champ
                binding.boutonCreerCompagnon.isEnabled = !s.isNullOrEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Ne rien faire avant la modification du texte
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Ne rien faire pendant la modification du texte
            }
        })

        // Configurer le bouton de confirmation de création du compagnon
        binding.boutonCreerCompagnon.setOnClickListener {
            creerCompagnon() // Appeler la méthode pour créer le compagnon
            navigateToMainActivity() // Naviguer vers l'activité principale
        }

        // Configurer l'éditeur de texte pour détecter l'appui sur "Enter"
        binding.inputNomCompagnon.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                if (binding.boutonCreerCompagnon.isEnabled) {
                    creerCompagnon() // Appeler la méthode pour créer le compagnon
                    navigateToMainActivity() // Naviguer vers l'activité principale
                }
                true
            } else {
                false
            }
        }
    }

    /**
     * Fonction privée pour créer un nouveau compagnon
     */
    private fun creerCompagnon() {
        val context = binding.root.context
        val nomCompagnon = binding.inputNomCompagnon.text.toString().trim()

        // Créer une nouvelle instance de compagnon avec des valeurs par défaut
        val nouveauCompagnon = Compagnon(
            id = 1,
            nom = nomCompagnon,
            faim = 50,
            humeur = 50,
            xp = 0,
            espece = "Dragon"
        )

        // Instancier le DAO pour manipuler les données du compagnon dans la base
        val compagnonDAO = CompagnonDAO(context)

        // Insérer le compagnon dans la base et vérifier le succès
        val insertionReussie = compagnonDAO.inserer(nouveauCompagnon)

        // Afficher un message en fonction du résultat de l'insertion
        if (insertionReussie) {
            Toast.makeText(context, "Compagnon créé avec succès!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Erreur: Un compagnon avec ce nom existe déjà", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Fonction privée pour naviguer vers l'activité principale après création
     */
    private fun navigateToMainActivity() {
        val context = binding.root.context
        if (context is CreerCompagnonActivity) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("navigateTo", "Taches")
            context.startActivity(intent)
        }
    }
}
