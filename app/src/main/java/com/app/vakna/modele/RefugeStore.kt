package com.app.vakna.modele

import com.app.vakna.adapters.GridData

class RefugeStore(
    private var id: Int,                // Identifiant unique de l'environnement
    private var nom: String,            // Nom de l'environnement
    private var modifFaim : Float = 1f,         // Modificateur de faim
    private var modifHumeur: Float = 1f,       // Modificateur d'humeur
    private var modifXp: Float = 1f,            // Modificateur d'xp
    private var modifPieces: Float = 1f,       // Modificateur de pieces
    private var prix: Int
) {

    open fun toGridData(): GridData {
        return GridData(apparence(), nom, prix)
    }
    fun getId(): Int {
        return id
    }
    fun getNom(): String {
        return nom
    }
    fun getModifFaim(): Float {
        return modifFaim
    }
    fun getModifHumeur(): Float {
        return modifHumeur
    }
    fun getModifXp(): Float {
        return modifXp
    }
    fun getModifPieces(): Float {
        return modifPieces
    }

    fun getPrix(): Int {
        return prix
    }

    fun apparence(): String {
        return "file:///android_asset/refuges/refuge_" + nom.lowercase() + ".png"
    }

    override fun toString(): String {
        return "Refuge " + getNom() + "(" + getId() + ") : faim : " + getModifFaim() +
                ", humeur : " + getModifHumeur() + ", xp : " + getModifHumeur() + ", pieces : " + getModifPieces()
    }

    // Méthode equals pour comparer 2 refuges
    override fun equals(other: Any?): Boolean {
        if (other !is RefugeStore) return false
        return id == other.id && nom == other.getNom()
    }

    // Méthode hashCode pour calculer le hashcode en utilisant l'ID du refuge
    override fun hashCode(): Int {
        return id.hashCode()
    }
}