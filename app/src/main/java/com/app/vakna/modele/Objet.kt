package com.app.vakna.modele

open class Objet(
    private var id: Int,
    private var nom: String,
    private var prix: Int,
    private var niveau: Int,
    private var type: TypeObjet,
    private var detail: String
) {
    fun getId(): Int {
        return id
    }

    fun getNom(): String {
        return nom
    }

    fun getPrix(): Int {
        return prix
    }

    fun getNiveau(): Int {
        return niveau
    }

    fun getType(): TypeObjet {
        return type
    }

    fun getDetails(): String {
        return detail
    }
}
