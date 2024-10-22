package com.app.vakna.modele.dao

import com.app.vakna.modele.Projet
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/** Permet de convertir un objet Projet en entrée JSON */
class ProjetToJson : JsonSerializer<Projet> {
    override fun serialize(
        src: Projet,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val objetJson = JsonObject()

        objetJson.addProperty("nom", src.nom)
        objetJson.addProperty("frequence", src.frequence.name)
        objetJson.addProperty("importance", src.importance.name)
        objetJson.addProperty("type", src.type.name)
        objetJson.addProperty("estTerminee", src.estTermine)
        objetJson.addProperty("estArchivee", src.estArchive)
        objetJson.addProperty("derniereValidation", src.derniereValidation.toString())

        return objetJson
    }
}