package com.app.vakna.modele

import android.content.Context
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.InventaireDAO

class Inventaire(private var contexte: Context) {
    private var inventaireDAO = InventaireDAO(contexte)
    private var objets = mutableListOf<ObjetObtenu>()
    private var pieces = 0
    private var gestionnaireCompagnons = GestionnaireDeCompagnons(CompagnonDAO(contexte))


     init {
         inventaireDAO.obtenirTousObjetsObtenus().forEach() { objets.add(it) }
         pieces = inventaireDAO.obtenirPieces()
     }

    fun getObjets(): List<ObjetObtenu> {
        return objets
    }

    fun getPieces(): Int {
        return pieces
    }

    fun getObjetParNom(nom: String): ObjetObtenu? {
        return objets.find { it.getNom() == nom }
    }

    fun getObjetById(id: Int): ObjetObtenu? {
        return objets.find { it.getId() == id }
    }

    private fun utiliserObjet(objet: ObjetObtenu) {
        assert(objet.getQuantite() > 0) { "La quantité de l'objet ne peut pas être négative ou nulle" }
        val niveau = objet.getNiveau()
        if (objet.getType() == TypeObjet.JOUET) {
            gestionnaireCompagnons.modifierFaim(0, niveau)
        } else if (objet.getType() == TypeObjet.NOURRITURE) {
            gestionnaireCompagnons.modifierHumeur(0, niveau)
        }
        objet.updateQuantite(-1)
    }

    fun utiliserObjet(nom: String, n: Int) {
        val objet = getObjetParNom(nom)
        assert(objet != null) { "On ne peut pas utiliser un objet que l'on ne possède pas" }
        assert(objet!!.getQuantite() >= n) { "On ne peut pas consommer plus d'objets que l'on en possède" }

        for (i in 0 until n) {
            utiliserObjet(objet)
        }
        inventaireDAO.mettreAJourQuantiteObjet(objet.getId(), objet.getQuantite())
    }

    fun ajouterObjet(objet: ObjetObtenu, quantite: Int) {
        if (!objets.any { it.getId() == objet.getId() }) {
            objets += objet
        }
        getObjetParNom(objet.getNom())?.updateQuantite(quantite)
        inventaireDAO.insererObjetObtenu(objet)
    }

    fun ajouterPieces(valeur: Int) {
        assert(pieces + valeur >= 0) { "Il est impossible d'avoir des pièces en négatif" }
        pieces += valeur
        inventaireDAO.mettreAJourPieces(pieces)
    }
}
