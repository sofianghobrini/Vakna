package com.app.vakna.modele

import android.content.Context
import android.util.Log
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.modele.api.RetrofitInstance
import com.app.vakna.modele.api.XmlObjetsManager
import com.app.vakna.modele.dao.InventaireDAO
import com.app.vakna.modele.dao.ObjetDAO

class Shop(
    context: Context
) {

    private val objetDAO = ObjetDAO(context)
    private val inventaireDAO = InventaireDAO(context)
    private var objetMagasin = mutableListOf<Objet>()
    private val inventaire = Inventaire(context)

    init {
        objetDAO.obtenirTous().forEach { objetMagasin.add(it) }
    }

    // Méthode pour obtenir un objet par son nom
    fun getObjet(nom: String): Objet? {
        return objetMagasin.find { it.getNom() == nom }
    }

    fun getObjetsParType(type: TypeObjet): List<Objet> {
        return objetMagasin.filter { it.getType() == type }
    }

    // Méthode pour obtenir tous les objets
    fun getObjets(): List<Objet> {
        return objetMagasin
    }

    suspend fun getObjetsEnLigne(context: Context): List<Objet> {
        try {

            val nouveauxObjets = RetrofitInstance.apiService.obtenirObjets()
            objetDAO.remplacerObjets(nouveauxObjets)
            val objetsManager = XmlObjetsManager(context)
            objetsManager.creerFichiers()
            objetsManager.remplirFichiers(nouveauxObjets)

            return nouveauxObjets
        } catch (e: Exception) {
            Log.d("test", "Erreur : $e")
            return emptyList()
        }
    }

    // Méthode pour acheter une certaine quantité d'un objet
    fun acheter(nom: String, quantite: Int) {
        val objet = getObjet(nom)
        if (objet != null) {
            val totalPrix = objet.getPrix() * quantite
            if (inventaireDAO.obtenirPieces() >= totalPrix) {
                inventaire.ajouterObjet(objet, quantite)
                val nouvellesPieces = inventaire.getPieces() - totalPrix
                inventaireDAO.mettreAJourPieces(nouvellesPieces)
            }
        }
    }

    // Méthode pour lister tous les objets disponibles dans la boutique
    fun listerObjet(): List<Objet> {
        return objetMagasin
    }

    // Méthode pour lister les objets par type
    fun listerObjet(type: TypeObjet): List<Objet> {
        return objetMagasin.filter { it.getType() == type }
    }

    companion object {
        fun setToGridDataArray(objets: List<Objet>): ArrayList<GridConsommableData> {
            val list = ArrayList<GridConsommableData>()
            for (objet in objets) {
                val listData = objet.toGridData()
                list.add(listData)
            }
            return list
        }
    }
}
