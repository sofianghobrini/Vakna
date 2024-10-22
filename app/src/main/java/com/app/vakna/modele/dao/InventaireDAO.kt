package com.app.vakna.modele.dao

import android.content.Context
import com.app.vakna.modele.Objet
import com.app.vakna.modele.ObjetObtenu
import com.app.vakna.modele.TypeObjet
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

class InventaireDAO(private val contexte: Context) {

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val accesJson = AccesJson("inventaire", contexte)

    private fun verifierExistence() {
        if (!accesJson.fichierExiste()) {
            val emptyJson = """{"pieces": 0, "objets_obtenus": []}"""
            accesJson.ecrireFichierJson(emptyJson)
        }
    }

    // Optenir le nombre de pièces
    fun obtenirPieces(): Int {
        verifierExistence()

        val jsonString = accesJson.lireFichierJson()
        val jsonObject = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject

        return jsonObject.get("pieces").asInt
    }

    // Mettre à jour le nombre de pièces
    fun mettreAJourPieces(pieces: Int) {
        verifierExistence()

        val jsonString = accesJson.lireFichierJson()
        val jsonObject = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject

        jsonObject.addProperty("pieces", pieces) // Update pieces

        accesJson.ecrireFichierJson(gson.toJson(jsonObject))
    }

    // Obtenir tous les objets obtenus
    fun obtenirTousObjetsObtenus(): List<ObjetObtenu> {
        verifierExistence()

        val jsonString = accesJson.lireFichierJson()
        val objetsObtenusJsonArray = gson.fromJson(jsonString, JsonElement::class.java)
            .asJsonObject.getAsJsonArray("objets_obtenus")

        val objetObtenuListType = object : TypeToken<List<ObjetObtenu>>() {}.type

        return gson.fromJson(objetsObtenusJsonArray, objetObtenuListType)
    }

    // Obtenir un objet obtenu par son ID
    fun obtenirParType(type: TypeObjet): List<Objet> {
        return obtenirTousObjetsObtenus().filter { it.getType() == type }
    }

    // Insérer un objet obtenu
    fun insererObjetObtenu(entite: ObjetObtenu): Boolean {
        verifierExistence()

        val jsonString = accesJson.lireFichierJson()
        val objetsObtenusJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val objetsObtenusJsonArray = objetsObtenusJson.getAsJsonArray("objets_obtenus")

        val listeObjetsObtenus: MutableList<ObjetObtenu> = gson.fromJson(
            objetsObtenusJsonArray, object : TypeToken<MutableList<ObjetObtenu>>() {}.type
        )

        if (listeObjetsObtenus.any { it.getId() == entite.getId() }) {
            return false // Objet obtenu déjà existant
        }

        listeObjetsObtenus.add(entite)
        objetsObtenusJson.add("objets_obtenus", gson.toJsonTree(listeObjetsObtenus))
        accesJson.ecrireFichierJson(gson.toJson(objetsObtenusJson))

        return true
    }

    fun mettreAJourQuantiteObjet(id: Int, quantite: Int): Boolean {
        val objetsObtenus = obtenirTousObjetsObtenus().toMutableList()
        val objetObtenuAModifier = objetsObtenus.find { it.getId() == id } ?: return false

        objetObtenuAModifier.updateQuantite(quantite - objetObtenuAModifier.getQuantite())

        val objetsObtenusJson = gson.toJsonTree(objetsObtenus)

        val jsonObject = gson.fromJson(accesJson.lireFichierJson(), JsonElement::class.java).asJsonObject
        jsonObject.add("objets_obtenus", objetsObtenusJson)

        accesJson.ecrireFichierJson(gson.toJson(jsonObject))

        return true
    }
}
