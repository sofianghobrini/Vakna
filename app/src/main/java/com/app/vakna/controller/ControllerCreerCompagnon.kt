package com.app.vakna.controller

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.app.vakna.CreerCompagnonActivity
import com.app.vakna.MainActivity
import com.app.vakna.databinding.ActivityCreerCompagnonBinding
import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.Objet
import com.app.vakna.modele.TypeObjet
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.ObjetDAO
import com.bumptech.glide.Glide

/**
 * Contrôleur pour gérer la création d'un nouveau compagnon
 * @param binding Le binding associé à l'activité de création du compagnon
 */
class ControllerCreerCompagnon(private val binding: ActivityCreerCompagnonBinding) {
    private val context: Context = binding.root.context
    private val shopDAO = ObjetDAO(context)
    init {
        val jouet1 = Objet(0, "Jouet 1", 15, 5, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet2 = Objet(1, "Jouet 2", 20, 6, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet3 = Objet(2, "Jouet 3", 25, 7, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet4 = Objet(3, "Jouet 4", 30, 8, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet5 = Objet(4, "Jouet 5", 35, 9, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet6 = Objet(5, "Jouet 6", 40, 10, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet7 = Objet(6, "Jouet 7", 45, 11, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet8 = Objet(7, "Jouet 8", 50, 12, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet9 = Objet(8, "Jouet 9", 55, 13, TypeObjet.JOUET, "jouet", "placeholder")
        val jouet10 = Objet(9, "Jouet 10", 60, 14, TypeObjet.JOUET, "jouet", "placeholder")

        val jouetsItems = listOf(jouet1, jouet2, jouet3, jouet4, jouet5, jouet6, jouet7, jouet8, jouet9, jouet10)

        val kebab = Objet(10, "Kebab", 8, 10, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val pizza = Objet(11, "Pizza", 12, 15, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val burger = Objet(12, "Burger", 10, 8, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val sandwich = Objet(13, "Sandwich", 7, 5, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val salade = Objet(14, "Salade", 6, 4, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val pasta = Objet(15, "Pasta", 11, 9, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val sushi = Objet(16, "Sushi", 14, 13, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val steak = Objet(17, "Steak", 20, 18, TypeObjet.NOURRITURE, "nourriture", "placeholder")
        val nourritureItems = listOf(kebab, pizza, burger, sandwich, salade, pasta, sushi, steak)

        val allItems = jouetsItems + nourritureItems

        allItems.forEach {
            shopDAO.inserer(it)
        }
        // Charger le GIF du dragon à l'aide de Glide
        Glide.with(binding.root)
            .asGif()
            .load(com.app.vakna.R.drawable.humeur_dragon_heureux)
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
            creerCompagnon()
            navigateToMainActivity()
        }

        // Configurer l'éditeur de texte pour détecter l'appui sur "Enter"
        binding.inputNomCompagnon.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                if (binding.boutonCreerCompagnon.isEnabled) {
                    creerCompagnon()
                    navigateToMainActivity()
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
