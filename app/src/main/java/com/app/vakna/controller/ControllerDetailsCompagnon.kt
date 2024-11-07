package com.app.vakna.controller

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.Toast
import com.app.vakna.DetailsCompagnonActivity
import com.app.vakna.DetailsObjetActivity
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityDetailsCompagnonBinding
import com.app.vakna.databinding.ActivityDetailsObjetBinding
import com.app.vakna.modele.GestionnaireDeCompagnons
import com.app.vakna.modele.Inventaire
import com.app.vakna.modele.Shop
import com.app.vakna.modele.ShopCompagnons
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.InventaireDAO

class ControllerDetailsCompagnon(
    private val binding: ActivityDetailsCompagnonBinding,
    private val intent: Intent
) {

    val context = binding.root.context
    val inventaireDAO = InventaireDAO(context)
    val shopCompagnon = ShopCompagnons(context)
    val gestionnaire = GestionnaireDeCompagnons(CompagnonDAO(context))

    init {
        val especeCompagnon = intent.getStringExtra("ESPECE_COMPAGNON") ?: context.getString(R.string.objet_inconnu)
        val compagnon = shopCompagnon.getCompagnonParEspece(especeCompagnon)
        afficherNombreDeCoins()

        binding.texteTitreDetails.text = compagnon?.espece ?: context.getString(R.string.objet_inconnu)
        binding.texteCoutCompagnon.text = context.getString(R.string.cout_format, compagnon?.prix)

        binding.boutonAchat.setOnClickListener {
            val nomCompagnon = binding.inputNomCompagnon.text.toString().trim()
            if(nomCompagnon.isEmpty()) {
                binding.inputNomCompagnon.error = "Entrez un nom pour votre compagnon"
            } else {
                shopCompagnon.acheterCompagnon(compagnon!!.id, nomCompagnon)
                if (context is DetailsCompagnonActivity) {
                    val sourceFragment = intent.getStringExtra("sourceFragment")
                    if (sourceFragment == "CompagnonFragment") {
                        val intent = Intent(context, MainActivity::class.java).apply {
                            intent.putExtra("navigateTo", "CompagnonFragment")
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        context.startActivity(intent)
                    }
                    context.finish()
                }
            }
        }

        binding.boutonRetour.setOnClickListener {
            if (context is DetailsCompagnonActivity) {
                context.finish()
            }
        }
    }

    private fun afficherNombreDeCoins() {
        val nombreDeCoins = inventaireDAO.obtenirPieces()
        binding.texteNombreCoins.text = context.getString(R.string.nombre_de_coins, nombreDeCoins)
    }
}
