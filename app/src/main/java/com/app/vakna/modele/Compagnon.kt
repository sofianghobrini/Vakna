package com.app.vakna.modele

import com.app.vakna.modele.dao.CompagnonDAO

class Compagnon(
    var id: Int,
    var nom: String,
    var faim: Int = 50,
    var humeur: Int = 50,
    var xp: Int = 0,
    val espece: String
) {
    private val dao = CompagnonDAO()

    fun modifierFaim(niveau: Int) {
        assert(niveau in -100..100) { "Le niveau de faim doit être compris entre -100 et 100." }

        faim += niveau
        faim = faim.coerceIn(0, 100)
        dao.modifier(id, this)
    }

    fun modifierHumeur(niveau: Int) {
        assert(niveau in -100..100) { "Le niveau d'humeur doit être compris entre -100 et 100." }

        humeur += niveau
        humeur = humeur.coerceIn(0, 100)
        dao.modifier(id, this)
    }

    fun gagnerXp(montant: Int) {
        val ancienNiveau = niveau()
        xp += montant
        if (ancienNiveau > niveau())
            xp -= montant
        dao.modifier(id, this)
    }

    fun niveau(): Int {
        return when {
            xp < 100 -> 0
            xp < 250 -> 1
            xp < 1000 -> 2
            else -> 3
        }
    }

    override fun toString(): String {
        return "$nom ($espece) : Faim = $faim, Humeur = $humeur, XP = $xp (niveau ${niveau()})"
    }
}
