package com.app.vakna.modele

class modeleCampagnon(
    nom:String,
    private val faim:Int = 50,
    private val humeur:Int = 50,
    private val xp:Int = 0,
    private val espece:String
){
    fun modifierFaim(niveau:Int){
        faim = niveau + faim
        assert(faim > 100) {"Le niveau de faim ne peut pas depasser de 100"}
        if(faim > 100)
            faim = 100

    }
    fun modifierHumeur(niveau: Int){

    }
    fun gagnerXp(montant:Int){

    }
    fun niveau(){

    }
}