package com.app.vakna.modele.dao

import com.app.vakna.modele.Compagnon
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.google.gson.TypeAdapter

class CompagnonAdapter : TypeAdapter<Compagnon>() {
    override fun write(out: JsonWriter, compagnon: Compagnon) {
        out.beginObject()
        out.name("id").value(compagnon.id)
        out.name("nom").value(compagnon.nom)
        out.name("faim").value(compagnon.faim)
        out.name("humeur").value(compagnon.humeur)
        out.name("xp").value(compagnon.xp)
        out.name("espece").value(compagnon.espece)
        out.endObject()
    }

    override fun read(`in`: JsonReader): Compagnon {
        var compagnon = Compagnon(id = 0, nom = "", espece = "")

        `in`.beginObject()
        while (`in`.hasNext()) {
            when (`in`.nextName()) {
                "id" -> compagnon.id = `in`.nextInt()
                "nom" -> compagnon.nom = `in`.nextString()
                "faim" -> compagnon.faim = `in`.nextInt()
                "humeur" -> compagnon.humeur = `in`.nextInt()
                "xp" -> compagnon.xp = `in`.nextInt()
                "espece" -> compagnon.espece = `in`.nextString()
                else -> `in`.skipValue()
            }
        }
        `in`.endObject()
        return compagnon
    }
}
