package com.app.vakna.modele

class modeleCampagnon(
    nom:String,
    private var faim:Int = 50,
    private var humeur:Int = 50,
    private var xp:Int = 0,
    private val espece:String
){
    fun modifierFaim(niveau:Int){
        if(niveau > 0)
            faim += niveau
            assert(faim > 100) {"Le niveau de faim ne peut pas depasser de 100"}
            if(faim > 100)
                faim = 100
        else
            faim -= niveau
            assert(faim > 0){"Le niveau de faim ne peut pas descendre en dessous 0"}
            if(faim < 0)
                faim = 0

    }
    fun modifierHumeur(niveau: Int){
        if(niveau > 0)
            humeur += niveau
        assert(humeur > 100) {"Le niveau de faim ne peut pas depasser de 100"}
        if(humeur > 100)
            humeur = 100
        else
            humeur -= niveau
        assert(humeur > 0){"Le niveau de faim ne peut pas descendre en dessous 0"}
        if(humeur < 0)
            humeur = 0

    }
    fun gagnerXp(montant:Int){

    }
    fun niveau(){

    }
}