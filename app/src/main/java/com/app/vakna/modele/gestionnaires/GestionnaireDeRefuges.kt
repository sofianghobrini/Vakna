package com.app.vakna.modele.gestionnaires

import android.content.Context
import com.app.vakna.modele.dao.refuge.Refuge
import com.app.vakna.modele.dao.refuge.RefugeDAO

class GestionnaireDeRefuges(context: Context) {
    private var refugeDAO = RefugeDAO(context)
    private var refuges = mutableSetOf<Refuge>()

    init {
        refugeDAO.obtenirTous().forEach { refuges.add(it) }
    }

    fun obtenirRefuges(): Set<Refuge> {
        refugeDAO.obtenirTous().forEach { refuges.add(it) }
        return refuges
    }

    fun obtenirRefuge(nom: String): Refuge? {
        return refuges.find { it.getNom() == nom }
    }

    fun obtenirRefuge(id: Int): Refuge? {
        return refuges.find { it.getId() == id }
    }

    fun ajouterRefuge(refuge: Refuge): Boolean {
        if (refuge.getNom().isBlank()) {
            throw IllegalArgumentException("Le nom du refuge ne peut pas être vide")
        }
        if (!refuges.add(refuge)) {
            throw IllegalArgumentException("Un Refuge avec le nom '${refuge.getNom()}' existe déjà")
        }
        return refugeDAO.inserer(refuge)
    }

    fun obtenirActif(): Refuge? {
        return refuges.find { it.getActif() == true }
    }

    fun setActif(id: Int) {
        val refuge = obtenirRefuge(id)
        val actifActuel = obtenirActif()
        actifActuel?.let {
            it.setActif(false)
            refugeDAO.modifier(it.getId(), it)
        }
        refuge?.let {
            it.setActif(true)
            refugeDAO.modifier(it.getId(), it)
        }
    }
}