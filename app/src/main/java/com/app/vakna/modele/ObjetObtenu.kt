package com.app.vakna.modele

import android.content.Context
import com.app.vakna.adapters.GridConsommableData

class ObjetObtenu(
    id: Int,
    nom: Map<String, String>,
    prix: Int,
    niveau: Int,
    type: TypeObjet,
    detail: Map<String, String>,
    private var quantite: Int,
    imageUrl: String,
    private var contexte: Context
) : Objet(id, nom, prix, niveau, type, detail, imageUrl) {

    override fun toGridData(context: Context): GridConsommableData {
        return GridConsommableData(getImageUrl(), getNom(contexte), getNiveau(), getPrix(), getQuantite())
    }

    fun getQuantite(): Int {
        return quantite
    }

    fun updateQuantite(valeur: Int) {
        assert(quantite + valeur >= 0) { "La quantite d'un produit ne peut pas être négatif" }
        quantite += valeur
    }
}
