package com.app.vakna.controller

import android.content.Intent
import android.text.Editable
import com.app.vakna.DetailsObjetActivity
import com.app.vakna.MainActivity
import com.app.vakna.databinding.ActivityDetailsObjetBinding
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
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

        quantite.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                null
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                null
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    prixTotal()
                    try {
                        val input = s.toString().toInt()
                        val maxValue = inventaireDAO.obtenirPieces() / (objet?.getPrix() ?: 1)

                        when {
                            input < 1 -> {
                                quantite.setText("1")
                                quantite.setSelection(quantite.text.length)
                            }
                            input > maxValue -> {
                                quantite.setText(maxValue.toString())
                                quantite.setSelection(quantite.text.length)
                            }
                            else -> {}
                        }
                    } catch (e: NumberFormatException) {
                        quantite.setText("1")
                        quantite.setSelection(quantite.text.length)
                    }
                }
            }
        })


        boutonDiminuer.setOnClickListener {
            val qte = quantite.text.toString().toInt()
            if (qte > 1) {
                quantite.setText((qte - 1).toString())
                reduirePrix()
            }
        }

        boutonAugmenter.setOnClickListener {
            val qte = quantite.text.toString().toInt()
            quantite.setText((qte + 1).toString())
            prixTotal()
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

        val maxValue = inventaireDAO.obtenirPieces() / objet?.getPrix()!!

        editText.filters = arrayOf<InputFilter>(MinMaxFilter(1,maxValue))
    }
    private fun afficherNombreDeCoins() {
        val nombreDeCoins = inventaireDAO.obtenirPieces()
        val texteNombreCoins = binding.texteNombreCoins
        texteNombreCoins.text = "$nombreDeCoins"
    }
    private fun achat( nom:String,  quantite:Int){
        shop.acheter(nom, quantite)
    }
    private fun prixTotal(){
        val quantite = binding.inputQuantite.text.toString().toInt()
        val name =  intent.getStringExtra("NOM_OBJET") ?: "Objet inconnu"
        val objet = shop.getObjet(name)

        // Calcul du prix total
        val prixTotal = objet?.getPrix()!! * quantite

        // Mettre à jour l'affichage du prix total
        binding.texteCout.text = "Coût total: $prixTotal"
    }
    private fun reduirePrix(){
        val quantite = binding.inputQuantite.text.toString().toInt()
        val name =  intent.getStringExtra("NOM_OBJET") ?: "Objet inconnu"
        val objet = shop.getObjet(name)

        // Calcul du prix total
        val prixTotal = objet?.getPrix()!! * quantite

        // Mettre à jour l'affichage du prix total
        binding.texteCout.text = "Coût total: $prixTotal"
    }

    inner class MinMaxFilter(private val minValue: Int, private val maxValue: Int) : InputFilter {
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dStart: Int,
            dEnd: Int
        ): CharSequence? {
            try {
                // Build the new input by combining existing text with new input
                val newInput = (dest.toString().substring(0, dStart) +
                        source.toString() +
                        dest.toString().substring(dEnd)).toInt()

                return when {
                    newInput < minValue -> minValue.toString()
                    newInput > maxValue -> maxValue.toString()
                    else -> null // Input is within bounds
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}