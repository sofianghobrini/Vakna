package com.app.vakna.modele

class Refuge(
    private var id: Int,                // Identifiant unique de l'environnement
    private var nom: String,            // Nom de l'environnement
    private var modifFaim : Int = 1,         // Modificateur de faim
    private var modifHumeur: Int = 1,       // Modificateur d'humeur
    private var modifXp: Int = 1,            // Modificateur d'xp
    private var modifPieces: Int = 1         // Modificateur de pieces
) {
    fun getId(): Int {
        return id
    }
    fun getNom(): String {
        return nom
    }
    fun getModifFaim(): Int {
        return modifFaim
    }
    fun getModifHumeur(): Int {
        return modifHumeur
    }
    fun getModifXp(): Int {
        return modifXp
    }
    fun getModifPieces(): Int {
        return modifPieces
    }
}