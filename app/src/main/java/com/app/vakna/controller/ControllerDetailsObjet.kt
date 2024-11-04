package com.app.vakna.controller

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.Toast
import com.app.vakna.DetailsObjetActivity
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityDetailsObjetBinding
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
        val nomObjet = intent.getStringExtra("NOM_OBJET") ?: context.getString(R.string.objet_inconnu)
        val objet = shop.getObjet(nomObjet)
        afficherNombreDeCoins()

        binding.texteTitreDetails.text = objet?.getNom() ?: context.getString(R.string.objet_inconnu)
        binding.texteNiveau.text = context.getString(R.string.niveau_format, objet?.getNiveau())
        binding.texteCout.text = context.getString(R.string.cout_format, objet?.getPrix())
        binding.texteDescription.text = objet?.getDetails() ?: context.getString(R.string.description_non_disponible)

        val boutonDiminuer = binding.boutonDiminuer
        val boutonAugmenter = binding.boutonAugmenter
        val quantite = binding.inputQuantite

        quantite.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
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

        binding.boutonAchat.setOnClickListener {
            val textQuantite = quantite.text.toString()
            val nbrQuantite = textQuantite.toInt()
            achat(nomObjet, nbrQuantite)
            if (context is DetailsObjetActivity) {
                val mainIntent = Intent(context, MainActivity::class.java)
                mainIntent.putExtra("navigateTo", "Magasin")
                context.startActivity(mainIntent)
            }
        }

        binding.boutonRetour.setOnClickListener {
            if (context is DetailsObjetActivity) {
                context.finish()
            }
        }

        val maxValue = inventaireDAO.obtenirPieces() / (objet?.getPrix() ?: 1)
        binding.inputQuantite.filters = arrayOf<InputFilter>(MinMaxFilter(1, maxValue))
    }

    private fun afficherNombreDeCoins() {
        val nombreDeCoins = inventaireDAO.obtenirPieces()
        binding.texteNombreCoins.text = context.getString(R.string.nombre_de_coins, nombreDeCoins)
    }

    private fun achat(nom: String, quantite: Int) {
        shop.acheter(nom, quantite)
    }

    private fun prixTotal() {
        val quantite = binding.inputQuantite.text.toString().toInt()
        val name = intent.getStringExtra("NOM_OBJET") ?: context.getString(R.string.objet_inconnu)
        val objet = shop.getObjet(name)
        val prixTotal = (objet?.getPrix() ?: 0) * quantite
        binding.texteCout.text = context.getString(R.string.cout_total_format, prixTotal)
    }

    private fun reduirePrix() {
        val quantite = binding.inputQuantite.text.toString().toInt()
        val name = intent.getStringExtra("NOM_OBJET") ?: context.getString(R.string.objet_inconnu)
        val objet = shop.getObjet(name)
        val prixTotal = (objet?.getPrix() ?: 0) * quantite
        binding.texteCout.text = context.getString(R.string.cout_total_format, prixTotal)
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
                val newInput = (dest.toString().substring(0, dStart) +
                        source.toString() +
                        dest.toString().substring(dEnd)).toInt()

                return when {
                    newInput < minValue -> minValue.toString()
                    newInput > maxValue -> maxValue.toString()
                    else -> null
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}
