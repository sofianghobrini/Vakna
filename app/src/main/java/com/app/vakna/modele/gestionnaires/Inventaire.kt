package com.app.vakna.modele.gestionnaires

import android.content.Context
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.dao.objetobtenu.ObjetObtenu
import com.app.vakna.modele.dao.Personnalite
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.InventaireDAO

class Inventaire(context: Context) {
    private var contexte: Context = context
    private var pieces = 0
    private var inventaireDAO = InventaireDAO(context)
    private var objets = mutableListOf<ObjetObtenu>()
    private var gestionnaireCompagnons = GestionnaireDeCompagnons(context)

    init {
        inventaireDAO.obtenirTousObjetsObtenus().forEach { objets.add(it) }
        pieces = inventaireDAO.obtenirPieces()
    }

    fun obtenirObjets(): List<ObjetObtenu> {
        return objets
    }

    fun obtenirObjets(type: TypeObjet): List<ObjetObtenu> {
        return objets.filter { it.getType() == type }
    }

    fun obtenirPieces(): Int {
        return pieces
    }

    fun obtenirObjet(nom: String): ObjetObtenu? {
        return objets.find { it.getNom(contexte) == nom }
    }

    private fun utiliserObjet(objet: ObjetObtenu) {
        assert(objet.getQuantite() > 0) { "La quantité de l'objet ne peut pas être négative ou nulle" }
        val niveau = objet.getNiveau()
        var compagnon = gestionnaireCompagnons.obtenirActif()
        if (compagnon == null) {
            compagnon = gestionnaireCompagnons.obtenirCompagnons().first()
            gestionnaireCompagnons.setActif(compagnon.id)
        }
        val idCompagnon = compagnon.id
        if (objet.getType() == TypeObjet.JOUET) {
            if(compagnon.personnalite == Personnalite.JOUEUR){
                gestionnaireCompagnons.modifierHumeur(idCompagnon, (niveau * compagnon.personnalite.facteurHumeur).toInt() )
            } else {
                gestionnaireCompagnons.modifierHumeur(idCompagnon, niveau)
            }
        } else if (objet.getType() == TypeObjet.NOURRITURE) {
            if(compagnon.personnalite == Personnalite.GOURMAND){
                gestionnaireCompagnons.modifierHumeur(idCompagnon, (niveau * 1.6).toInt())
            }
            gestionnaireCompagnons.modifierFaim(idCompagnon, niveau)
        }
        objet.updateQuantite(-1)
    }

    fun utiliserObjet(nom: String, n: Int) {
        assert(n > 0){"On ne peut pas utiliser 0 objet"}
        val objet = obtenirObjet(nom)
        assert(objet != null) { "On ne peut pas utiliser un objet que l'on ne possède pas" }
        assert(objet!!.getQuantite() >= n) { "On ne peut pas consommer plus d'objets que l'on en possède" }

        for (i in 0 until n) {
            utiliserObjet(objet)
        }
        inventaireDAO.mettreAJourQuantiteObjet(objet.getId(), objet.getQuantite())
    }

    fun ajouterObjet(objet: Objet, quantite: Int) {
        assert(quantite > 0) { "La quantité d'objets ne peut pas être négative ou nulle" }
        val nouvelObjet = ObjetObtenu(objet.getId(),objet.getNom(),objet.getPrix(), objet.getNiveau(), objet.getType(), objet.getDetails(), 0, objet.getImageUrl(), contexte)
        if (!objets.any { it.getId() == objet.getId() }) {
            objets += nouvelObjet
            inventaireDAO.insererObjetObtenu(nouvelObjet)
        }
        val objetNouveau = obtenirObjet(objet.getNom(contexte))
        objetNouveau?.updateQuantite(quantite)
        inventaireDAO.mettreAJourQuantiteObjet(objetNouveau!!.getId(), objetNouveau.getQuantite())
    }

    fun ajouterPieces(valeur: Int) {
        assert(pieces + valeur >= 0) { "Il est impossible d'avoir des pièces en négatif" }
        pieces += valeur
        inventaireDAO.mettreAJourPieces(pieces)
    }

    companion object {
        fun setToGridDataArray(objets: List<ObjetObtenu>, context: Context): ArrayList<GridConsommableData> {
            val list = ArrayList<GridConsommableData>()
            for (objet in objets) {
                val listData = objet.toGridData(context)
                list.add(listData)
            }
            return list
        }
    }
}