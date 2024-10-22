package com.app.vakna.modele.dao

import com.app.vakna.modele.Objet
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/** Permet de convertir un objet Tache en entrée JSON */
class ObjetToJson : JsonSerializer<Objet> {
    override fun serialize(
        src: Objet,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val objetJson = JsonObject()

        objetJson.addProperty("id", src.getId())
        objetJson.addProperty("nom", src.getNom())
        objetJson.addProperty("prix", src.getPrix())
        objetJson.addProperty("niveau", src.getNiveau())
        objetJson.addProperty("type", src.getType().name)
        objetJson.addProperty("detail", src.getDetails())

        return objetJson
    }
}