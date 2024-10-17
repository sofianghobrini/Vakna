package com.app.vakna.modele.dao

import com.app.vakna.modele.Compagnon
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.io.File

class CompagnonDAO : DAO<Compagnon, Int> {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val cheminFichier = System.getProperty("user.dir")?.plus("/app/src/bdd/compagnon.json") ?: ""
    private val typeCompagnonList = object : TypeToken<MutableList<Compagnon>>() {}.type

    override fun obtenirTous(): List<Compagnon> {
        val fichier = File(cheminFichier).readText()

        val compagnonsJsonArray = gson.fromJson(fichier, JsonElement::class.java)
            .asJsonObject.getAsJsonArray("compagnons")
        return gson.fromJson(compagnonsJsonArray, typeCompagnonList)
    }

    override fun obtenirParId(id: Int): Compagnon? {
        return obtenirTous().find { it.id == id }
    }

    override fun inserer(entite: Compagnon): Boolean {
        val fichier = File(cheminFichier)

        val objetJson = gson.fromJson(fichier.readText(), JsonElement::class.java).asJsonObject
        val compagnonsJsonArray = objetJson.getAsJsonArray("compagnons")

        val listeCompagnons: MutableList<Compagnon> = gson.fromJson(compagnonsJsonArray, typeCompagnonList)
            ?: mutableListOf()

        // Vérification si un compagnon avec cet ID existe déjà
        if (listeCompagnons.any { it.id == entite.id }) {
            return false
        }

        // Génération d'un nouvel id unique pour chaque compagnon
        val nouvelId = (listeCompagnons.maxOfOrNull { it.id } ?: 0) + 1
        entite.id = nouvelId

        listeCompagnons.add(entite)

        objetJson.add("compagnons", gson.toJsonTree(listeCompagnons))
        fichier.writeText(gson.toJson(objetJson))
        return true
    }

    override fun modifier(id: Int, entite: Compagnon): Boolean {
        val fichier = File(cheminFichier)

        val objetJson = gson.fromJson(fichier.readText(), JsonElement::class.java).asJsonObject
        val compagnonsJsonArray = objetJson.getAsJsonArray("compagnons")

        val listeCompagnons: MutableList<Compagnon> = gson.fromJson(compagnonsJsonArray, typeCompagnonList)
            ?: mutableListOf()

        val indexCompaAModifier = listeCompagnons.indexOfFirst { it.id == id }

        if (indexCompaAModifier == -1) {
            return false
        }

        listeCompagnons[indexCompaAModifier] = entite

        objetJson.add("compagnons", gson.toJsonTree(listeCompagnons))
        fichier.writeText(gson.toJson(objetJson))
        return true
    }

    override fun supprimer(id: Int): Boolean {
        val fichier = File(cheminFichier)

        val objetJson = gson.fromJson(fichier.readText(), JsonElement::class.java).asJsonObject
        val compagnonsJsonArray = objetJson.getAsJsonArray("compagnons")

        val listeCompagnons: MutableList<Compagnon> = gson.fromJson(compagnonsJsonArray, typeCompagnonList)
            ?: mutableListOf()

        val compagnonASupprimer = listeCompagnons.find { it.id == id } ?: return false

        listeCompagnons.remove(compagnonASupprimer)

        objetJson.add("compagnons", gson.toJsonTree(listeCompagnons))
        fichier.writeText(gson.toJson(objetJson))
        return true
    }
}
