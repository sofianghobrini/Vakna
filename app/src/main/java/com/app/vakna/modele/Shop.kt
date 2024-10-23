package com.app.vakna.modele

import android.content.Context
import com.app.vakna.modele.dao.InventaireDAO
import com.app.vakna.modele.dao.ObjetDAO

class Shop(
    private val context: Context
) {

    private val objetDAO = ObjetDAO(context)
    private val inventaireDAO = InventaireDAO(context)
    private var objetMagasin = mutableListOf<Objet>()

    init {
        objetDAO.obtenirTous().forEach() { objetMagasin.add(it) }
    }

    // Méthode pour obtenir un objet par son nom
    fun getObjet(nom: String): Objet? {
        return objetMagasin.find { it.getNom() == nom }
    }

    // Méthode pour obtenir tous les objets
    fun getObjets(): List<Objet> {
        return objetMagasin
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
                inventaire.ajouterObjet(objet, quantite)
                inventaire.ajouterPieces(-totalPrix)
                inventaireDAO.mettreAJourPieces(inventaire.getPieces())
            }
        }
    }

    // Méthode pour lister tous les objets disponibles dans la boutique
    fun listerObjet(): List<Objet> {
        return objetMagasin
    }

    // Méthode pour lister les objets par type
    fun listerObjet(type: TypeObjet): List<Objet> {
        return objetMagasin.filter { it.getType() == type}
    }
}
