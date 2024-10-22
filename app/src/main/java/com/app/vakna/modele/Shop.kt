package com.app.vakna.modele


class Shop (private val objets : List<Objet>){
    fun getObjet(nom : String) : Objet?{
        for (objets in objets) {
            if (objets.getNom() == nom) {
                objets.getNom()
                objets.getPrix()
                objets.getType()
                objets.getNiveau()
                objets.getDetails()
            }
        }
        return null // Si aucun objet n'a été trouvé
    }

    // Méthode pour acheter une certaine quantité d'un objet
/*    fun acheter(nom: String, quantite: Int, inventaire: Inventaire) {
        // Obtenir l'objet par son nom
        val objet = getObjet(nom)

        // Vérifier si l'objet existe et si le solde de l'inventaire est suffisant pour la quantité demandée
        if (objet != null) {
            val totalPrix = objet.getPrix() * quantite
            if (inventaire.getPieces() >= totalPrix) {
                // Ajouter l'objet à l'inventaire et mettre à jour le solde
                inventaire.ajouterObjet(
                    ObjetObtenu(
                        nom = objet.getNom(),
                        prix = objet.getPrix(),
                        niveau = objet.getNiveau(),
                        type = objet.getType(), // Conversion du type enum en chaîne
                        detail = objet.getDetails(),
                        quantite =
                    ),
                    quantite
                )
                inventaire.ajouterPieces(-totalPrix) // Déduire le prix total
                println("$quantite x $nom ont été ajoutés à votre inventaire.")
            } else {
                println("Solde insuffisant pour acheter $quantite x $nom.")
            }
        } else {
            println("Objet $nom non trouvé.")
        }
    }*/

    fun listerObjet():List<Objet>{
        return objets
    }
    fun listerObjet(type:TypeObjet):List<Objet>{
        return objets.filter { it.getType() == type }
    }
}