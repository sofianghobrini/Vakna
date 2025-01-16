package com.app.vakna.modele

import android.content.Context
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.compagnonstore.CompagnonStore
import com.app.vakna.modele.dao.compagnonstore.CompagnonStoreDAO
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.dao.objet.ObjetDAO
import com.app.vakna.modele.dao.refuge.Refuge
import com.app.vakna.modele.dao.refuge.RefugeDAO
import com.app.vakna.modele.dao.refugestore.RefugeStore
import com.app.vakna.modele.dao.refugestore.RefugeStoreDAO
import com.app.vakna.modele.gestionnaires.Inventaire

class InitialisationJSON(context: Context) {

    private val compagnonStoreDAO = CompagnonStoreDAO(context)
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
            compagnonStoreDAO.inserer(it)
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
        // Jouets
        val jouet1 = Objet(
            0,
            mapOf("fr" to "Bateau", "en" to "Boat"),
            15,
            5,
            TypeObjet.JOUET,
            mapOf("fr" to "Attention à ne pas imiter le Titanic ...", "en" to "Be careful not to imitate the Titanic ..."),
            "file:///android_asset/jouets/jouet_bateau.png"
        )

        val jouet2 = Objet(
            1,
            mapOf("fr" to "Dinosaure", "en" to "Dinosaur"),
            20,
            6,
            TypeObjet.JOUET,
            mapOf("fr" to "Grrr ! Je suis le roi méchant !", "en" to "Grrr! I am the evil king!"),
            "file:///android_asset/jouets/jouet_dinosaure.png"
        )

        val jouet3 = Objet(
            2,
            mapOf("fr" to "Ourson", "en" to "Teddy Bear"),
            25,
            7,
            TypeObjet.JOUET,
            mapOf("fr" to "Câlins gratuits !", "en" to "Free hugs!"),
            "file:///android_asset/jouets/jouet_ourson.png"
        )

        val jouet4 = Objet(
            3,
            mapOf("fr" to "Robot", "en" to "Robot"),
            30,
            8,
            TypeObjet.JOUET,
            mapOf("fr" to "Bip bip bop skibidi bop", "en" to "Beep beep bop skibidi bop"),
            "file:///android_asset/jouets/jouet_robot.png"
        )

        val jouet5 = Objet(
            4,
            mapOf("fr" to "Voiture", "en" to "Car"),
            35,
            9,
            TypeObjet.JOUET,
            mapOf("fr" to "Vrouuuuum, vrouuuuum!", "en" to "Vroom, vroom!"),
            "file:///android_asset/jouets/jouet_voiture.png"
        )

        return listOf(jouet1, jouet2, jouet3, jouet4, jouet5)
    }

    private fun initialiserNourriture(): List<Objet> {

        val kebab = Objet(
            10,
            mapOf("fr" to "Kebab", "en" to "Kebab"),
            8,
            10,
            TypeObjet.NOURRITURE,
            mapOf("fr" to "Le cholestérol est mon meilleur ami", "en" to "Cholesterol is my best friend"),
            "file:///android_asset/nourritures/nourriture_kebab.png"
        )

        val pizza = Objet(
            11,
            mapOf("fr" to "Pizza", "en" to "Pizza"),
            12,
            15,
            TypeObjet.NOURRITURE,
            mapOf("fr" to "Une pizza de la mama, prego.", "en" to "A pizza from mama, prego."),
            "file:///android_asset/nourritures/nourriture_pizza.png"
        )

        val burger = Objet(
            12,
            mapOf("fr" to "Burger", "en" to "Burger"),
            10,
            8,
            TypeObjet.NOURRITURE,
            mapOf("fr" to "Puis-je avoir un burger ? Avec un peu de beurre de cacahuète.", "en" to "Can I get a burger? With some peanut butter."),
            "file:///android_asset/nourritures/nourriture_plumberger.png"
        )

        val sandwich = Objet(
            13,
            mapOf("fr" to "Sandwich", "en" to "Sandwich"),
            7,
            5,
            TypeObjet.NOURRITURE,
            mapOf("fr" to "L'une des nourritures les plus saines de ce jeu.", "en" to "One of the healthiest foods in this game."),
            "file:///android_asset/nourritures/nourriture_sandwich.png"
        )

        val salade = Objet(
            14,
            mapOf("fr" to "Salade", "en" to "Salad"),
            6,
            4,
            TypeObjet.NOURRITURE,
            mapOf("fr" to "Sérieusement, vous voulez manger du vert ?", "en" to "Seriously, you want to eat greens?"),
            "file:///android_asset/nourritures/nourriture_salade.png"
        )

        val pasta = Objet(
            15,
            mapOf("fr" to "Pâtes", "en" to "Pasta"),
            11,
            9,
            TypeObjet.NOURRITURE,
            mapOf("fr" to "Mamamia, c'est la pasta de la pizza!", "en" to "Mamamia, it's the pasta from the pizza!"),
            "file:///android_asset/nourritures/nourriture_pasta.png"
        )

        val sushi = Objet(
            16,
            mapOf("fr" to "Sushi", "en" to "Sushi"),
            14,
            13,
            TypeObjet.NOURRITURE,
            mapOf("fr" to "Non, on ne va pas faire de blague raciste ... quoique ...", "en" to "No, we won't make a racist joke ... or will we ..."),
            "file:///android_asset/nourritures/nourriture_sushi.png"
        )

        val steak = Objet(
            17,
            mapOf("fr" to "Steak", "en" to "Steak"),
            20,
            18,
            TypeObjet.NOURRITURE,
            mapOf("fr" to "De la viande à l'état pur (c'est quand même mieux que la salade)", "en" to "Pure meat (it's still better than salad)"),
            "file:///android_asset/nourritures/nourriture_steak.png"
        )
        return listOf(kebab, pizza, burger, sandwich, salade, pasta, sushi, steak)
    }

    private fun initialiserRefuges() {
        refugeStoreDAO.inserer(RefugeStore(1, "Cantine", 1.5f, 1.0f, 1.0f, 1.0f, 250))
        refugeDAO.inserer(Refuge(1, "Tresor", 1.0f, 1.0f, 1.0f, 1.5f, true))
    }
}