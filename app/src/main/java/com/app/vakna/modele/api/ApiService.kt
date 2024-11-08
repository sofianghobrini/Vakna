package com.app.vakna.modele.api

import com.app.vakna.modele.Objet
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("objets.php")
    suspend fun obtenirObjets(): List<Objet>

    @GET("objets.php")
    suspend fun obtenirObjet(@Query("id") id: Int): Objet

    @POST("objets.php")
    suspend fun creerObjet(@Body objet: Objet): Objet

    @PATCH("objets.php")
    suspend fun modifierObjet(@Query("id") id: Int, @Body objet: Objet): Objet

    @DELETE("objets.php")
    suspend fun supprimerObjet(@Query("id") id: Int): Objet
}