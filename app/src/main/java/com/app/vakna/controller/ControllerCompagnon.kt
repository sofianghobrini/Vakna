package com.app.vakna.controller

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.app.vakna.R
import com.app.vakna.databinding.FragmentCompagnonBinding
import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.dao.CompagnonDAO
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Contrôleur pour gérer les interactions avec le compagnon dans l'application.
 * @param binding Le binding associé au fragment compagnon pour accéder aux vues.
 */
class ControllerCompagnon(private val binding: FragmentCompagnonBinding) {

    private val context: Context = binding.root.context
    private val dao = CompagnonDAO(context)
    private var compagnon: Compagnon? = null

    // Liste des items "Jouets" disponibles
    private val jouetsItems = listOf(
        "Jouet 1", "Jouet 2", "Jouet 3", "Jouet 4",
        "Jouet 5", "Jouet 6", "Jouet 7", "Jouet 8",
        "Jouet 9", "Jouet 10"
    )

    // Liste des items "Nourriture" disponibles
    private val nourritureItems = listOf(
        "Kebab", "Pizza", "Burger", "Sandwich",
        "Salade", "Kebab", "Kebab", "Pasta",
        "Sushi", "Steak"
    )

    /**
     * Initialise l'interface utilisateur pour afficher les informations du compagnon.
     * Charge les données depuis la base et configure les éléments graphiques.
     */
    fun initializeCompagnon() {
        // Charger le compagnon depuis la base de données
        val compagnons = dao.obtenirTous()
        compagnon = if (compagnons.isNotEmpty()) compagnons[0] else null

        // Mettre à jour l'affichage du nom et du niveau du compagnon
        compagnon?.let {
            binding.dragonName.text = it.nom
            updateLevelAndProgress(it) // Mise à jour du niveau et progression XP
        }

        // Charger et afficher un GIF via Glide
        Glide.with(context)
            .asGif()
            .load(R.drawable.dragon)
            .into(binding.dragonGif)

        // Ajouter les onglets "Jouets" et "Nourriture" dans le TabLayout
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Jouets"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Nourriture"))

        // Configurer le GridView par défaut avec les items "Jouets"
        setupGridView(jouetsItems)

        // Gérer la sélection d'onglets (Jouets / Nourriture)
        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                when (tab?.text) {
                    "Jouets" -> setupGridView(jouetsItems)
                    "Nourriture" -> setupGridView(nourritureItems)
                }
            }
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
    }

    /**
     * Configure le GridView pour afficher la liste des items (jouets ou nourriture).
     * @param items La liste d'items à afficher dans le GridView.
     */
    private fun setupGridView(items: List<String>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, items)
        binding.gridViewItems.adapter = adapter
    }

    /**
     * Affiche une boîte de dialogue permettant de modifier le nom du compagnon.
     * Le nouveau nom est sauvegardé dans la base de données et mis à jour dans l'interface utilisateur.
     */
    fun showEditNameDialog() {
        val editText = EditText(context).apply {
            hint = "Nouveau nom"
            inputType = android.text.InputType.TYPE_CLASS_TEXT
            filters = arrayOf(android.text.InputFilter.LengthFilter(50))
            compagnon?.let {
                setText(it.nom)  // Pré-remplir avec le nom actuel du compagnon
            }
        }

        MaterialAlertDialogBuilder(context)
            .setTitle("Modifier le nom de ton Companion")
            .setView(editText)
            .setPositiveButton("Confirmer") { dialog, _ ->
                val newName = editText.text.toString()
                if (newName.isNotEmpty()) {
                    // Mettre à jour le nom du compagnon dans la base de données
                    compagnon?.let {
                        it.nom = newName
                        dao.modifier(it.id, it)  // Sauvegarder les modifications
                        binding.dragonName.text = newName  // Mettre à jour l'affichage
                        Toast.makeText(context, "Nom du compagnon changé", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * Met à jour le niveau du compagnon et l'affichage de la progression en fonction de l'XP.
     * @param compagnon Le compagnon pour lequel le niveau et la progression doivent être mis à jour.
     */
    private fun updateLevelAndProgress(compagnon: Compagnon) {
        val currentXp = compagnon.xp
        val level = currentXp / 100  // Calculer le niveau à partir de l'XP
        val xpForCurrentLevel = currentXp % 100  // XP restant pour compléter le niveau actuel

        // Mettre à jour le texte du niveau
        binding.dragonLevel.text = "Niv. $level"

        // Mettre à jour la barre de progression avec l'XP restant pour le niveau
        binding.progressBarLevel.progress = xpForCurrentLevel
    }
}
