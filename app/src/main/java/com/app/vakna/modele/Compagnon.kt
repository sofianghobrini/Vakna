package com.app.vakna.modele


class Compagnon(
    var id: Int,                // Identifiant unique du compagnon
    var nom: String,            // Nom du compagnon
    var faim: Int = 50,         // Niveau de faim (valeur par défaut = 50)
    var humeur: Int = 50,       // Niveau d'humeur (valeur par défaut = 50)
    var xp: Int = 0,            // Expérience (XP) du compagnon (par défaut = 0)
    var espece: String,         // Espèce du compagnon (par exemple, "Dragon")
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

    // Redéfinition de la méthode toString pour afficher les informations du compagnon
    override fun toString(): String {
        return "$nom ($espece) : Faim = $faim, Humeur = $humeur, XP = $xp (niveau ${niveau()})"
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

