package com.app.vakna.modele.dao

import com.app.vakna.modele.Compagnon
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

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
        val actif = objetJson.get("actif").asBoolean

        return Compagnon(id, nom, faim, humeur, xp, espece, actif)
    }
}