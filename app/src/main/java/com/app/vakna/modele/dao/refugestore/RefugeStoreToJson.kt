package com.app.vakna.modele.dao.refugestore

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class RefugeStoreToJson : JsonSerializer<RefugeStore> {
    override fun serialize(
        src: RefugeStore,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val refugeJson = JsonObject()

        refugeJson.addProperty("id", src.getId())
        refugeJson.addProperty("nom", src.getNom())
        refugeJson.addProperty("modifFaim", src.getModifFaim())
        refugeJson.addProperty("modifHumeur", src.getModifHumeur())
        refugeJson.addProperty("modifXp", src.getModifXp())
        refugeJson.addProperty("modifPieces", src.getModifPieces())
        refugeJson.addProperty("prix", src.getPrix())

        return refugeJson
    }
}