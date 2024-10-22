package com.app.vakna.modele.dao

import com.app.vakna.modele.Tache
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/** Permet de convertir un objet Tache en entrée JSON */
class TacheToJson : JsonSerializer<Tache> {
    override fun serialize(
        src: Tache,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val objetJson = JsonObject()

        objetJson.addProperty("nom", src.nom)
        objetJson.addProperty("frequence", src.frequence.name)
        objetJson.addProperty("importance", src.importance.name)
        objetJson.addProperty("type", src.type.name)
        objetJson.addProperty("estTerminee", src.estTerminee)
        objetJson.addProperty("estArchivee", src.estArchivee)
        objetJson.addProperty("derniereValidation", src.derniereValidation.toString())

        return objetJson
    }
}