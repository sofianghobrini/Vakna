package com.app.vakna.controller

import android.content.Intent
import android.widget.Toast
import com.app.vakna.vue.DetailsCompagnonActivity
import com.app.vakna.R
import com.app.vakna.databinding.ActivityDetailsCompagnonBinding
import com.app.vakna.modele.gestionnaires.GestionnaireDeCompagnons
import com.app.vakna.modele.gestionnaires.Inventaire
import com.app.vakna.modele.gestionnaires.MagasinCompagnons
import com.app.vakna.modele.dao.InventaireDAO
import com.bumptech.glide.Glide

class ControllerDetailsCompagnon(
    private val binding: ActivityDetailsCompagnonBinding,
    private val intent: Intent
) {

    val context = binding.root.context
    val inventaireDAO = InventaireDAO(context)
    val inventaire = Inventaire(context)
    val shopCompagnon = MagasinCompagnons(context)
    val gestionnaire = GestionnaireDeCompagnons(context)

    init {
        afficherNombreDeCoins()
        val especeCompagnon = intent.getStringExtra("ESPECE_COMPAGNON") ?: context.getString(R.string.objet_inconnu)
        val compagnon = shopCompagnon.obtenirCompagnon(especeCompagnon)

        assert(compagnon != null) { " Ce compagnon n'existe pas! " }
        compagnon!!

        binding.texteTitreDetails.text = compagnon.espece

        Glide.with(context)
            .load(compagnon.apparenceDefaut())
            .into(binding.imageCompagnon)

        binding.texteCoutCompagnon.text = context.getString(R.string.cout_format, compagnon.prix)

        binding.boutonAchat.setOnClickListener {
            val nomCompagnon = binding.inputNomCompagnon.text.toString().trim()
            if(nomCompagnon.isEmpty()) {
                binding.inputNomCompagnon.error = "Entrez un nom pour votre compagnon"
            } else {
                if (inventaire.obtenirPieces() < compagnon!!.prix) {
                    Toast.makeText(context, "Vous n'avez pas assez de pièces pour acheter ce compagnon!", Toast.LENGTH_SHORT).show()
                }
                shopCompagnon.acheterCompagnon(compagnon.id, nomCompagnon)
                NavigationHandler.navigationActiviteVersFragment(context, "CompagnonFragment")
                fermerLaPage()
            }
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
        if (context is DetailsCompagnonActivity) {
            context.finish()
        }
    }
}
