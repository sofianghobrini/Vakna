package com.app.vakna.modele.dao

import android.content.Context
import com.app.vakna.modele.Objet
import com.app.vakna.modele.TypeObjet
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

class ObjetDAO(contexte: Context) : DAO<Objet, Int> {

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Objet::class.java, JsonToObjet())
        .registerTypeAdapter(Objet::class.java, ObjetToJson())
        .create()

    private val accesJson = AccesJson("objets", contexte)

    // Ensure that the file exists, otherwise create it with an empty JSON structure
    private fun verifierExistence() {
        if (!accesJson.fichierExiste()) {
            val emptyJson = """{"objets": []}"""
            accesJson.ecrireFichierJson(emptyJson)
        }
    }

    override fun obtenirTous(): List<Objet> {
        verifierExistence()

        val jsonString = accesJson.lireFichierJson()

        val objetsJsonArray = gson.fromJson(jsonString, JsonElement::class.java)
            .asJsonObject.getAsJsonArray("objets")

        val objetListType = object : TypeToken<List<Objet>>() {}.type

        return gson.fromJson(objetsJsonArray, objetListType)
    }

    override fun obtenirParId(id: Int): Objet? {
        val objets = obtenirTous() // Get the list of all objects
        return objets.find { it.getId() == id } // Find the object with the matching ID
    }

    override fun inserer(entite: Objet): Boolean {
        verifierExistence()

        val jsonString = accesJson.lireFichierJson()

        val objetsJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val objetsJsonArray = objetsJson.getAsJsonArray("objets")

        val listeObjets: MutableList<Objet> = gson.fromJson(
            objetsJsonArray,
            object : TypeToken<MutableList<Objet>>() {}.type
        )

        if (listeObjets.any { it.getId() == entite.getId() }) {
            return false // Objet with this ID already exists
        }

        listeObjets.add(entite)
        objetsJson.add("objets", gson.toJsonTree(listeObjets))
        accesJson.ecrireFichierJson(gson.toJson(objetsJson))

        return true
    }

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
