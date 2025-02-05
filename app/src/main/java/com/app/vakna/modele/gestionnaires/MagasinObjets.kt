package com.app.vakna.modele.gestionnaires

import android.content.Context
import android.util.Log
import com.app.vakna.adapters.GridConsommableData
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.api.RetrofitInstance
import com.app.vakna.modele.dao.InventaireDAO
import com.app.vakna.modele.dao.objet.ObjetDAO

class Shop(
    private var context: Context
) {

    private val objetDAO = ObjetDAO(context)
    private val inventaireDAO = InventaireDAO(context)
    private var objetMagasin = mutableListOf<Objet>()
    private val inventaire = Inventaire(context)

    init {
        objetDAO.obtenirTous().forEach { objetMagasin.add(it) }
    }

    // Méthode pour obtenir un objet par son nom
    fun obtenirObjet(nom: String): Objet? {
        return objetMagasin.find { it.getNom(context) == nom }
    }

    fun obtenirObjets(type: TypeObjet): List<Objet> {
        return objetMagasin.filter { it.getType() == type }
    }

    // Méthode pour obtenir tous les objets
    fun obtenirObjets(): List<Objet> {
        return objetMagasin
    }

    suspend fun obtenirObjetsEnLigne(): List<Objet> {
        try {

            val nouveauxObjets = RetrofitInstance.apiService.obtenirObjets()
            objetDAO.remplacerObjets(nouveauxObjets)
            nouveauxObjets.forEach {
                obj -> RetrofitInstance.downloadImage(
                    context,
                    RetrofitInstance.BASE_URL + "images/objets/" + obj.getImageUrl(),
                    obj.getImageUrl(),
                    "objets"
                )
            }

            return nouveauxObjets
        } catch (e: Exception) {
            Log.d("test", "Erreur : $e")
            return emptyList()
        }
    }

    // Méthode pour acheter une certaine quantité d'un objet
    fun acheter(nom: String, quantite: Int) {
        val objet = obtenirObjet(nom)
        if (objet != null) {
            val totalPrix = objet.getPrix() * quantite
            if (inventaireDAO.obtenirPieces() >= totalPrix) {
                inventaire.ajouterObjet(objet, quantite)
                val nouvellesPieces = inventaire.obtenirPieces() - totalPrix
                inventaireDAO.mettreAJourPieces(nouvellesPieces)
            }
        }
    }

    // Méthode pour lister les objets par type
    fun listerObjet(type: TypeObjet): List<Objet> {
        return objetMagasin.filter { it.getType() == type }
    }

    companion object {
        fun setToGridDataArray(objets: List<Objet>, context: Context): ArrayList<GridConsommableData> {
            val list = ArrayList<GridConsommableData>()
            for (objet in objets) {
                val listData = objet.toGridData(context)
                list.add(listData)
            }
            return list
        }
    }
}
