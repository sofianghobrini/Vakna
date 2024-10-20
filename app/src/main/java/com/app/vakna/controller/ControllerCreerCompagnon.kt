package com.app.vakna.controller

import android.content.Intent
import android.widget.Toast
import com.app.vakna.CreerCompagnonActivity
import com.app.vakna.MainActivity
import com.app.vakna.databinding.ActivityCreerCompagnonBinding
import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.dao.CompagnonDAO

class ControllerCreerCompagnon(private val binding: ActivityCreerCompagnonBinding) {

    init {
        // Charger le GIF du dragon
        com.bumptech.glide.Glide.with(binding.root)
            .asGif()
            .load(com.app.vakna.R.drawable.dragon)
            .into(binding.dragonGif)

        // Configurer le listener de clic pour le bouton de confirmation
        val boutonConfirmer = binding.boutonCreerCompagnon
        boutonConfirmer.setOnClickListener {
            creerCompagnon()
            val context = binding.root.context
            if (context is CreerCompagnonActivity) {
                // Naviguer vers l'activité principale après la création du compagnon
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("navigateTo", "Taches")
                context.startActivity(intent)
            }
        }
    }

    // Fonction pour créer un nouveau compagnon
    private fun creerCompagnon() {
        // Obtenir le contexte
        val context = binding.root.context

        // Récupérer le nom entré dans l'EditText
        val nomCompagnon = binding.inputNomCompagnon.text.toString().trim()

        // Vérifier si le nom est vide
        if (nomCompagnon.isEmpty()) {
            // Afficher un message Toast si aucun nom n'a été entré
            Toast.makeText(context, "Veuillez entrer un nom pour le compagnon", Toast.LENGTH_SHORT).show()
            return
        }

        // Créer un nouveau compagnon avec des valeurs par défaut (faim et humeur à 50, XP à 0)
        val nouveauCompagnon = Compagnon(id = 0, nom = nomCompagnon, faim = 50, humeur = 50, xp = 0, espece = "Dragon")

        // Créer une instance de CompagnonDAO pour gérer les opérations de base de données
        val compagnonDAO = CompagnonDAO(context)

        // Insérer le nouveau compagnon dans la base de données
        val insertionReussie = compagnonDAO.inserer(nouveauCompagnon)

        // Vérifier si l'insertion a réussi
        if (insertionReussie) {
            // Afficher un message de succès
            Toast.makeText(context, "Compagnon créé avec succès!", Toast.LENGTH_SHORT).show()
        } else {
            // Afficher un message d'erreur si le compagnon existe déjà ou si l'insertion échoue
            Toast.makeText(context, "Erreur: Un compagnon avec ce nom existe déjà", Toast.LENGTH_SHORT).show()
        }
    }
}
