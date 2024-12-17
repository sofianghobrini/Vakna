package com.app.vakna.modele

import com.app.vakna.adapters.GridData

class CompagnonStore(
    var id: Int,           // Unique identifier for the companion in the store
    var nom: String,       // Name of the companion
    var espece: String,    // Species of the companion (e.g., "Dragon")
    var prix: Int,       // Price of the companion in the store

) {

    fun toGridData(): GridData {
        return GridData(apparenceDefaut(), espece, prix)
    }

    // Méthode pour obtenir le fichier ou est stocké l'apparence par défaut du compagnon
    fun apparenceDefaut(): String {
        var image = "file:///android_asset/compagnons/"
        image += espece.lowercase() + "/" + espece.lowercase() + "_heureux_1.gif"
        return image
    }

    // Overriding the toString method to display companion's information in the store
    override fun toString(): String {
        return "$nom ($espece) - Price: $$prix"
    }

    // Method equals to compare two companions, returns false if they have different IDs
    override fun equals(other: Any?): Boolean {
        if (other !is CompagnonStore) return false
        return id == other.id
    }

    // Method hashCode to calculate hashcode using the companion's ID
    override fun hashCode(): Int {
        return id.hashCode()
    }
}
