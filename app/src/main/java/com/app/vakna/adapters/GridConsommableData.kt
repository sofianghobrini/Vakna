package com.app.vakna.adapters

import com.app.vakna.modele.dao.TypeObjet

class GridConsommableData (
    image: String,
    nom: String,
    val niveau: Int,
    cout: Int,
    val type: TypeObjet,
    val qte: Int? = null): GridData(image, nom, cout)