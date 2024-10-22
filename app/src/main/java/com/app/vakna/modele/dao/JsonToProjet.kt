package com.app.vakna.modele.dao

import com.app.vakna.modele.Frequence
import com.app.vakna.modele.Importance
import com.app.vakna.modele.Projet
import com.app.vakna.modele.TypeTache
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate

/** Permet de convertir une entrée JSON en objet Projet */
class JsonToProjet : JsonDeserializer<Projet> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Projet {
        val objetJson = json.asJsonObject

        val nom = objetJson.get("nom").asString
        val frequence = Frequence.valueOf(objetJson.get("frequence").asString)
        val importance = Importance.valueOf(objetJson.get("importance").asString)
        val type = TypeTache.valueOf(objetJson.get("type").asString)
        val derniereValidation = LocalDate.parse(objetJson.get("derniereValidation").asString)
        val estTermine = objetJson.get("estTermine").asBoolean
        val estArchive = objetJson.get("estArchive").asBoolean
        val nbFinis = objetJson.get("nbFinis").asInt

        return Projet(nom, frequence, importance, type, derniereValidation, estTermine, estArchive, nbFinis)
    }
}