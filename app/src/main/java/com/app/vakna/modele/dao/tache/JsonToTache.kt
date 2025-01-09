package com.app.vakna.modele.dao.tache

import android.os.Build
import androidx.annotation.RequiresApi
import com.app.vakna.modele.dao.Frequence
import com.app.vakna.modele.dao.Importance
import com.app.vakna.modele.dao.TypeTache
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime

/** Permet de convertir une entrée JSON en objet Tache */
class JsonToTache : JsonDeserializer<Tache> {
    @RequiresApi(Build.VERSION_CODES.O)
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
        val jours = if (objetJson.get("jours") != null) {
            listOf(objetJson.get("jours").asInt)
        } else {
            null
        }
        val derniereValidation = if (objetJson.get("derniereValidation").asString == "null") {
            null
        } else {
            LocalDate.parse(objetJson.get("derniereValidation").asString)
        }
        val prochaineValidation = if (objetJson.get("prochaineValidation").asString == "null") {
            null
        } else {
            LocalDateTime.parse(objetJson.get("prochaineValidation").asString)
        }
        val estTerminee = objetJson.get("estTerminee").asBoolean
        val estArchivee = objetJson.get("estArchivee").asBoolean

        return Tache(nom, frequence, importance, type, jours, derniereValidation, prochaineValidation, estTerminee, estArchivee)
    }
}