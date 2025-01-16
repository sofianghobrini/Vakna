package com.app.vakna.modele.dao.compagnonstore

import android.content.Context
import com.app.vakna.adapters.GridData

class CompagnonStore(
    var id: Int,           // Unique identifier for the companion in the store
    var espece: String,    // Species of the companion (e.g., "Dragon")
    var prix: Int,       // Price of the companion in the store
    private var enLigne: Boolean = false // True si l'image vient d'en ligne

) {
    fun toGridData(context: Context): GridData {
        return GridData(apparenceDefaut(context), espece, prix)
    }

    // Méthode pour obtenir le fichier ou est stocké l'apparence par défaut du compagnon
    fun apparenceDefaut(context: Context): String {
        // Si on ne spécifie pas d'image d'url, alors ca veut dire que l'image est dans les fichiers de l'appli
        return if (enLigne) {
            context.filesDir.absolutePath + "compagnons/" + enLigne
        } else {
            "file:///android_asset/compagnons/" + espece.lowercase() + "/" + espece.lowercase() + "_heureux_1.gif"
        }
    }

    // Overriding the toString method to display companion's information in the store
    override fun toString(): String {
        return "$espece - Price: $$prix"
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
