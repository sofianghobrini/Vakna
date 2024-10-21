package com.app.vakna.modele.dao

import android.content.Context
import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.Importance
import com.app.vakna.modele.Tache
import com.app.vakna.modele.TypeTache
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDate

/** Permet de convertir un JSON en objet Compagnon */
class JsonToCompagnon : JsonDeserializer<Compagnon> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Compagnon {
        val objetJson = json.asJsonObject

        val id = objetJson.get("id").asInt
        val nom = objetJson.get("nom").asString
        val faim = objetJson.get("faim").asInt
        val humeur = objetJson.get("humeur").asInt
        val xp = objetJson.get("xp").asInt
        val espece = objetJson.get("espece").asString

        return Compagnon(id, nom, faim, humeur, xp, espece)
    }
}

/** Permet de convertir un objet Tache en entrée JSON */
class CompagnonToJson : JsonSerializer<Compagnon> {
    override fun serialize(
        src: Compagnon,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val objetJson = JsonObject()

        objetJson.addProperty("id", src.id)
        objetJson.addProperty("nom", src.nom)
        objetJson.addProperty("faim", src.faim)
        objetJson.addProperty("humeur", src.humeur)
        objetJson.addProperty("xp", src.xp)
        objetJson.addProperty("espece", src.espece)

        return objetJson
    }
}
class CompagnonDAO(private val contexte: Context) : DAO<Compagnon, Int> {

    // Initialisation de l'objet Gson avec un adaptateur personnalisé pour les objets Compagnon
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Compagnon::class.java, CompagnonToJson())
        .registerTypeAdapter(Compagnon::class.java, JsonToCompagnon())
        .create()

    // Gestion de l'accès aux fichiers JSON
    private val accesJson = AccesJson("compagnons", contexte) // Gère l'accès aux fichiers

    // Vérifier l'existence du fichier, sinon en créer un vide
    private fun verifierExistence() {
        if (!accesJson.fichierExiste()) {
            val emptyJson = """{"compagnons": []}""" // Initialise une structure JSON vide
            accesJson.ecrireFichierJson(emptyJson)
        }
    }

    // Obtenir tous les compagnons depuis le fichier JSON
    override fun obtenirTous(): List<Compagnon> {
        verifierExistence() // Vérifier si le fichier existe, sinon le créer

        val jsonString = accesJson.lireFichierJson()

        // Convertir le tableau JSON en liste d'objets Compagnon
        val compagnonsJsonArray = gson.fromJson(jsonString, JsonElement::class.java)
            .asJsonObject.getAsJsonArray("compagnons")

        val typeCompagnonList = object : TypeToken<List<Compagnon>>() {}.type
        return gson.fromJson(compagnonsJsonArray, typeCompagnonList)
    }

    // Obtenir un compagnon par son ID
    override fun obtenirParId(id: Int): Compagnon? {
        return obtenirTous().find { it.id == id }
    }

    // Insérer un nouveau compagnon dans le fichier JSON
    override fun inserer(entite: Compagnon): Boolean {
        verifierExistence() // Vérifier l'existence du fichier

        val jsonString = accesJson.lireFichierJson()

        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val compagnonsJsonArray = objetJson.getAsJsonArray("compagnons")

        val typeCompagnonList = object : TypeToken<MutableList<Compagnon>>() {}.type
        val listeCompagnons: MutableList<Compagnon> = gson.fromJson(compagnonsJsonArray, typeCompagnonList)
            ?: mutableListOf()

        // Vérifier si un compagnon avec le même nom existe déjà
        if (listeCompagnons.any { it.nom == entite.nom }) {
            return false
        }

        // Générer un ID unique pour le nouveau compagnon
        val nouvelId = (listeCompagnons.maxOfOrNull { it.id } ?: 0) + 1
        entite.id = nouvelId

        listeCompagnons.add(entite)

        // Mettre à jour le fichier JSON avec la nouvelle liste de compagnons
        objetJson.add("compagnons", gson.toJsonTree(listeCompagnons))
        accesJson.ecrireFichierJson(gson.toJson(objetJson))
        return true
    }

    // Modifier un compagnon existant dans le fichier JSON
    override fun modifier(id: Int, entite: Compagnon): Boolean {
        verifierExistence() // Vérifier l'existence du fichier

        val jsonString = accesJson.lireFichierJson()
        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val compagnonsJsonArray = objetJson.getAsJsonArray("compagnons")

        val typeCompagnonList = object : TypeToken<MutableList<Compagnon>>() {}.type
        val listeCompagnons: MutableList<Compagnon> = gson.fromJson(compagnonsJsonArray, typeCompagnonList)
            ?: mutableListOf()

        // Trouver l'index du compagnon à modifier
        val indexCompaAModifier = listeCompagnons.indexOfFirst { it.id == id }

        if (indexCompaAModifier == -1) {
            return false // Compagnon non trouvé
        }

        // Mettre à jour les données du compagnon
        listeCompagnons[indexCompaAModifier] = entite

        // Sauvegarder la liste mise à jour dans le fichier JSON
        objetJson.add("compagnons", gson.toJsonTree(listeCompagnons))
        accesJson.ecrireFichierJson(gson.toJson(objetJson))
        return true
    }

    // Supprimer un compagnon par ID
    override fun supprimer(id: Int): Boolean {
        verifierExistence() // Vérifier l'existence du fichier

        val jsonString = accesJson.lireFichierJson()
        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val compagnonsJsonArray = objetJson.getAsJsonArray("compagnons")

        val typeCompagnonList = object : TypeToken<MutableList<Compagnon>>() {}.type
        val listeCompagnons: MutableList<Compagnon> = gson.fromJson(compagnonsJsonArray, typeCompagnonList)
            ?: mutableListOf()

        // Trouver le compagnon à supprimer
        val compagnonASupprimer = listeCompagnons.find { it.id == id } ?: return false

        listeCompagnons.remove(compagnonASupprimer)

        // Sauvegarder la liste mise à jour dans le fichier JSON
        objetJson.add("compagnons", gson.toJsonTree(listeCompagnons))
        accesJson.ecrireFichierJson(gson.toJson(objetJson))
        return true
    }
}
