package com.app.vakna.modele

import android.util.Log
import com.app.vakna.modele.dao.CompagnonDAO

class GestionnaireDeCompagnons(private var dao : CompagnonDAO) {
    private val setDeCompagnons = mutableSetOf<Compagnon>()

    init {
        dao.obtenirTous().forEach { setDeCompagnons.add(it) }
    }

    fun ajouterCompagnon(compagnon: Compagnon): Boolean {
        if (compagnon.nom.isBlank()) {
            throw IllegalArgumentException("Le nom du compagnon ne peut pas être vide")
        }
        if (!setDeCompagnons.add(compagnon)) {
            throw IllegalArgumentException("Une tâche avec le nom '${compagnon.nom}' existe déjà")
        }
        return dao.inserer(compagnon)
    }

    fun modifierCompagnon(id: Int, nouveauCompagnon: Compagnon): Boolean {
        val compagnon = setDeCompagnons.find { it.id == id }
        if (compagnon != null) {
            compagnon.nom = nouveauCompagnon.nom
            compagnon.faim = nouveauCompagnon.faim
            compagnon.humeur = nouveauCompagnon.humeur
            compagnon.xp = nouveauCompagnon.xp
            compagnon.espece = nouveauCompagnon.espece

            return dao.modifier(id, nouveauCompagnon)
        } else {
            throw IllegalArgumentException("Compagnon avec l'id $id introuvable")
        }
    }

    fun modifierFaim(id: Int, niveau: Int) {
        // Vérification que le niveau est compris entre -100 et 100
        assert(niveau in -100..100) { "Le niveau de faim doit être compris entre -100 et 100." }
        val compagnon = setDeCompagnons.find { it.id == id }?: return

        // Modification du niveau de faim et forçage de la valeur entre 0 et 100
        compagnon.faim += niveau
        compagnon.faim = compagnon.faim.coerceIn(0, 100)
        dao.modifier(id, compagnon)
    }

    // Méthode pour modifier le niveau d'humeur du compagnon
    fun modifierHumeur(id: Int, niveau: Int) {
        // Vérification que le niveau est compris entre -100 et 100
        assert(niveau in -100..100) { "Le niveau de faim doit être compris entre -100 et 100." }
        val compagnon = setDeCompagnons.find { it.id == id }?: return

        // Modification du niveau d'humeur et forçage de la valeur entre 0 et 100
        compagnon.humeur += niveau
        compagnon.humeur = compagnon.humeur.coerceIn(0, 100)
        dao.modifier(id, compagnon)
    }

    // Méthode pour ajouter de l'expérience (XP) au compagnon
    fun gagnerXp(id: Int, montant: Int) {

        //assert(0 < montant) { "Le montant d'xp a gagné doit être strictement positif" }
        Log.i("test2", "gagnerXP montant : $montant, idCompa : $id")
        val compagnon = setDeCompagnons.find { it.id == id }?: return
        compagnon.xp += montant
        Log.i("test2", "ca a marché ????")
        dao.modifier(id, compagnon)
    }

    fun obtenirCompagnons(): Set<Compagnon> {
        dao.obtenirTous().forEach { setDeCompagnons.add(it) }
        return setDeCompagnons
    }

    fun obtenirCompagnon(id: Int): Compagnon? {
        return obtenirCompagnons().find { it.id == id }
    }
}