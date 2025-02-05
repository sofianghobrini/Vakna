package com.app.vakna.modele.dao.compagnonstore

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/** permet de convertir un JSON en objet CompanionStore */
class JsonToCompagnonStore : JsonDeserializer<CompagnonStore> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CompagnonStore {
        val objetJson = json.asJsonObject

        val id = objetJson.get("id").asInt
        val espece = objetJson.get("espece").asString
        val prix = objetJson.get("prix").asInt

        return CompagnonStore(id, espece, prix)
    }
}
