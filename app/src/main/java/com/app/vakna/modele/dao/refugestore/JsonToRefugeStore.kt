package com.app.vakna.modele.dao.refugestore

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class JsonToRefugeStore : JsonDeserializer<RefugeStore> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): RefugeStore {
        val refugeJson = json.asJsonObject

        val id = refugeJson.get("id").asInt
        val nom = refugeJson.get("nom").asString
        val modifFaim = refugeJson.get("modifFaim").asFloat
        val modifHumeur = refugeJson.get("modifHumeur").asFloat
        val modifXp = refugeJson.get("modifXp").asFloat
        val modifPieces = refugeJson.get("modifPieces").asFloat
        val prix = refugeJson.get("prix").asInt
        return RefugeStore(id, nom, modifFaim, modifHumeur, modifXp, modifPieces, prix)
    }
}