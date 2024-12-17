package com.app.vakna.controller

import android.content.Intent
import com.app.vakna.DetailsCompagnonActivity
import com.app.vakna.DetailsRefugeActivity
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityDetailsRefugesBinding
import com.app.vakna.modele.GestionnaireDeRefuge
import com.app.vakna.modele.ShopRefuge
import com.app.vakna.modele.dao.InventaireDAO
import com.bumptech.glide.Glide

class ControllerDetailsRefuge (
    private val binding: ActivityDetailsRefugesBinding,
    private val intent: Intent
) {

    val context = binding.root.context
    val inventaireDAO = InventaireDAO(context)
    val shopRefuge = ShopRefuge(context)
    val gestionnaire = GestionnaireDeRefuge(context)

    init {
        val nomRefuge = intent.getStringExtra("NOM_REFUGE") ?: context.getString(R.string.objet_inconnu)
        val refuge = shopRefuge.getRefugeStoreParNom(nomRefuge)
        afficherNombreDeCoins()

        binding.texteTitreDetails.text = refuge!!.getNom()
        Glide.with(context)
            .load(refuge.apparence())
            .into(binding.imageRefuge)
        binding.texteModifFaim.text = "Modif Faim:" + refuge.getModifFaim().toString()
        binding.texteModifHumeur.text = "Modif Humeur:" + refuge.getModifHumeur().toString()
        binding.texteModifXP.text = "Modif XP:" + refuge.getModifXp().toString()
        binding.texteModifPieces.text = "Modif Pièces:" + refuge.getModifPieces().toString()
        binding.texteCout.text = context.getString(R.string.cout_format, refuge.getPrix())

        binding.boutonAchat.setOnClickListener {
            shopRefuge.acheterRefuge(refuge.getId())
            if (context is DetailsRefugeActivity) {
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

        binding.boutonRetour.setOnClickListener {
            if (context is DetailsRefugeActivity) {
                context.finish()
            }
        }
    }

    private fun afficherNombreDeCoins() {
        val nombreDeCoins = inventaireDAO.obtenirPieces()
        binding.texteNombreCoins.text = context.getString(R.string.nombre_de_coins, nombreDeCoins)
    }
}