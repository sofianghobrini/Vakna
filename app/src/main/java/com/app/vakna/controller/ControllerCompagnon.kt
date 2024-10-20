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

class ControllerCompagnon(
    private val binding: FragmentCompagnonBinding
) {
    private val context: Context = binding.root.context
    private val dao = CompagnonDAO(context)
    private var compagnon: Compagnon? = null

    // Items pour "Jouets" et "Nourriture"
    private val jouetsItems = listOf(
        "Jouet 1", "Jouet 2", "Jouet 3", "Jouet 4",
        "Jouet 5", "Jouet 6", "Jouet 7", "Jouet 8",
        "Jouet 9", "Jouet 10"
    )

    private val nourritureItems = listOf(
        "Kebab", "Pizza", "Burger", "Sandwich",
        "Salade", "Kebab", "Kebab", "Pasta",
        "Sushi", "Steak"
    )

    // Initialiser la vue pour le compagnon
    fun initializeCompagnon() {
        // Charger le compagnon depuis la base de données
        val compagnons = dao.obtenirTous()
        compagnon = if (compagnons.isNotEmpty()) compagnons[0] else null

        // Afficher le nom du compagnon si disponible
        compagnon?.let {
            binding.dragonName.text = it.nom
            updateLevelAndProgress(it) // Mettre à jour le niveau et la progression en fonction de l'XP
        }

        // Charger le GIF en utilisant Glide
        Glide.with(context)
            .asGif()
            .load(R.drawable.dragon)
            .into(binding.dragonGif)

        // Ajouter les onglets "Jouets" et "Nourriture" dans le TabLayout
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Jouets"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Nourriture"))

        // Configurer le GridView par défaut avec les jouets
        setupGridView(jouetsItems)

        // Gérer la sélection d'onglets entre "Jouets" et "Nourriture"
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

    // Configurer le GridView pour afficher les items
    private fun setupGridView(items: List<String>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, items)
        binding.gridViewItems.adapter = adapter
    }

    // Afficher une boîte de dialogue pour modifier le nom du compagnon
    fun showEditNameDialog() {
        val editText = EditText(context).apply {
            hint = "Nouveau nom"
            inputType = android.text.InputType.TYPE_CLASS_TEXT
            filters = arrayOf(android.text.InputFilter.LengthFilter(50))
            compagnon?.let {
                setText(it.nom)  // Pré-remplir avec le nom actuel
            }
        }

        MaterialAlertDialogBuilder(context)
            .setTitle("Modifier le nom de ton Companion")
            .setView(editText)
            .setPositiveButton("Confirmer") { dialog, _ ->
                val newName = editText.text.toString()
                if (newName.isNotEmpty()) {
                    // Mettre à jour le nom du compagnon et sauvegarder dans la base de données
                    compagnon?.let {
                        it.nom = newName
                        dao.modifier(it.id, it)  // Sauvegarder le nom mis à jour
                        binding.dragonName.text = newName  // Mettre à jour l'UI
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

    // Fonction pour mettre à jour le niveau et la progression de l'XP dans l'UI
    private fun updateLevelAndProgress(compagnon: Compagnon) {
        val currentXp = compagnon.xp
        val level = currentXp / 100  // Calculer le niveau en tant que XP / 100
        val xpForCurrentLevel = currentXp % 100  // XP restant pour le niveau actuel

        // Mettre à jour le texte du niveau
        binding.dragonLevel.text = "Niv. $level"

        // Mettre à jour la barre de progression pour le niveau actuel (XP sur 100)
        binding.progressBarLevel.progress = xpForCurrentLevel
    }
}
