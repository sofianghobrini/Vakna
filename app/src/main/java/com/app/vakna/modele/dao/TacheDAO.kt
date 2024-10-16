package com.app.vakna.modele.dao

import com.app.vakna.modele.*

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.time.LocalDate

/** Permet de convertir une entrée JSON en objet Tache */
class JsonToTache : JsonDeserializer<Tache> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Tache {
        val objetJson = json.asJsonObject

        val nom = objetJson.get("nom").asString
        val frequence = Frequence.valueOf(objetJson.get("frequence").asString)
        val importance = Importance.valueOf(objetJson.get("importance").asString)
        val type = TypeTache.valueOf(objetJson.get("type").asString)
        val estTerminee = objetJson.get("estTerminee").asBoolean
        val derniereValidation = LocalDate.parse(objetJson.get("derniereValidation").asString)

        return Tache(nom, frequence, importance, type, derniereValidation, estTerminee)
    }
}

/** Permet de convertir un objet Tache en entrée JSON */
class TacheToJson : JsonSerializer<Tache> {
    override fun serialize(
        src: Tache,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val objetJson = JsonObject()

        objetJson.addProperty("nom", src.nom)
        objetJson.addProperty("frequence", src.frequence.name)
        objetJson.addProperty("importance", src.importance.name)
        objetJson.addProperty("type", src.type.name)
        objetJson.addProperty("estTerminee", src.estTerminee)
        objetJson.addProperty("derniereValidation", src.derniereValidation.toString())

        return objetJson
    }
}

class TacheDAO : DAO<Tache, String> {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Tache::class.java, JsonToTache())
        .registerTypeAdapter(Tache::class.java, TacheToJson())
        .create()

    private val cheminFichier = System.getProperty("user.dir")?.plus("/app/src/bdd/tache.json") ?: ""
    val typeTacheList = object : TypeToken<MutableList<Tache>>() {}.type

    override fun obtenirTous(): List<Tache> {
        val jsonString = File(cheminFichier).readText()

        val tachesJsonArray = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
            .getAsJsonArray("taches")

        return gson.fromJson(tachesJsonArray, typeTacheList)
    }

    /** Récupère l'entité avec l'id P associé si elle existe (null sinon) */
    override fun obtenirParId(id: String): Tache? {
        return obtenirTous().find { it.nom == id }
    }

    /** Insère l'entité T dans la BDD */
    override fun inserer(entite : Tache): Boolean {
        val fichierJson = File(cheminFichier)
        val jsonString = fichierJson.readText()

        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val tachesJsonArray = objetJson.getAsJsonArray("taches")


        val listeTaches: MutableList<Tache> = gson.fromJson(tachesJsonArray, typeTacheList)?: mutableListOf()


        if (listeTaches.any { it.nom == entite.nom }) {
            return false
        }

        listeTaches.add(entite)

        objetJson.add("taches", gson.toJsonTree(listeTaches))

        fichierJson.writeText(gson.toJson(objetJson))

        return true
    }

    /** Remplace l'entite avec l'id P par l'entite T */
    override fun modifier(id: String, entite: Tache): Boolean {
        val fichierJson = File(cheminFichier)
        val jsonString = fichierJson.readText()

        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val tachesJsonArray = objetJson.getAsJsonArray("taches")

        val listeTaches: MutableList<Tache> = gson.fromJson(tachesJsonArray, typeTacheList)?: mutableListOf()


        val indexTacheAModifier = listeTaches.indexOfFirst { it.nom == id }

        if (indexTacheAModifier == -1) {
            return false
        }

        listeTaches[indexTacheAModifier] = entite

        objetJson.add("taches", gson.toJsonTree(listeTaches))

        fichierJson.writeText(gson.toJson(objetJson))

        return true
    }

    /** Supprime l'entité avec l'id P associé */
    override fun supprimer(id : String): Boolean {
        val fichierJson = File(cheminFichier)
        val jsonString = fichierJson.readText()

        val objetJson = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val tachesJsonArray = objetJson.getAsJsonArray("taches")

        val listeTaches: MutableList<Tache> = gson.fromJson(tachesJsonArray, typeTacheList)?: mutableListOf()


        val tacheASupprimer = listeTaches.find { it.nom == id } ?: return false
        listeTaches.remove(tacheASupprimer)

        objetJson.add("taches", gson.toJsonTree(listeTaches))

        fichierJson.writeText(gson.toJson(objetJson))

        return true
    }
}