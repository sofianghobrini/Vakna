package com.app.vakna.modele

import android.content.Context
import com.app.vakna.LocaleHelper
import com.app.vakna.adapters.GridConsommableData

open class Objet(
    private var id: Int,
    private var nom: Map<String, String>,
    private var prix: Int,
    private var niveau: Int,
    private var type: TypeObjet,
    private var detail: Map<String, String>,
    private var imageUrl: String
) {

    init {
        setNom(nom)
        setPrix(prix)
        setDetails(detail)
        require(imageUrl.isNotBlank()){"L'URL de l'image ne doit pas être vide"}
    }

    open fun toGridData(context: Context): GridConsommableData {
        return GridConsommableData(getImageUrl(), getNom(context), getNiveau(), getPrix())
    }

    fun getId(): Int {
        return id
    }

    fun getNom(context: Context): String {
        return nom[LocaleHelper.getLanguage(context)]!!
    }

    fun getNom(): Map<String, String> {
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

    fun getDetails(context: Context): String {
        return detail[LocaleHelper.getLanguage((context))]!!
    }

    fun getDetails(): Map<String, String> {
        return detail
    }

    fun setNom(noms: Map<String, String>) {
        if (nom.isEmpty()) {
            throw IllegalArgumentException("Le nom ne peut pas être vide ou seulement des espaces.")
        }
        this.nom = noms
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

    fun setDetails(details: Map<String, String>) {
        if (detail.isEmpty()) {
            throw IllegalArgumentException("Les détails ne peuvent pas être vides.")
        }
        this.detail = details
    }
    fun getImageUrl(): String {
        return imageUrl
    }

    override fun toString(): String {
        return "$id $nom ($type) $prix€ : $detail [niveau=$niveau]"
    }
}
