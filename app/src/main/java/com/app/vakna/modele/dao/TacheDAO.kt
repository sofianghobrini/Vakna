package com.app.vakna.modele.dao

import com.app.vakna.modele.*

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/** Permet de convertir une entree JSON en objet Tache */
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

class TacheDAO : DAO<Tache, String> {
    private val gson = GsonBuilder()
        .registerTypeAdapter(Tache::class.java, JsonToTache())
        .create()

    override fun obtenirTous(): List<Tache> {
        val jsonString = File(System.getProperty("user.dir") + "/app/src/bdd/tache.json").readText()

        val tachesJsonArray = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
            .getAsJsonArray("taches")

        val tacheListType = object : TypeToken<List<Tache>>() {}.type

        return gson.fromJson(tachesJsonArray, tacheListType)
    }

    /** Récupère l'entité avec l'id P associé si elle existe (null sinon) */
    override fun obtenirParId(id: String): Tache? {
        return null
    }

    /** Insère l'entité T dans la BDD */
    override fun inserer(entite : Tache): Boolean {
        return true
    }

    /** Remplace l'entite avec l'id P par l'entite T */
    override fun modifier(entite: Tache, id: String): Boolean {
        return true
    }

    /** Supprime l'entité avec l'id P associé */
    override fun supprimer(id : String): Boolean {
        return true
    }
}