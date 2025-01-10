package com.app.vakna.modele.dao

import android.content.Context
import com.app.vakna.modele.Objet
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/** Permet de convertir un objet Objet en entrée JSON */
class ObjetToJson : JsonSerializer<Objet> {
    override fun serialize(
        src: Objet,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val objetJson = JsonObject()

        objetJson.addProperty("id", src.getId())

        val nomJson = JsonObject()
        src.getNom().forEach { (langue, valeur) ->
            nomJson.addProperty(langue, valeur)
        }
        objetJson.add("nom", nomJson)

        objetJson.addProperty("prix", src.getPrix())
        objetJson.addProperty("niveau", src.getNiveau())
        objetJson.addProperty("type", src.getType().name)

        val detailJson = JsonObject()
        src.getDetails().forEach { (langue, valeur) ->
            detailJson.addProperty(langue, valeur)
        }
        objetJson.add("detail", detailJson)

        objetJson.addProperty("imageUrl", src.getImageUrl())

        return objetJson
    }
}
