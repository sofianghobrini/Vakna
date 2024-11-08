package com.app.vakna.modele.api

import com.app.vakna.modele.Objet
import com.app.vakna.modele.dao.JsonToObjet
import com.app.vakna.modele.dao.ObjetToJson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://localhost/vakna_api/"

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Objet::class.java, JsonToObjet())      // Votre Serializer
        .registerTypeAdapter(Objet::class.java, ObjetToJson())    // Votre Deserializer
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}