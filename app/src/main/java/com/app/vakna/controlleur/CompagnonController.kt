package com.app.vakna.controlleur

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.vakna.R
import com.app.vakna.databinding.FragmentCompagnonBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CompagnonController(
    private val fragment: Fragment,
    private val binding: FragmentCompagnonBinding,
    private val context: Context
) {

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

    // Initialize le vue du compagnon
    fun initializeCompagnon() {
        // Charger le GIF en utilisant Glide
        com.bumptech.glide.Glide.with(fragment)
            .asGif()
            .load(R.drawable.dragon)
            .into(binding.dragonGif)

        // Ajouter les onglets "Jouets" et "Nourriture" au TabLayout
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Jouets"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Nourriture"))

        // Mettre en place le GridView avec les jouets par défaut
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

    private fun setupGridView(items: List<String>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, items)
        binding.gridViewItems.adapter = adapter
    }

    // Affiche une boîte de dialogue pour modifier le nom du compagnon
    fun showEditNameDialog() {
        val editText = EditText(context).apply {
            hint = "Nouveau nom"
            inputType = android.text.InputType.TYPE_CLASS_TEXT
            filters = arrayOf(android.text.InputFilter.LengthFilter(50))
        }

        MaterialAlertDialogBuilder(context)
            .setTitle("Modifier le nom de ton Companion")
            .setView(editText)
            .setPositiveButton("Confirmer") { dialog, _ ->
                val newName = editText.text.toString()
                if (newName.isNotEmpty()) {
                    binding.dragonName.text = newName
                    Toast.makeText(context, "Nom companion changé", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
