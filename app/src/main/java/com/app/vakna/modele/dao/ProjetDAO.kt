package com.app.vakna.modele.dao

import android.content.Context
import com.app.vakna.modele.*

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

class ProjetDAO (contexte : Context) : DAO<Projet, String> {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Projet::class.java, JsonToProjet())
        .registerTypeAdapter(Projet::class.java, ProjetToJson())
        .create()
    private val accesJson = AccesJson("projets", contexte)
    private val projetListType = object : TypeToken<List<Projet>>() {}.type

    private fun verifierExistence() {
        if (!accesJson.fichierExiste()) {
            val emptyJson = """{"projets": []}"""
            accesJson.ecrireFichierJson(emptyJson)
        }
    }

    override fun obtenirTous(): List<Projet> {

        verifierExistence()

        val jsonString = accesJson.lireFichierJson()

        val projetJsonArray = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
            .getAsJsonArray("projets")

        return gson.fromJson(projetJsonArray, projetListType)
    }

    /** Récupère l'entité avec l'id P associé si elle existe (null sinon) */
    override fun obtenirParId(id: String): Projet? {
        val projets = obtenirTous()

        for (t in projets) {
            if (t.nom == id) {
                return t
            }
        }

        return null
    }

    /** Insère l'entité T dans la BDD */
    override fun inserer(entite : Projet): Boolean {
        val jsonString = accesJson.lireFichierJson()


        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val projetsJsonArray = objetJson.getAsJsonArray("projets")

        val listeProjets: MutableList<Projet> = gson.fromJson(projetsJsonArray, projetListType)?: mutableListOf()


        if (listeProjets.any { it.nom == entite.nom }) {
            return false
        }

        listeProjets.add(entite)

        objetJson.add("projets", gson.toJsonTree(listeProjets))

        accesJson.ecrireFichierJson(gson.toJson(objetJson))

        return true
    }

    /** Remplace l'entite avec l'id P par l'entite T */
    override fun modifier(id: String, entite: Projet): Boolean {
        val jsonString = accesJson.lireFichierJson()

        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val projetsJsonArray = objetJson.getAsJsonArray("projets")

        val listeProjets: MutableList<Projet> = gson.fromJson(projetsJsonArray, projetListType)?: mutableListOf()


        val indexProjetAModifier = listeProjets.indexOfFirst { it.nom == id }

        if (indexProjetAModifier == -1) {
            return false
        }

        listeProjets[indexProjetAModifier] = entite

        objetJson.add("projets", gson.toJsonTree(listeProjets))

        accesJson.ecrireFichierJson(gson.toJson(objetJson))

        return true
    }

    /** Supprime l'entité avec l'id P associé */
    override fun supprimer(id : String): Boolean {
        val jsonString = accesJson.lireFichierJson()

        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val projetsJsonArray = objetJson.getAsJsonArray("projets")

        val listeProjets: MutableList<Projet> = gson.fromJson(projetsJsonArray, projetListType)?: mutableListOf()


        val projetASupprimer = listeProjets.find { it.nom == id } ?: return false
        listeProjets.remove(projetASupprimer)

        objetJson.add("projets", gson.toJsonTree(listeProjets))

        accesJson.ecrireFichierJson(gson.toJson(objetJson))

        return true
    }
}