package com.app.vakna.modele.dao.objet

import android.content.Context
import com.app.vakna.modele.dao.AccesJson
import com.app.vakna.modele.dao.DAO
import com.app.vakna.modele.dao.TypeObjet
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

class ObjetDAO(contexte: Context) : DAO<Objet, Int> {

    // Initialisation de l'objet Gson avec un adaptateur personnalisé pour les objets Objet
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Objet::class.java, JsonToObjet())
        .registerTypeAdapter(Objet::class.java, ObjetToJson())
        .create()

    // Gestion de l'accès aux fichiers JSON
    private val accesJson = AccesJson("objets", contexte)

    // Vérifier l'existence du fichier, sinon en créer un vide
    private fun verifierExistence() {
        if (!accesJson.fichierExiste()) {
            val emptyJson = """{"objets": []}"""
            accesJson.ecrireFichierJson(emptyJson)
        }
    }

    // Obtenir tous les objets depuis le fichier JSON
    override fun obtenirTous(): List<Objet> {
        verifierExistence()
        val jsonString = accesJson.lireFichierJson()

        val objetsJsonArray = gson.fromJson(jsonString, JsonElement::class.java)
            .asJsonObject.getAsJsonArray("objets")

        val objetListType = object : TypeToken<List<Objet>>() {}.type
        return gson.fromJson(objetsJsonArray, objetListType)
    }

    // Obtenir un compagnon par son ID
    override fun obtenirParId(id: Int): Objet? {
        return obtenirTous().find { it.getId() == id }
    }

    // Insérer un nouvel objet dans le fichier JSON
    override fun inserer(entite: Objet): Boolean {
        verifierExistence()
        val jsonString = accesJson.lireFichierJson()

        val objetsJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val objetsJsonArray = objetsJson.getAsJsonArray("objets")

        val listeObjets: MutableList<Objet> = gson.fromJson(
            objetsJsonArray,
            object : TypeToken<MutableList<Objet>>() {}.type
        )

        // Vérifier si un objet avec le même nom existe déjà
        if (listeObjets.any { it.getId() == entite.getId() }) {
            return false
        }

        listeObjets.add(entite)
        objetsJson.add("objets", gson.toJsonTree(listeObjets))
        accesJson.ecrireFichierJson(gson.toJson(objetsJson))

        return true
    }

    // Modifier un objet existant dans le fichier JSON
    override fun modifier(id: Int, entite: Objet): Boolean {
        val objets = obtenirTous().toMutableList()
        val indexObjetAModifier = objets.indexOfFirst { it.getId() == id }

        if (indexObjetAModifier == -1) {
            return false // Object not found
        }

        objets[indexObjetAModifier] = entite
        val objetsJson = gson.toJsonTree(objets)

        val jsonObject = gson.fromJson(accesJson.lireFichierJson(), JsonElement::class.java).asJsonObject
        jsonObject.add("objets", objetsJson)

        accesJson.ecrireFichierJson(gson.toJson(jsonObject))

        return true
    }

    override fun supprimer(id: Int): Boolean {
        val objets = obtenirTous().toMutableList()
        val objetToRemove = objets.find { it.getId() == id } ?: return false

        objets.remove(objetToRemove)
        val objetsJson = gson.toJsonTree(objets)

        val jsonObject = gson.fromJson(accesJson.lireFichierJson(), JsonElement::class.java).asJsonObject
        jsonObject.add("objets", objetsJson)

        accesJson.ecrireFichierJson(gson.toJson(jsonObject))

        return true
    }

    // Method to obtain objects by type (useful for the shop)
    fun obtenirParType(type: TypeObjet): List<Objet> {
        return obtenirTous().filter { it.getType() == type }
    }
}
