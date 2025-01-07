package com.app.vakna.modele.dao

import android.content.Context
import com.app.vakna.modele.dao.objetobtenu.JsonToObjetObtenu
import com.app.vakna.modele.dao.objetobtenu.ObjetObtenu
import com.app.vakna.modele.dao.objetobtenu.ObjetObtenuToJson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

class InventaireDAO(private val contexte: Context) {

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(ObjetObtenu::class.java, JsonToObjetObtenu())
        .registerTypeAdapter(ObjetObtenu::class.java, ObjetObtenuToJson())
        .create()

    private val accesJson = AccesJson("inventaire", contexte)

    // Vérifie si le fichier JSON existe, sinon en crée un nouveau avec des valeurs par défaut
    private fun verifierExistence() {
        if (!accesJson.fichierExiste()) {
            val emptyJson = """{"pieces": 0, "objets_obtenus": []}"""
            accesJson.ecrireFichierJson(emptyJson)
        }
    }

    // Obtenir le nombre de pièces
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
        jsonObject.addProperty("pieces", pieces) // Mettre à jour les pièces
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

    // Obtenir des objets obtenus par type
    fun obtenirParType(type: TypeObjet): List<ObjetObtenu> {
        return obtenirTousObjetsObtenus().filter { it.getType() == type }
    }

    // Insérer un nouvel objet obtenu dans l'inventaire
    fun insererObjetObtenu(entite: ObjetObtenu): Boolean {
        verifierExistence()
        val jsonString = accesJson.lireFichierJson()

        val objetsObtenusJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val objetsObtenusJsonArray = objetsObtenusJson.getAsJsonArray("objets_obtenus")

        val listeObjetsObtenus: MutableList<ObjetObtenu> = gson.fromJson(
            objetsObtenusJsonArray,
            object : TypeToken<MutableList<ObjetObtenu>>() {}.type
        )

        // Vérifier si un inventaire avec le même nom existe déjà
        if (listeObjetsObtenus.any { it.getId() == entite.getId() }) {
            return false
        }

        listeObjetsObtenus.add(entite)
        // Mettre à jour le fichier JSON avec la nouvelle liste d'objets obtenu
        objetsObtenusJson.add("objets_obtenus", gson.toJsonTree(listeObjetsObtenus))
        accesJson.ecrireFichierJson(gson.toJson(objetsObtenusJson))
        return true
    }

    // Supprimer un objet obtenu de l'inventaire
    fun supprimerObjetObtenu(id: Int): Boolean {
        verifierExistence()
        val jsonString = accesJson.lireFichierJson()
        val objetsObtenusJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val objetsObtenusJsonArray = objetsObtenusJson.getAsJsonArray("objets_obtenus")

        val listeObjetsObtenus: MutableList<ObjetObtenu> = gson.fromJson(
            objetsObtenusJsonArray, object : TypeToken<MutableList<ObjetObtenu>>() {}.type
        )

        val objetASupprimer = listeObjetsObtenus.find { it.getId() == id } ?: return false

        listeObjetsObtenus.remove(objetASupprimer)
        objetsObtenusJson.add("objets_obtenus", gson.toJsonTree(listeObjetsObtenus))
        accesJson.ecrireFichierJson(gson.toJson(objetsObtenusJson))

        return true
    }

    // Mettre à jour la quantité d'un objet obtenu
    fun mettreAJourQuantiteObjet(id: Int, quantite: Int): Boolean {
        val objetsObtenus = obtenirTousObjetsObtenus().toMutableList()
        val objetObtenuAModifier = objetsObtenus.find { it.getId() == id } ?: return false

        // Mettre à jour la quantité de l'objet
        objetObtenuAModifier.updateQuantite(quantite - objetObtenuAModifier.getQuantite())

        // Mettre à jour la liste d'objets obtenus et l'enregistrer dans le fichier JSON
        val objetsObtenusJson = gson.toJsonTree(objetsObtenus)
        val jsonObject = gson.fromJson(accesJson.lireFichierJson(), JsonElement::class.java).asJsonObject
        jsonObject.add("objets_obtenus", objetsObtenusJson)

        accesJson.ecrireFichierJson(gson.toJson(jsonObject))

        return true
    }
}
