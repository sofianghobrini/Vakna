package com.app.vakna.controller

import android.content.Intent
import com.app.vakna.DetailsObjetActivity
import com.app.vakna.MainActivity
import com.app.vakna.databinding.ActivityDetailsObjetBinding
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import com.app.vakna.modele.Inventaire
import com.app.vakna.modele.Shop
import com.app.vakna.modele.dao.InventaireDAO

class ControllerDetailsObjet(
    private val binding: ActivityDetailsObjetBinding,
    private val intent: Intent
) {

    val context = binding.root.context
    val shop = Shop(context)
    val inventaireDAO = InventaireDAO(context)

    init {
        val nomObjet = intent.getStringExtra("NOM_OBJET") ?: "Objet inconnu"
        val objet = shop.getObjet(nomObjet)
        afficherNombreDeCoins()
        binding.texteTitreDetails.text = "${objet?.getNom()}"


        binding.texteNiveau.text = "Niveau: ${objet?.getNiveau()}"

        binding.texteCout.text = "Coût: ${objet?.getPrix()}"

        binding.texteDescription.text = "${objet?.getDetails()}"

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
            val nomObjet = intent.getStringExtra("NOM_OBJET") ?: "Objet inconnu"
            val textQuantite = quantite.text.toString()
            val nbrQuantite = textQuantite.toInt()
            achat(nomObjet, nbrQuantite)
            if (context is DetailsObjetActivity) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("navigateTo", "Magasin")
                context.startActivity(intent)
            }
        }

        val boutonRetour = binding.boutonRetour

        boutonRetour.setOnClickListener {
            if (context is DetailsObjetActivity) {
                context.finish()
            }
        }

        val editText = binding.inputQuantite

        editText.filters = arrayOf<InputFilter>(MinFilter(1))
    }
    private fun afficherNombreDeCoins() {
        val nombreDeCoins = inventaireDAO.obtenirPieces()
        val texteNombreCoins = binding.texteNombreCoins
        texteNombreCoins.text = "$nombreDeCoins"
    }
    private fun achat( nom:String,  quantite:Int){
        shop.acheter(nom, quantite)
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