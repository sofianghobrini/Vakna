package com.app.vakna.modele.dao.refugestore

import android.content.Context
import com.app.vakna.modele.dao.AccesJson
import com.app.vakna.modele.dao.DAO
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

class RefugeStoreDAO(contexte: Context) : DAO<RefugeStore, Int> {

    // Initialisation de l'objet Gson avec un adaptateur personnalisé pour les objets RefugeStore
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(RefugeStore::class.java, RefugeStoreToJson())
        .registerTypeAdapter(RefugeStore::class.java, JsonToRefugeStore())
        .create()

    // Gestion de l'accès aux fichiers JSON
    private val accesJson = AccesJson("refuges_store", contexte)

    // Vérifier l'existence du fichier, sinon en créer un vide
    private fun verifierExistence() {
        if (!accesJson.fichierExiste()) {
            val emptyJson = """{"refuges_store": []}"""
            accesJson.ecrireFichierJson(emptyJson)
        }
    }

    // Obtenir tous les environnements depuis le fichier JSON
    override fun obtenirTous(): List<RefugeStore> {
        verifierExistence()
        val jsonString = accesJson.lireFichierJson()

        val refugesStoreJsonArray = gson.fromJson(jsonString, JsonElement::class.java)
            .asJsonObject.getAsJsonArray("refuges_store")

        val refugeStoreListType = object : TypeToken<List<RefugeStore>>() {}.type
        return gson.fromJson(refugesStoreJsonArray, refugeStoreListType)
    }

    // Obtenir un compagnon par son ID
    override fun obtenirParId(id: Int): RefugeStore? {
        return obtenirTous().find { it.getId() == id }
    }

    // Insérer un nouveau refuge dans les refuges
    override fun inserer(entite: RefugeStore): Boolean {
        verifierExistence()
        val jsonString = accesJson.lireFichierJson()

        val refugeStoreJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val refugeStoreJsonArray = refugeStoreJson.getAsJsonArray("refuges_store")

        val listeRefugesStore: MutableList<RefugeStore> = gson.fromJson(
            refugeStoreJsonArray,
            object : TypeToken<MutableList<RefugeStore>>() {}.type
        )

        // Vérifier si un refuge avec le même nom existe déjà
        if (listeRefugesStore.any { it.getId() == entite.getId() }) {
            return false
        }

        listeRefugesStore.add(entite)
        // Mettre à jour le fichier JSON avec la nouvelle liste de refuges
        refugeStoreJson.add("refuges_store", gson.toJsonTree(listeRefugesStore))
        accesJson.ecrireFichierJson(gson.toJson(refugeStoreJson))
        return true
    }

    // Modifier un refuge existant dans le fichier JSON
    override fun modifier(id: Int, entite: RefugeStore): Boolean {
        val refugesStore = obtenirTous().toMutableList()
        val indexRefugeStoreAModifier = refugesStore.indexOfFirst { it.getId() == id }

        if (indexRefugeStoreAModifier == -1) {
            return false // Object not found
        }

        refugesStore[indexRefugeStoreAModifier] = entite
        val refugesStoreJson = gson.toJsonTree(refugesStore)

        val jsonObject = gson.fromJson(accesJson.lireFichierJson(), JsonElement::class.java).asJsonObject
        jsonObject.add("refuges_store", refugesStoreJson)

        accesJson.ecrireFichierJson(gson.toJson(jsonObject))

        return true
    }

    override fun supprimer(id: Int): Boolean {
        val refugesStore = obtenirTous().toMutableList()
        val refugeStoreToRemove = refugesStore.find { it.getId() == id } ?: return false

        refugesStore.remove(refugeStoreToRemove)
        val refugesStoreJson = gson.toJsonTree(refugesStore)

        val jsonObject = gson.fromJson(accesJson.lireFichierJson(), JsonElement::class.java).asJsonObject
        jsonObject.add("refuges_store", refugesStoreJson)

        accesJson.ecrireFichierJson(gson.toJson(jsonObject))

        return true
    }
}