package com.app.vakna.controller

import android.content.Intent
import android.widget.Toast
import com.app.vakna.vue.DetailsRefugeActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityDetailsRefugesBinding
import com.app.vakna.modele.gestionnaires.GestionnaireDeRefuges
import com.app.vakna.modele.gestionnaires.Inventaire
import com.app.vakna.modele.gestionnaires.MagasinRefuge
import com.app.vakna.modele.dao.InventaireDAO
import com.bumptech.glide.Glide

class ControllerDetailsRefuge (
    private val binding: ActivityDetailsRefugesBinding,
    private val intent: Intent
) {

    val context = binding.root.context
    val inventaireDAO = InventaireDAO(context)
    val inventaire= Inventaire(context)
    val magasinRefuge = MagasinRefuge(context)
    val gestionnaire = GestionnaireDeRefuges(context)

    init {
        afficherNombreDeCoins()

        val nomRefuge = intent.getStringExtra("NOM_REFUGE") ?: context.getString(R.string.objet_inconnu)

        val refuge = magasinRefuge.obtenirRefugeStore(nomRefuge)

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
            if (inventaire.obtenirPieces() < refuge.getPrix()) {
                Toast.makeText(context, "Vous n'avez pas assez de pièces pour acheter ce refuge!", Toast.LENGTH_SHORT).show()
            }
            magasinRefuge.acheterRefuge(refuge.getId())
            NavigationHandler.navigationActiviteVersFragment(context,"CompagnonFragment")
            fermerLaPage()
        }

        binding.boutonRetour.setOnClickListener {
            fermerLaPage()
        }
    }

    private fun afficherNombreDeCoins() {
        val nombreDeCoins = inventaireDAO.obtenirPieces()
        binding.texteNombreCoins.text = context.getString(R.string.nombre_de_coins, nombreDeCoins)
    }

    private fun fermerLaPage() {
        if (context is DetailsRefugeActivity) {
            context.finish()
        }
    }
}