package com.app.vakna.modele.dao.objetobtenu

import com.app.vakna.modele.dao.TypeObjet
import android.content.Context
import com.app.vakna.modele.ObjetObtenu
import com.app.vakna.modele.TypeObjet
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/** Permet de convertir une entrée JSON en objet ObjetObtenu */
class JsonToObjetObtenu (private val androidContext: Context) : JsonDeserializer<ObjetObtenu> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ObjetObtenu {
        val objetJson = json.asJsonObject

        val id = objetJson.get("id").asInt
        val nom = context.deserialize<Map<String, String>>(
            objetJson.get("nom"), object: TypeToken<Map<String, String>>() {}.type
        )
        val prix = objetJson.get("prix").asInt
        val niveau = objetJson.get("niveau").asInt
        val type = TypeObjet.valueOf(objetJson.get("type").asString)
        val detail = context.deserialize<Map<String, String>>(
            objetJson.get("detail"), object: TypeToken<Map<String, String>>() {}.type
        )
        val quantite = objetJson.get("quantite").asInt
        val imageUrl = objetJson.get("imageUrl").asString

        return ObjetObtenu(id, nom, prix, niveau, type, detail, quantite, imageUrl, androidContext)
    }
}
