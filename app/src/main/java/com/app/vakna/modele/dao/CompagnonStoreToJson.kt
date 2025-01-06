package com.app.vakna.modele.dao

import com.app.vakna.modele.CompagnonStore
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/** Permet de convertir un objet CompanionStore en entrée JSON */
class CompagnonStoreToJson : JsonSerializer<CompagnonStore> {
    override fun serialize(
        src: CompagnonStore,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val objetJson = JsonObject()

        objetJson.addProperty("id", src.id)
        objetJson.addProperty("espece", src.espece)
        objetJson.addProperty("prix", src.prix)

        return objetJson
    }
}
