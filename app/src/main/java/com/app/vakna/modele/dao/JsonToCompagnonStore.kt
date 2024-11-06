package com.app.vakna.modele.dao

import com.app.vakna.modele.CompanionStore
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/** permet de convertir un JSON en objet CompanionStore */
class JsonToCompagnonStore : JsonDeserializer<CompanionStore> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CompanionStore {
        val objetJson = json.asJsonObject

        val id = objetJson.get("id").asInt
        val nom = objetJson.get("nom").asString
        val espece = objetJson.get("espece").asString
        val prix = objetJson.get("prix").asInt

        return CompanionStore(id, nom, espece, prix)
    }
}
