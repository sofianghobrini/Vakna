package com.app.vakna.modele

import android.content.Context
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.compagnonstore.CompagnonStore
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.dao.objet.ObjetDAO
import com.app.vakna.modele.dao.refuge.Refuge
import com.app.vakna.modele.dao.refuge.RefugeDAO
import com.app.vakna.modele.dao.refugestore.RefugeStore
import com.app.vakna.modele.dao.refugestore.RefugeStoreDAO
import com.app.vakna.modele.gestionnaires.Inventaire
import com.app.vakna.modele.gestionnaires.MagasinCompagnons

class InitialisationJSON(context: Context) {

    private var magasinCompagnons = MagasinCompagnons(context)
    private val shopDAO = ObjetDAO(context)
    private val refugeStoreDAO = RefugeStoreDAO(context)
    private val refugeDAO = RefugeDAO(context)
    private val inventaire = Inventaire(context)

    init {
        initialiserCompagnons()
        initialiserObjets()
        initialiserRefuges()
        inventaire.ajouterPieces(300)
    }

    private fun initialiserCompagnons() {
        val dragon = CompagnonStore(1, "Dragon", 750)
        val lapin = CompagnonStore(2, "Lapin", 450)
        val chat = CompagnonStore(3, "Chat", 500)
        val licorne = CompagnonStore(4, "Licorne", 600)
        val serpent = CompagnonStore(5, "Serpent", 650)
        val ecureuil = CompagnonStore(6, "Ecureuil", 400)

        val compagnonsList = listOf(dragon, lapin, chat, licorne, serpent, ecureuil)

        compagnonsList.forEach {
            magasinCompagnons.ajouterCompagnon(it)
        }
    }

    private fun initialiserObjets() {
        val jouetsItems = initialiserJouets()

        val nourritureItems = initialiserNourriture()

        val allItems = jouetsItems + nourritureItems

        allItems.forEach {
            shopDAO.inserer(it)
        }
    }

    private fun initialiserJouets(): List<Objet> {
        val jouet1 = Objet(
            0,
            "Bateau",
            15,
            5,
            TypeObjet.JOUET,
            "Attention à ne pas imiter le Titanic ...",
            "file:///android_asset/jouets/jouet_bateau.png"
        )
        val jouet2 = Objet(
            1,
            "Dinosaure",
            20,
            6,
            TypeObjet.JOUET,
            "Grrr ! Je suis le roi méchants !",
            "file:///android_asset/jouets/jouet_dinosaure.png"
        )
        val jouet3 = Objet(
            2,
            "Ourson",
            25,
            7,
            TypeObjet.JOUET,
            "Câlins gratuits !",
            "file:///android_asset/jouets/jouet_ourson.png"
        )
        val jouet4 = Objet(
            3,
            "Robot",
            30,
            8,
            TypeObjet.JOUET,
            "Bip bip bop skibidi bop",
            "file:///android_asset/jouets/jouet_robot.png"
        )
        val jouet5 = Objet(
            4,
            "Voiture",
            35,
            9,
            TypeObjet.JOUET,
            "Vrouuuuum, vrouuuuum!",
            "file:///android_asset/jouets/jouet_voiture.png"
        )

        return listOf(jouet1, jouet2, jouet3, jouet4, jouet5)
    }

    private fun initialiserNourriture(): List<Objet> {
        val kebab = Objet(
            10,
            "Kebab",
            8,
            10,
            TypeObjet.NOURRITURE,
            "Le cholestérole est mon meilleur ami",
            "file:///android_asset/nourritures/nourriture_kebab.png"
        )
        val pizza = Objet(
            11,
            "Pizza",
            12,
            15,
            TypeObjet.NOURRITURE,
            "Un pizza dé la mama, prego.",
            "file:///android_asset/nourritures/nourriture_pizza.png"
        )
        val burger = Objet(
            12,
            "Burger",
            10,
            8,
            TypeObjet.NOURRITURE,
            "Can I get some burger ? With some peanut butter.",
            "file:///android_asset/nourritures/nourriture_plumberger.png"
        )
        val sandwich = Objet(
            13,
            "Sandwich",
            7,
            5,
            TypeObjet.NOURRITURE,
            "L'une des nourritures les plus saine de ce jeu.",
            "file:///android_asset/nourritures/nourriture_sandwich.png"
        )
        val salade = Objet(
            14,
            "Salade",
            6,
            4,
            TypeObjet.NOURRITURE,
            "Sérieusement vous voulez manger du vert ?",
            "file:///android_asset/nourritures/nourriture_salade.png"
        )
        val pasta = Objet(
            15,
            "Pasta",
            11,
            9,
            TypeObjet.NOURRITURE,
            "Mamamia c'est la pasta dé la pizza !",
            "file:///android_asset/nourritures/nourriture_pasta.png"
        )
        val sushi = Objet(
            16,
            "Sushi",
            14,
            13,
            TypeObjet.NOURRITURE,
            "Non on ne va faire de blague raciste ... quoique ...",
            "file:///android_asset/nourritures/nourriture_sushi.png"
        )
        val steak = Objet(
            17,
            "Steak",
            20,
            18,
            TypeObjet.NOURRITURE,
            "De la viande à l'état pure (c'est quand même mieux que la salade)",
            "file:///android_asset/nourritures/nourriture_steak.png"
        )
        return listOf(kebab, pizza, burger, sandwich, salade, pasta, sushi, steak)
    }

    private fun initialiserRefuges() {
        refugeStoreDAO.inserer(RefugeStore(1, "Cantine", 1.5f, 1.0f, 1.0f, 1.0f, 250))
        refugeDAO.inserer(Refuge(1, "Tresor", 1.0f, 1.0f, 1.0f, 1.5f, true))
    }
}