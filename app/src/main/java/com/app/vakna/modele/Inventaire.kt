package com.app.vakna.modele

class Inventaire(
    private var pieces: Int,
    private var objets: List<ObjetObtenu>,
    private var compagnon: GestionnaireDeCompagnons
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
        if (objet.getType() == "Nourriture") {
            compagnon.modifierFaim(0, niveau)
        } else if (objet.getType() == "Jouet") {
            compagnon.modifierHumeur(0, niveau)
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
    }

    fun ajouterObjet(objet: ObjetObtenu, quantite: Int) {
        if (!objets.any { it.getId() == objet.getId() }) {
            objets += objet
        }
        getObjetParNom(objet.getNom())?.updateQuantite(quantite)
    }

    fun ajouterPieces(valeur: Int) {
        assert(pieces + valeur >= 0) { "Il est impossible d'avoir des pièces en négatif" }
        pieces += valeur
    }
}
