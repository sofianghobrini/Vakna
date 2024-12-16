package com.app.vakna.modele

import kotlin.random.Random

class Compagnon(
    var id: Int,                // Identifiant unique du compagnon
    var nom: String,            // Nom du compagnon
    var faim: Int = 50,         // Niveau de faim (valeur par défaut = 50)
    var humeur: Int = 50,       // Niveau d'humeur (valeur par défaut = 50)
    var xp: Int = 0,            // Expérience (XP) du compagnon (par défaut = 0)
    var espece: String,         // Espèce du compagnon (par exemple, "Dragon")
    var personnalite: Personnalite, //La personnalite du Compagon (sa personnalité est aléatoire)
    var actif: Boolean          // Indicateur si le compagnon est sélectionné
) {

    // Méthode pour déterminer le niveau actuel du compagnon en fonction de son XP
    fun niveau(): Int {
        return xp / 100  // Le niveau est calculé en divisant l'XP par 100
    }

    // Méthode pour obtenir le fichier ou est stocké l'apparence par défaut du compagnon
    fun apparenceDefaut(): String {
        var image = "file:///android_asset/compagnons/"
        image += espece.lowercase() + "/" + espece.lowercase() + "_heureux.gif"
        return image
    }

    // Méthode pour obtenir le fichier ou est stocké l'apparence actuelle du compagnon
    fun apparence(): String {
        var humeurImage = "file:///android_asset/compagnons/"

        humeurImage += espece.lowercase() + "/" + espece.lowercase() + "_"

        var humeurComp = 0
        humeurComp = if (humeur < faim) {
            humeur
        } else {
            faim
        }

        humeurImage += if (humeurComp > 60) {
            "heureux"
        } else if (humeurComp > 30) {
            "moyen"
        } else if (humeurComp > 0) {
            "enerve"
        } else {
            "triste"
        }
        humeurImage +=".gif"
        return humeurImage
    }

    fun personnalite_compagnon () : Personnalite{
        val nbrAlea = Random.nextInt(1, 6)
        return when(nbrAlea){
            1->Personnalite.CALME
            2->Personnalite.GRINCHEUX
            3->Personnalite.GOURMAND
            4->Personnalite.CUPIDE
            5->Personnalite.JOUEUR
            else->Personnalite.AVARD
        }
    }

    // Redéfinition de la méthode toString pour afficher les informations du compagnon
    override fun toString(): String {
        return "$nom ($espece) : Faim = $faim, Humeur = $humeur, XP = $xp (niveau ${niveau()}), $personnalite"
    }

    // Méthode equals pour comparer 2 compagnons, renvoie faux s'ils sont différents par ID
    override fun equals(other: Any?): Boolean {
        if (other !is Compagnon) return false
        return id == other.id
    }

    // Méthode hashCode pour calculer le hashcode en utilisant l'ID du compagnon
    override fun hashCode(): Int {
        return id.hashCode()
    }

}

