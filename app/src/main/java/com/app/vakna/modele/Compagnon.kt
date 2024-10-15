package com.app.vakna.modele

class Compagnon(
    nom:String,
    private var faim:Int = 50,
    private var humeur:Int = 50,
    private var xp:Int = 0,
    private val espece:String
){
    fun getFaim(): Int {
        return faim
    }

    fun getHumeur(): Int {
        return humeur
    }

    fun getXp(): Int {
        return xp
    }

    fun modifierFaim(niveau:Int){
        assert(niveau in -100..100) { "Le niveau de faim doit être compris entre -100 et 100." }

        faim += niveau
        if(faim > 100)
            faim = 100
        else if(faim <0)
            faim = 0
    }
    fun modifierHumeur(niveau: Int){
        assert(niveau in -100..100) { "Le niveau d'humeur doit être compris entre -100 et 100." }

        humeur += niveau
        if(humeur > 100)
            humeur = 100
        else if(humeur <0)
            humeur = 0
    }
    fun gagnerXp(montant:Int){
        val ancienNiveau = niveau()
        xp += montant
        if (ancienNiveau > niveau())
            xp -= montant
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
}