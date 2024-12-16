package com.app.vakna.modele.dao

import com.app.vakna.modele.Frequence
import com.app.vakna.modele.Importance
import com.app.vakna.modele.Tache
import com.app.vakna.modele.TypeTache
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime

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
        val derniereValidation = LocalDate.parse(objetJson.get("derniereValidation").asString)
        val prochaineValidation = LocalDateTime.parse(objetJson.get("prochaineValidation").asString)
        val estTerminee = objetJson.get("estTerminee").asBoolean
        val estArchivee = objetJson.get("estArchivee").asBoolean

        return Tache(nom, frequence, importance, type, derniereValidation, prochaineValidation, estTerminee, estArchivee)
    }
}