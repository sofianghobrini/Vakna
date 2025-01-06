package com.app.vakna.modele

import android.content.Context
import com.app.vakna.modele.dao.RefugeDAO

class GestionnaireDeRefuge(context: Context) {
    private var refugeDAO = RefugeDAO(context)
    private var refuges = mutableSetOf<Refuge>()

    init {
        refugeDAO.obtenirTous().forEach { refuges.add(it) }
    }

    fun getRefuges(): Set<Refuge> {
        refugeDAO.obtenirTous().forEach { refuges.add(it) }
        return refuges
    }

    fun getRefugeParNom(nom: String): Refuge? {
        return refuges.find { it.getNom() == nom }
    }

    fun getRefugeParId(id: Int): Refuge? {
        return refuges.find { it.getId() == id }
    }

    fun getActif(): Refuge? {
        return refuges.find { it.getActif() == true }
    }

    fun setActif(id: Int) {
        val refuge = getRefugeParId(id)
        val actifActuel = getActif()
        actifActuel?.let {
            it.setActif(false)
            refugeDAO.modifier(it.getId(), it)
        }
        refuge?.let {
            it.setActif(true)
            refugeDAO.modifier(it.getId(), it)
        }
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
}