package com.app.vakna.modele

import com.app.vakna.modele.dao.InventaireDAO

class Inventaire(
    private var pieces: Int,
    private var objets: MutableList<ObjetObtenu>,
    private var compagnon: GestionnaireDeCompagnons,
    private val inventaireDAO: InventaireDAO
) {

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
            compagnon.modifierFaim(0, niveau)
        } else if (objet.getType() == TypeObjet.NOURRITURE) {
            compagnon.modifierHumeur(0, niveau)
        }
        objet.updateQuantite(-1)

        // Update the quantity in the DAO
        inventaireDAO.mettreAJourQuantiteObjet(objet.getId(), objet.getQuantite())
    }

    fun utiliserObjet(nom: String, n: Int) {
        val objet = getObjetParNom(nom)
        assert(objet != null) { "On ne peut pas utiliser un objet que l'on ne possède pas" }
        assert(objet!!.getQuantite() >= n) { "On ne peut pas consommer plus d'objets que l'on en possède" }

        for (i in 0 until n) {
            utiliserObjet(objet)
        }

        // Update in DAO after use
        inventaireDAO.mettreAJourQuantiteObjet(objet.getId(), objet.getQuantite())
    }

    fun ajouterObjet(objet: ObjetObtenu, quantite: Int) {
        val existingObjet = getObjetParNom(objet.getNom())
        if (existingObjet == null) {
            // If the object doesn't exist, add it
            objets += objet
        } else {
            // If the object already exists, update its quantity
            existingObjet.updateQuantite(quantite)
        }

        // Ensure DAO reflects the correct quantity in the inventory
        inventaireDAO.mettreAJourQuantiteObjet(objet.getId(), getObjetParNom(objet.getNom())!!.getQuantite())
    }

    fun ajouterPieces(valeur: Int) {
        assert(pieces + valeur >= 0) { "Il est impossible d'avoir des pièces en négatif" }
        pieces += valeur

        // Update the number of pieces in the DAO
        inventaireDAO.mettreAJourPieces(pieces)
    }

    // Refresh the inventory data from DAO (e.g., when loading the app)
    fun chargerInventaire() {
        pieces = inventaireDAO.obtenirPieces()
        objets = inventaireDAO.obtenirTousObjetsObtenus().toMutableList()
    }
}
