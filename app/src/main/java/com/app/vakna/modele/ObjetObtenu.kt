package com.app.vakna.modele

import com.app.vakna.adapters.GridData

class ObjetObtenu(
    id: Int,
    nom: String,
    prix: Int,
    niveau: Int,
    type: TypeObjet,
    detail: String,
    private var quantite: Int,
    imageUrl: String
) : Objet(id, nom, prix, niveau, type, detail, imageUrl) {

    override fun toGridData(): GridData {
        return GridData(0, getNom(), getNiveau(), getPrix(), getQuantite())
    }

    fun getQuantite(): Int {
        return quantite
    }

    fun updateQuantite(valeur: Int) {
        assert(quantite + valeur >= 0) { "La quantite d'un produit ne peut pas être négatif" }
        quantite += valeur
    }
}
