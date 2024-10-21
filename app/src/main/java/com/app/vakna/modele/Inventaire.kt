package com.app.vakna.modele

class Inventaire(private var pieces: Int,
                 private var objets : List<ObjetObtenu>,
                 private var compagnon: GestionnaireDeCompagnons) {

    fun getObjets(): List<ObjetObtenu> {
        return objets
    }

    fun getPieces(): Int {
        return pieces
    }

    fun getObjet(nom: String): ObjetObtenu? {
        for (objet: ObjetObtenu in objets) {
            if (objet.getNom() == nom) {
                return objet
            }
        }
        return null
    }

    private fun utiliserObjet(objet: ObjetObtenu) {
        assert(objet.getQuantite() < 1) {"La quantite de l'objet ne peut pas etre négatif ou nul"}
        val niveau = objet.getNiveau()
        if (objet.getType() == "Nourriture") {
            compagnon.modifierFaim(0, niveau)
        } else if (objet.getType() == "Jouet") {
            compagnon.modifierHumeur(0, niveau)
        }
        objet.updateQuantite(-1)

    }

    fun utiliserObjet(nom: String, n: Int) {
        var objet = getObjet(nom)
        assert(objet == null){"On ne peut pas utiliser un objet que l'on ne possède pas"}
        if (objet != null) {
            assert(objet.getQuantite()-n < 1) {"On ne peux pas consommer plus d'objet que l'on en possède"}

            for (i in 0..n) {
                utiliserObjet(objet)
            }
        }
    }

    fun ajouterObjet(objet: ObjetObtenu, quantite: Int) {
        if (!(objet in objets)) {
            objets += objet
        }
        getObjet(objet.getNom())?.updateQuantite(quantite)
    }

    fun ajouterPieces(valeur: Int) {
        assert(pieces+valeur < 0){"Il est impossible d'avoir des pieces en negatif"}
        pieces += valeur
    }
}
