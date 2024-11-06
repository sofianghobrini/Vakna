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
        val modifFaim = refugeJson.get("modifFaim").asInt
        val modifHumeur = refugeJson.get("modifHumeur").asInt
        val modifXp = refugeJson.get("modifXp").asInt
        val modifPieces = refugeJson.get("modifPieces").asInt

        return Refuge(id, nom, modifFaim, modifHumeur, modifXp, modifPieces)
    }
}