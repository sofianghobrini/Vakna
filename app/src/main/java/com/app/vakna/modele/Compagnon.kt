package com.app.vakna.modele

import com.app.vakna.modele.dao.CompagnonDAO

class Compagnon(
    var nom:String,
    var faim:Int = 50,
    var humeur:Int = 50,
    var xp:Int = 0,
    val espece:String
){
    private val dao = CompagnonDAO()

    fun modifierFaim(niveau:Int){
        assert(niveau in -100..100) { "Le niveau de faim doit être compris entre -100 et 100." }

        faim += niveau
        if(faim > 100)
            faim = 100
        else if(faim <0)
            faim = 0
        dao.modifier(nom, this)
    }
    fun modifierHumeur(niveau: Int){
        assert(niveau in -100..100) { "Le niveau d'humeur doit être compris entre -100 et 100." }

        humeur += niveau
        if(humeur > 100)
            humeur = 100
        else if(humeur <0)
            humeur = 0
        dao.modifier(nom, this)
    }

    fun gagnerXp(montant:Int){
        val ancienNiveau = niveau()
        xp += montant
        if (ancienNiveau > niveau())
            xp -= montant
        dao.modifier(nom, this)
    }

    fun niveau(): Int {
        if (xp < 100)
            return 0
        if (xp < 250)
            return 1
        if (xp < 1000)
            return 2
        return 3
    }

    override fun toString(): String {
        return "$nom ($espece) : Faim = $faim, Humeur = $humeur, XP = $xp (niveau ${niveau()})"
    }
}