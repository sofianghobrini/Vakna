package com.app.vakna.modele

import android.content.Context
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.InventaireDAO

class Inventaire(context: Context) {
    private var contexte: Context = context
    private var inventaireDAO = InventaireDAO(contexte)
    private var objets = mutableListOf<ObjetObtenu>()
    private var pieces = 0
    private var gestionnaireCompagnons = GestionnaireDeCompagnons(CompagnonDAO(contexte))


    init {
        inventaireDAO.obtenirTousObjetsObtenus().forEach { objets.add(it) }
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
        return objets.find { it.getNom(contexte) == nom }
    }

    fun getObjetById(id: Int): ObjetObtenu? {
        return objets.find { it.getId() == id }
    }

    fun getGestionnaireC(): GestionnaireDeCompagnons {
        return gestionnaireCompagnons
    }

    private fun utiliserObjet(objet: ObjetObtenu) {
        assert(objet.getQuantite() > 0) { "La quantité de l'objet ne peut pas être négative ou nulle" }
        val niveau = objet.getNiveau()
        var compagnon = gestionnaireCompagnons.obtenirActif()
        if (compagnon == null) {
            compagnon = gestionnaireCompagnons.obtenirCompagnons().first()
        }
       val idCompagnon = compagnon.id
        if (objet.getType() == TypeObjet.JOUET) {
            if(compagnon.personnalite == Personnalite.JOUEUR){
                gestionnaireCompagnons.modifierHumeur(idCompagnon, (niveau * compagnon.personnalite.facteurHumeur).toInt() )
            }
            else{
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
        val objet = getObjetParNom(nom)
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
        val objetNouveau = getObjetParNom(objet.getNom(contexte))
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