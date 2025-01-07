package com.app.vakna.controller

import android.content.Intent
import android.widget.Toast
import com.app.vakna.vue.DetailsCompagnonActivity
import com.app.vakna.vue.MainActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityDetailsCompagnonBinding
import com.app.vakna.modele.gestionnaires.GestionnaireDeCompagnons
import com.app.vakna.modele.gestionnaires.Inventaire
import com.app.vakna.modele.gestionnaires.ShopCompagnons
import com.app.vakna.modele.dao.InventaireDAO
import com.bumptech.glide.Glide

class ControllerDetailsCompagnon(
    private val binding: ActivityDetailsCompagnonBinding,
    private val intent: Intent
) {

    val context = binding.root.context
    val inventaireDAO = InventaireDAO(context)
    val inventaire = Inventaire(context)
    val shopCompagnon = ShopCompagnons(context)
    val gestionnaire = GestionnaireDeCompagnons(context)

    init {
        val especeCompagnon = intent.getStringExtra("ESPECE_COMPAGNON") ?: context.getString(R.string.objet_inconnu)
        val compagnon = shopCompagnon.getCompagnonParEspece(especeCompagnon)
        afficherNombreDeCoins()

        binding.texteTitreDetails.text = compagnon?.espece ?: context.getString(R.string.objet_inconnu)
        Glide.with(context)
            .load(compagnon?.apparenceDefaut())
            .into(binding.imageCompagnon)
        binding.texteCoutCompagnon.text = context.getString(R.string.cout_format, compagnon?.prix)

        binding.boutonAchat.setOnClickListener {
            val nomCompagnon = binding.inputNomCompagnon.text.toString().trim()
            if(nomCompagnon.isEmpty()) {
                binding.inputNomCompagnon.error = "Entrez un nom pour votre compagnon"
            } else {
                if (inventaire.getPieces() < compagnon!!.prix) {
                    Toast.makeText(context, "Vous n'avez pas assez de pièces pour acheter ce compagnon!", Toast.LENGTH_SHORT).show()
                }
                shopCompagnon.acheterCompagnon(compagnon.id, nomCompagnon)
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
