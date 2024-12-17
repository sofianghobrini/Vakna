package com.app.vakna.modele.dao

import com.app.vakna.modele.Refuge
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/** Permet de convertir un objet Environnement en entrée JSON */
class RefugeToJson : JsonSerializer<Refuge> {
    override fun serialize(
        src: Refuge,
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
        refugeJson.addProperty("actif", src.getActif())

        return refugeJson
    }
}