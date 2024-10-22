package com.app.vakna.modele

import com.app.vakna.modele.dao.InventaireDAO
import com.app.vakna.modele.dao.ObjetDAO

class Shop(
    private val objetDAO: ObjetDAO,
    private val inventaireDAO: InventaireDAO
) {

    // Méthode pour obtenir un objet par son nom
    fun getObjet(nom: String): Objet? {
        return objetDAO.obtenirTous().find { it.getNom() == nom }
    }

    // Méthode pour acheter une certaine quantité d'un objet
    fun acheter(nom: String, quantite: Int, inventaire: Inventaire) {
        // Obtenir l'objet par son nom
        val objet = getObjet(nom)

        // Vérifier si l'objet existe et si le solde de l'inventaire est suffisant pour la quantité demandée
        if (objet != null) {
            val id = objet.getId()
            val totalPrix = objet.getPrix() * quantite
            if (inventaire.getPieces() >= totalPrix) {
                // Ajouter l'objet à l'inventaire et mettre à jour le solde
                val objetObtenu = ObjetObtenu(
                    id = id,
                    nom = objet.getNom(),
                    prix = objet.getPrix(),
                    niveau = objet.getNiveau(),
                    type = objet.getType(),
                    detail = objet.getDetails(),
                    quantite = quantite
                )

                // Ajouter ou mettre à jour l'objet dans l'inventaire
                inventaire.ajouterObjet(objetObtenu, quantite)

                // Déduire les pièces nécessaires pour l'achat
                inventaire.ajouterPieces(-totalPrix)

                // Mise à jour de l'inventaire dans la base de données
                inventaireDAO.mettreAJourQuantiteObjet(objetObtenu.getId(), objetObtenu.getQuantite())
                inventaireDAO.mettreAJourPieces(inventaire.getPieces())
            } else {
                println("Solde insuffisant pour acheter $quantite x ${objet.getNom()}.")
            }
        } else {
            println("Objet $nom non trouvé dans la boutique.")
        }
    }

    // Méthode pour lister tous les objets disponibles dans la boutique
    fun listerObjet(): List<Objet> {
        return objetDAO.obtenirTous()
    }

    // Méthode pour lister les objets par type
    fun listerObjet(type: TypeObjet): List<Objet> {
        return objetDAO.obtenirParType(type)
    }
}
