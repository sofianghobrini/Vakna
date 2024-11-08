package com.app.vakna.modele

import com.app.vakna.adapters.GridConsommableData

open class Objet(
    private var id: Int,
    private var nom: String,
    private var prix: Int,
    private var niveau: Int,
    private var type: TypeObjet,
    private var detail: String,
    private var imageUrl: String
) {

    init {
        setNom(nom)
        setPrix(prix)
        setDetails(detail)
        require(imageUrl.isNotBlank()){"L'URL de l'image ne doit pas être vide"}
    }

    open fun toGridData(): GridConsommableData {
        return GridConsommableData(getImageUrl(), getNom(), getNiveau(), getPrix())
    }

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

    fun setNom(nom: String) {
        if (nom.isBlank()) {
            throw IllegalArgumentException("Le nom ne peut pas être vide ou seulement des espaces.")
        }
        this.nom = nom
    }

    fun setPrix(prix: Int) {
        if (prix < 0) {
            throw IllegalArgumentException("Le prix ne peut pas être négatif.")
        }
        this.prix = prix
    }

    fun setNiveau(niveau: Int) {
        this.niveau = niveau
    }

    fun setType(type: TypeObjet) {
        this.type = type
    }

    fun setDetails(detail: String) {
        if (detail.isBlank()) {
            throw IllegalArgumentException("Les détails ne peuvent pas être vides.")
        }
        this.detail = detail
    }
    fun getImageUrl(): String {
        return imageUrl
    }

}
