package com.app.vakna.modele

import android.content.Context
import com.app.vakna.adapters.GridData
import com.app.vakna.adapters.ListData
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.InventaireDAO
import java.lang.reflect.Type

class Inventaire(private var contexte: Context) {
    private var inventaireDAO = InventaireDAO(contexte)
    private var objets = mutableListOf<ObjetObtenu>()
    private var pieces = 0
    private var gestionnaireCompagnons = GestionnaireDeCompagnons(CompagnonDAO(contexte))


    init {
        inventaireDAO.obtenirTousObjetsObtenus().forEach() { objets.add(it) }
        pieces = inventaireDAO.obtenirPieces()
    }

    fun getObjets(): List<ObjetObtenu> {
        return objets
    }

    fun getObjetsParType(type: TypeObjet): List<ObjetObtenu> {
        return objets.filter { it.getType() == type }
    }

    fun getPieces(): Int {
        return pieces
    }

    fun getObjetParNom(nom: String): ObjetObtenu? {
        return objets.find { it.getNom() == nom }
    }

    fun getObjetById(id: Int): ObjetObtenu? {
        return objets.find { it.getId() == id }
    }

    private fun utiliserObjet(objet: ObjetObtenu) {
        assert(objet.getQuantite() > 0) { "La quantité de l'objet ne peut pas être négative ou nulle" }
        val niveau = objet.getNiveau()
        if (objet.getType() == TypeObjet.JOUET) {
            gestionnaireCompagnons.modifierFaim(0, niveau)
        } else if (objet.getType() == TypeObjet.NOURRITURE) {
            gestionnaireCompagnons.modifierHumeur(0, niveau)
        }
        objet.updateQuantite(-1)
    }

    fun utiliserObjet(nom: String, n: Int) {
        val objet = getObjetParNom(nom)
        assert(objet != null) { "On ne peut pas utiliser un objet que l'on ne possède pas" }
        assert(objet!!.getQuantite() >= n) { "On ne peut pas consommer plus d'objets que l'on en possède" }

        for (i in 0 until n) {
            utiliserObjet(objet)
        }
        inventaireDAO.mettreAJourQuantiteObjet(objet.getId(), objet.getQuantite())
    }

    fun ajouterObjet(objet: Objet, quantite: Int) {
        var nouvelObjet = ObjetObtenu(objet.getId(),objet.getNom(),objet.getPrix(), objet.getNiveau(), objet.getType(), objet.getDetails(), 0, objet.getImageUrl())
        if (!objets.any { it.getId() == objet.getId() }) {
            objets += nouvelObjet
            inventaireDAO.insererObjetObtenu(nouvelObjet)
        }
        getObjetParNom(objet.getNom())?.updateQuantite(quantite)
        inventaireDAO.mettreAJourQuantiteObjet(nouvelObjet.getId(), nouvelObjet.getQuantite())
    }

    fun ajouterPieces(valeur: Int) {
        assert(pieces + valeur >= 0) { "Il est impossible d'avoir des pièces en négatif" }
        pieces += valeur
        inventaireDAO.mettreAJourPieces(pieces)
    }

    companion object {
        fun setToGridDataArray(objets: List<ObjetObtenu>): ArrayList<GridData> {
            val list = ArrayList<GridData>()
            for (objet in objets) {
                val listData = objet.toGridData()
                list.add(listData)
            }
            return list
        }
    }
}