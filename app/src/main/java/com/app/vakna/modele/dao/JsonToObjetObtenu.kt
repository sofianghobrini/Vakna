package com.app.vakna.modele.dao

import com.app.vakna.modele.ObjetObtenu
import com.app.vakna.modele.TypeObjet
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/** Permet de convertir une entrée JSON en objet ObjetObtenu */
class JsonToObjetObtenu : JsonDeserializer<ObjetObtenu> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ObjetObtenu {
        val objetJson = json.asJsonObject

        val id = objetJson.get("id").asInt
        val nom = objetJson.get("nom").asString
        val prix = objetJson.get("prix").asInt
        val niveau = objetJson.get("niveau").asInt
        val type = TypeObjet.valueOf(objetJson.get("type").asString)
        val detail = objetJson.get("detail").asString
        val quantite = objetJson.get("quantite").asInt
        val imageUrl = objetJson.get("imageUrl").asString

        return ObjetObtenu(id, nom, prix, niveau, type, detail, quantite, imageUrl)
    }
}
