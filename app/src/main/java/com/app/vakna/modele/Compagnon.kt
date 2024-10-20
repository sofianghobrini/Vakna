package com.app.vakna.modele

class Compagnon(
    var id: Int,                // Identifiant unique du compagnon
    var nom: String,            // Nom du compagnon
    var faim: Int = 50,         // Niveau de faim (valeur par défaut = 50)
    var humeur: Int = 50,       // Niveau d'humeur (valeur par défaut = 50)
    var xp: Int = 0,            // Expérience (XP) du compagnon (par défaut = 0)
    var espece: String          // Espèce du compagnon (par exemple, "Dragon")
) {

    // Méthode pour modifier le niveau de faim du compagnon
    fun modifierFaim(niveau: Int) {
        // Vérification que le niveau est compris entre -100 et 100
        assert(niveau in -100..100) { "Le niveau de faim doit être compris entre -100 et 100." }

        // Modification du niveau de faim et forçage de la valeur entre 0 et 100
        faim += niveau
        faim = faim.coerceIn(0, 100)
    }

    // Méthode pour modifier le niveau d'humeur du compagnon
    fun modifierHumeur(niveau: Int) {
        // Vérification que le niveau est compris entre -100 et 100
        assert(niveau in -100..100) { "Le niveau d'humeur doit être compris entre -100 et 100." }

        // Modification du niveau d'humeur et forçage de la valeur entre 0 et 100
        humeur += niveau
        humeur = humeur.coerceIn(0, 100)
    }

    // Méthode pour ajouter de l'expérience (XP) au compagnon
    fun gagnerXp(montant: Int) {
        xp += montant
    }

    // Méthode pour déterminer le niveau actuel du compagnon en fonction de son XP
    fun niveau(): Int {
        return xp / 100  // Le niveau est calculé en divisant l'XP par 100
    }

    // Redéfinition de la méthode toString pour afficher les informations du compagnon
    override fun toString(): String {
        return "$nom ($espece) : Faim = $faim, Humeur = $humeur, XP = $xp (niveau ${niveau()})"
    }
}
