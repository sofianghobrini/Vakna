package com.app.vakna.modele

import android.util.Log
import com.app.vakna.modele.dao.CompagnonDAO
import java.time.LocalDate

class Compagnon(
    var id: Int,                // Identifiant unique du compagnon
    var nom: String,            // Nom du compagnon
    var faim: Int = 50,         // Niveau de faim (valeur par défaut = 50)
    var humeur: Int = 50,       // Niveau d'humeur (valeur par défaut = 50)
    var xp: Int = 0,            // Expérience (XP) du compagnon (par défaut = 0)
    var espece: String,          // Espèce du compagnon (par exemple, "Dragon")
) {

    // Méthode pour déterminer le niveau actuel du compagnon en fonction de son XP
    fun niveau(): Int {
        return xp / 100  // Le niveau est calculé en divisant l'XP par 100
    }

    // Redéfinition de la méthode toString pour afficher les informations du compagnon
    override fun toString(): String {
        return "$nom ($espece) : Faim = $faim, Humeur = $humeur, XP = $xp (niveau ${niveau()})"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Compagnon) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

class GestionnaireDeCompagnons(private var dao :CompagnonDAO) {
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