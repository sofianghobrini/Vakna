package com.app.vakna.modele

open class Objet(
    private var nom: String,
    private var prix: Int,
    private var niveau: Int,
    private var type: String,
    private var detail: String
) {
    fun getNom(): String {
        return nom
    }

    fun getPrix(): Int {
        return prix
    }

    fun getNiveau(): Int {
        return niveau
    }

    fun getType(): String {
        return type
    }

    fun getDetails(): String {
        return detail
    }
}