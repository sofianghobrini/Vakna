package com.app.vakna.modele.dao

import com.app.vakna.modele.Refuge
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/** Permet de convertir un JSON en objet Refuge */
class JsonToRefuge : JsonDeserializer<Refuge> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Refuge {
        val refugeJson = json.asJsonObject

        val id = refugeJson.get("id").asInt
        val nom = refugeJson.get("nom").asString
        val modifFaim = refugeJson.get("modifFaim").asFloat
        val modifHumeur = refugeJson.get("modifHumeur").asFloat
        val modifXp = refugeJson.get("modifXp").asFloat
        val modifPieces = refugeJson.get("modifPieces").asFloat

        return Refuge(id, nom, modifFaim, modifHumeur, modifXp, modifPieces)
    }
}