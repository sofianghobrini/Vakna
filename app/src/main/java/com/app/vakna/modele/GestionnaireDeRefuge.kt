package com.app.vakna.modele

import android.content.Context
import com.app.vakna.modele.dao.RefugeDAO

class GestionnaireDeRefuge(contexte: Context) {
    private var refugeDAO = RefugeDAO(contexte)
    private var refuges = mutableListOf<Refuge>()

    init {
        refugeDAO.obtenirTous().forEach { refuges.add(it) }
    }

    fun getRefuges(): List<Refuge> {
        return refuges
    }

    fun getRefugeParNom(nom: String): Refuge? {
        return refuges.find { it.getNom() == nom }
    }

    fun getRefugeParId(id: Int): Refuge? {
        return refuges.find { it.getId() == id }
    }

    fun ajouterRefuge(refuge: Refuge) {
        assert(getRefugeParId(refuge.getId()) == null) { "Il ne peut y avoir deux fois le meme refuge" }
        refuges.add(refuge)
        refugeDAO.inserer(refuge)
    }
}