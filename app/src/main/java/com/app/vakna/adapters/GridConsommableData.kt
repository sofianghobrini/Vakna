package com.app.vakna.adapters

class GridConsommableData (
    image: Int,
    nom: String,
    val niveau: Int,
    cout: Int,
    val qte: Int? = null): GridData(image, nom, cout)