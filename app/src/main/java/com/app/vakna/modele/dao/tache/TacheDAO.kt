package com.app.vakna.modele.dao.tache

import android.content.Context
import com.app.vakna.modele.dao.AccesJson
import com.app.vakna.modele.dao.DAO

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

class TacheDAO (contexte : Context) : DAO<Tache, String> {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Tache::class.java, JsonToTache())
        .registerTypeAdapter(Tache::class.java, TacheToJson())
        .create()
    private val accesJson = AccesJson("taches", contexte)

    private fun verifierExistence() {
        if (!accesJson.fichierExiste()) {
            val emptyJson = """{"taches": []}"""
            accesJson.ecrireFichierJson(emptyJson)
        }
    }

    override fun obtenirTous(): List<Tache> {

        verifierExistence()

        val jsonString = accesJson.lireFichierJson()

        val tachesJsonArray = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
            .getAsJsonArray("taches")

        val tacheListType = object : TypeToken<List<Tache>>() {}.type

        return gson.fromJson(tachesJsonArray, tacheListType)
    }

    /** Récupère l'entité avec l'id P associé si elle existe (null sinon) */
    override fun obtenirParId(id: String): Tache? {
        val taches = obtenirTous()

        for (t in taches) {
            if (t.nom == id) {
                return t
            }
        }

        return null
    }

    /** Insère l'entité T dans la BDD */
    override fun inserer(entite : Tache): Boolean {
        val jsonString = accesJson.lireFichierJson()


        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val tachesJsonArray = objetJson.getAsJsonArray("taches")

        val typeTacheList = object : TypeToken<MutableList<Tache>>() {}.type
        val listeTaches: MutableList<Tache> = gson.fromJson(tachesJsonArray, typeTacheList)?: mutableListOf()


        if (listeTaches.any { it.nom == entite.nom }) {
            return false
        }

        listeTaches.add(entite)

        objetJson.add("taches", gson.toJsonTree(listeTaches))

        accesJson.ecrireFichierJson(gson.toJson(objetJson))

        return true
    }

    /** Remplace l'entite avec l'id P par l'entite T */
    override fun modifier(id: String, entite: Tache): Boolean {
        val jsonString = accesJson.lireFichierJson()

        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val tachesJsonArray = objetJson.getAsJsonArray("taches")

        val typeTacheList = object : TypeToken<MutableList<Tache>>() {}.type
        val listeTaches: MutableList<Tache> = gson.fromJson(tachesJsonArray, typeTacheList)?: mutableListOf()


        val indexTacheAModifier = listeTaches.indexOfFirst { it.nom == id }

        if (indexTacheAModifier == -1) {
            return false
        }

        listeTaches[indexTacheAModifier] = entite

        objetJson.add("taches", gson.toJsonTree(listeTaches))

        accesJson.ecrireFichierJson(gson.toJson(objetJson))

        return true
    }

    /** Supprime l'entité avec l'id P associé */
    override fun supprimer(id : String): Boolean {
        val jsonString = accesJson.lireFichierJson()

        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val tachesJsonArray = objetJson.getAsJsonArray("taches")

        val typeTacheList = object : TypeToken<MutableList<Tache>>() {}.type
        val listeTaches: MutableList<Tache> = gson.fromJson(tachesJsonArray, typeTacheList)?: mutableListOf()


        val tacheToRemove = listeTaches.find { it.nom == id } ?: return false
        listeTaches.remove(tacheToRemove)

        objetJson.add("taches", gson.toJsonTree(listeTaches))

        accesJson.ecrireFichierJson(gson.toJson(objetJson))

        return true
    }
}