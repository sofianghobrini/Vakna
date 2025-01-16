package com.app.vakna.modele.dao.compagnonstore

import android.content.Context
import com.app.vakna.modele.dao.AccesJson
import com.app.vakna.modele.dao.DAO
import com.app.vakna.modele.dao.objet.Objet
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

class CompagnonStoreDAO(private val contexte: Context) : DAO<CompagnonStore, Int> {

    // Initialisation de l'objet Gson avec des adaptateurs personnalisés pour les objets CompanionStore
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(CompagnonStore::class.java, CompagnonStoreToJson())
        .registerTypeAdapter(CompagnonStore::class.java, JsonToCompagnonStore())
        .create()

    // Gestion de l'accès aux fichiers JSON
    private val accesJson = AccesJson("companions_store", contexte)

    // Vérifie l'existence du fichier, sinon en crée une structure vide
    private fun verifierExistence() {
        if (!accesJson.fichierExiste()) {
            val emptyJson = """{"companions_store": []}"""
            accesJson.ecrireFichierJson(emptyJson)
        }
    }

    // Récupérer tous les articles du magasin de compagnons depuis le fichier JSON
    override fun obtenirTous(): List<CompagnonStore> {
        verifierExistence() // Vérifie l'existence du fichier, sinon le crée

        val jsonString = accesJson.lireFichierJson()
        val companionsJsonArray = gson.fromJson(jsonString, JsonElement::class.java)
            .asJsonObject.getAsJsonArray("companions_store")

        val typeCompagnonStoreList = object : TypeToken<List<CompagnonStore>>() {}.type
        return gson.fromJson(companionsJsonArray, typeCompagnonStoreList)
    }

    // Récupérer un article du magasin de compagnons par ID
    override fun obtenirParId(id: Int): CompagnonStore? {
        return obtenirTous().find { it.id == id }
    }

    // Insérer un nouvel article dans le magasin de compagnons dans le fichier JSON
    override fun inserer(entite: CompagnonStore): Boolean {
        verifierExistence() // Vérifie l'existence du fichier

        val jsonString = accesJson.lireFichierJson()
        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val companionsJsonArray = objetJson.getAsJsonArray("companions_store")

        val typeCompagnonStoreList = object : TypeToken<MutableList<CompagnonStore>>() {}.type
        val listeCompanions: MutableList<CompagnonStore> = gson.fromJson(companionsJsonArray, typeCompagnonStoreList)
            ?: mutableListOf()

        if (listeCompanions.any { it.espece == entite.espece }) {
            return false
        }

        // Génère un ID unique pour le nouvel article du magasin de compagnons
        val nouvelId = (listeCompanions.maxOfOrNull { it.id } ?: 0) + 1
        entite.id = nouvelId

        listeCompanions.add(entite)

        // Met à jour le fichier JSON avec la nouvelle liste d'articles du magasin de compagnons
        objetJson.add("companions_store", gson.toJsonTree(listeCompanions))
        accesJson.ecrireFichierJson(gson.toJson(objetJson))
        return true
    }

    // Modifier un article existant dans le magasin de compagnons dans le fichier JSON
    override fun modifier(id: Int, entite: CompagnonStore): Boolean {
        verifierExistence() // Vérifie l'existence du fichier

        val jsonString = accesJson.lireFichierJson()
        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val companionsJsonArray = objetJson.getAsJsonArray("companions_store")

        val typeCompagnonStoreList = object : TypeToken<MutableList<CompagnonStore>>() {}.type
        val listeCompanions: MutableList<CompagnonStore> = gson.fromJson(companionsJsonArray, typeCompagnonStoreList)
            ?: mutableListOf()

        // Trouver l'index de l'article du magasin de compagnons à modifier
        val indexCompanionToModify = listeCompanions.indexOfFirst { it.id == id }
        if (indexCompanionToModify == -1) {
            return false // Article non trouvé
        }

        // Mettre à jour les données du compagnon
        listeCompanions[indexCompanionToModify] = entite

        // Sauvegarder la liste mise à jour dans le fichier JSON
        objetJson.add("companions_store", gson.toJsonTree(listeCompanions))
        accesJson.ecrireFichierJson(gson.toJson(objetJson))
        return true
    }

    // Supprimer un article du magasin de compagnons par ID
    override fun supprimer(id: Int): Boolean {
        verifierExistence() // Vérifie l'existence du fichier

        val jsonString = accesJson.lireFichierJson()
        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val companionsJsonArray = objetJson.getAsJsonArray("companions_store")

        val typeCompagnonStoreList = object : TypeToken<MutableList<CompagnonStore>>() {}.type
        val listeCompanions: MutableList<CompagnonStore> = gson.fromJson(companionsJsonArray, typeCompagnonStoreList)
            ?: mutableListOf()

        // Trouver l'article du magasin de compagnons à supprimer
        val companionToDelete = listeCompanions.find { it.id == id } ?: return false
        listeCompanions.remove(companionToDelete)

        // Sauvegarder la liste mise à jour dans le fichier JSON
        objetJson.add("companions_store", gson.toJsonTree(listeCompanions))
        accesJson.ecrireFichierJson(gson.toJson(objetJson))
        return true
    }

    fun remplacerCompagnons(nouveauxCompagnons: List<CompagnonStore>) {
        for (comp in obtenirTous()) {
            supprimer(comp.id)
        }
        for (comp in nouveauxCompagnons) {
            inserer(comp)
        }
    }
}
