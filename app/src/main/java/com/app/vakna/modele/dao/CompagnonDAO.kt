package com.app.vakna.modele.dao

import com.app.vakna.modele.Compagnon
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.io.File

class CompagnonDAO : DAO<Compagnon, String> {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val cheminFichier = System.getProperty("user.dir")?.plus("/app/src/bdd/compagnon.json") ?: ""
    val typeCompagnonList = object : TypeToken<MutableList<Compagnon>>() {}.type

    override fun obtenirTous(): List<Compagnon> {
        val fichier = File(cheminFichier).readText()

        val compagnonsJsonArray = gson.fromJson(fichier, JsonElement::class.java).asJsonObject.getAsJsonArray("compagnons")
        return gson.fromJson(compagnonsJsonArray
            , typeCompagnonList)
    }

    override fun obtenirParId(id: String): Compagnon? {
        return obtenirTous().find { it.nom == id }
    }

    override fun inserer(entite: Compagnon): Boolean {
        val fichier = File(cheminFichier)

        val objetJson = gson.fromJson(fichier.readText(), JsonElement::class.java).asJsonObject
        val compagnonsJsonArray = objetJson.getAsJsonArray("compagnons")

        val listeCompagnons : MutableList<Compagnon> = gson.fromJson(compagnonsJsonArray, typeCompagnonList)?: mutableListOf()

        if (listeCompagnons.any { it.nom == entite.nom }) {
            return false
        }

        listeCompagnons.add(entite)

        objetJson.add("compagnons", gson.toJsonTree(listeCompagnons))
        fichier.writeText(gson.toJson(objetJson))
        return true
    }

    override fun modifier(id: String, entite: Compagnon): Boolean {
        val fichier = File(cheminFichier)

        val objetJson = gson.fromJson(fichier.readText(), JsonElement::class.java).asJsonObject
        val compagnonsJsonArray = objetJson.getAsJsonArray("compagnons")

        val listeCompagnons : MutableList<Compagnon> = gson.fromJson(compagnonsJsonArray, typeCompagnonList)?: mutableListOf()

        val indexCompaAModifier = listeCompagnons.indexOfFirst { it.nom == id }

        if (indexCompaAModifier == -1) {
            return false
        }

        listeCompagnons[indexCompaAModifier] = entite

        objetJson.add("compagnons", gson.toJsonTree(listeCompagnons))
        fichier.writeText(gson.toJson(objetJson))
        return true
    }

    override fun supprimer(id: String): Boolean {
        val fichier = File(cheminFichier)

        val objetJson = gson.fromJson(fichier.readText(), JsonElement::class.java).asJsonObject
        val compagnonsJsonArray = objetJson.getAsJsonArray("compagnons")

        val listeCompagnons : MutableList<Compagnon> = gson.fromJson(compagnonsJsonArray, typeCompagnonList)?: mutableListOf()

        val compagnonASupprimer = listeCompagnons.find { it.nom == id }?: return false

        listeCompagnons.remove(compagnonASupprimer)

        objetJson.add("compagnons", gson.toJsonTree(listeCompagnons))
        fichier.writeText(gson.toJson(objetJson))
        return true
    }

}