package com.app.vakna.modele.api

data class ObjetData (
    val id: Int,
    val nom: String,
    val prix: Int,
    val niveau: Int,
    val type: String,
    val detail: String,
    val imageUrl: String
)