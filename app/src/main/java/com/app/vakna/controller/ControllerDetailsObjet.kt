package com.app.vakna.controller

import android.content.Intent
import com.app.vakna.DetailsObjetActivity
import com.app.vakna.MainActivity
import com.app.vakna.databinding.ActivityDetailsObjetBinding
import android.text.InputFilter
import android.text.Spanned

class ControllerDetailsObjet(
    private val binding: ActivityDetailsObjetBinding,
    private val intent: Intent
) {

    val context = binding.root.context

    init {
        val nomObjet = intent.getStringExtra("NOM_OBJET") ?: "Objet inconnu"
        val niveauObjet = intent.getStringExtra("NIVEAU_OBJET") ?: "Objet inconnu"
        val coutObjet = intent.getStringExtra("COUT_OBJET") ?: "Objet inconnu"

        binding.texteTitreDetails.text = "$nomObjet"

        binding.texteNiveau.text = "Niveau: $niveauObjet"

        binding.texteCout.text = "Coût: $coutObjet"

        val boutonDiminuer = binding.boutonDiminuer
        val boutonAugmenter = binding.boutonAugmenter
        val quantite = binding.inputQuantite

        boutonDiminuer.setOnClickListener {
            val qte = quantite.text.toString().toInt()
            if (qte > 1) {
                quantite.setText((qte - 1).toString())
            }
        }

        boutonAugmenter.setOnClickListener {
            val qte = quantite.text.toString().toInt()
            quantite.setText((qte + 1).toString())
        }

        val boutonAchat = binding.boutonAchat

        boutonAchat.setOnClickListener {
            if (context is DetailsObjetActivity) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("navigateTo", "Magasin")
                context.startActivity(intent)
            }
        }

        val editText = binding.inputQuantite

        editText.filters = arrayOf<InputFilter>(MinFilter(1))
    }

    inner class MinFilter(private val minValue: Int) : InputFilter {
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dStart: Int,
            dEnd: Int
        ): CharSequence? {
            try {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (input >= minValue) {
                    return null
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}