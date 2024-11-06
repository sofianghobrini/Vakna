package com.app.vakna.modele.dao

import com.app.vakna.modele.Refuge
import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

class RefugeDAO(contexte: Context) : DAO<Refuge, Int> {

    // Initialisation de l'objet Gson avec un adaptateur personnalisé pour les objets Refuge
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Refuge::class.java, RefugeToJson())
        .registerTypeAdapter(Refuge::class.java, JsonToRefuge())
        .create()

    // Gestion de l'accès aux fichiers JSON
    private val accesJson = AccesJson("refuges", contexte)

    // Vérifier l'existence du fichier, sinon en créer un vide
    private fun verifierExistence() {
        if (!accesJson.fichierExiste()) {
            val emptyJson = """{"refuges": []}"""
            accesJson.ecrireFichierJson(emptyJson)
        }
    }

    // Obtenir tous les environnements depuis le fichier JSON
    override fun obtenirTous(): List<Refuge> {
        verifierExistence()
        val jsonString = accesJson.lireFichierJson()

        val refugesJsonArray = gson.fromJson(jsonString, JsonElement::class.java)
            .asJsonObject.getAsJsonArray("refuges")

        val refugeListType = object : TypeToken<List<Refuge>>() {}.type
        return gson.fromJson(refugesJsonArray, refugeListType)
    }

    // Obtenir un compagnon par son ID
    override fun obtenirParId(id: Int): Refuge? {
        return obtenirTous().find { it.getId() == id }
    }

    // Insérer un nouveau refuge dans les refuges
    override fun inserer(entite: Refuge): Boolean {
        verifierExistence()
        val jsonString = accesJson.lireFichierJson()

        val refugeJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val refugeJsonArray = refugeJson.getAsJsonArray("refuges")

        val listeRefuges: MutableList<Refuge> = gson.fromJson(
            refugeJsonArray,
            object : TypeToken<MutableList<Refuge>>() {}.type
        )

        // Vérifier si un refuge avec le même nom existe déjà
        if (listeRefuges.any { it.getId() == entite.getId() }) {
            return false
        }

        listeRefuges.add(entite)
        // Mettre à jour le fichier JSON avec la nouvelle liste de refuges
        refugeJson.add("refuges", gson.toJsonTree(listeRefuges))
        accesJson.ecrireFichierJson(gson.toJson(refugeJson))
        return true
    }

    // Modifier un refuge existant dans le fichier JSON
    override fun modifier(id: Int, entite: Refuge): Boolean {
        val refuges = obtenirTous().toMutableList()
        val indexRefugeAModifier = refuges.indexOfFirst { it.getId() == id }

        if (indexRefugeAModifier == -1) {
            return false // Object not found
        }

        refuges[indexRefugeAModifier] = entite
        val refugesJson = gson.toJsonTree(refuges)

        val jsonObject = gson.fromJson(accesJson.lireFichierJson(), JsonElement::class.java).asJsonObject
        jsonObject.add("refuges", refugesJson)

        accesJson.ecrireFichierJson(gson.toJson(jsonObject))

        return true
    }

    override fun supprimer(id: Int): Boolean {
        val refuges = obtenirTous().toMutableList()
        val refugeToRemove = refuges.find { it.getId() == id } ?: return false

        refuges.remove(refugeToRemove)
        val refugesJson = gson.toJsonTree(refuges)

        val jsonObject = gson.fromJson(accesJson.lireFichierJson(), JsonElement::class.java).asJsonObject
        jsonObject.add("refuges", refugesJson)

        accesJson.ecrireFichierJson(gson.toJson(jsonObject))

        return true
    }
}