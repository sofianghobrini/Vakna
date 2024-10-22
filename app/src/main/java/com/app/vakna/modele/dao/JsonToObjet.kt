package com.app.vakna.modele.dao

import com.app.vakna.modele.Objet
import com.app.vakna.modele.TypeObjet
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/** Permet de convertir une entrée JSON en objet Objet */
class JsonToObjet : JsonDeserializer<Objet> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Objet {
        val objetJson = json.asJsonObject

        val id = objetJson.get("id").asInt
        val nom = objetJson.get("nom").asString
        val prix = objetJson.get("prix").asInt
        val niveau = objetJson.get("niveau").asInt
        val type = TypeObjet.valueOf(objetJson.get("type").asString)
        val detail = objetJson.get("detail").asString

        return Objet(id, nom, prix, niveau, type, detail)
    }
}
