package com.app.vakna.modele.dao

import com.app.vakna.modele.Compagnon
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/** Permet de convertir un objet Tache en entrée JSON */
class CompagnonToJson : JsonSerializer<Compagnon> {
    override fun serialize(
        src: Compagnon,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val objetJson = JsonObject()

        objetJson.addProperty("id", src.id)
        objetJson.addProperty("nom", src.nom)
        objetJson.addProperty("faim", src.faim)
        objetJson.addProperty("humeur", src.humeur)
        objetJson.addProperty("xp", src.xp)
        objetJson.addProperty("espece", src.espece)
        objetJson.addProperty("actif", src.actif)

        return objetJson
    }
}