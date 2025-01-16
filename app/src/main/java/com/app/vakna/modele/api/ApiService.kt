package com.app.vakna.modele.api

import com.app.vakna.modele.dao.compagnonstore.CompagnonStore
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.dao.refuge.Refuge
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET
    fun downloadFile(@Url fileUrl: String): Call<ResponseBody>

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



    @GET("refuges.php")
    suspend fun obtenirRefuges(): List<Refuge>

    @GET("refuges.php")
    suspend fun obtenirRefuge(@Query("id") id: Int): Refuge

    @POST("refuges.php")
    suspend fun creerRefuge(@Body refuge: Refuge): Refuge

    @PATCH("refuges.php")
    suspend fun modifierRefuge(@Query("id") id: Int, @Body refuge: Refuge): Refuge

    @DELETE("refuges.php")
    suspend fun supprimerRefuge(@Query("id") id: Int): Refuge



    @GET("compagnons.php")
    suspend fun obtenirCompagnons(): List<CompagnonStore>

    @GET("compagnons.php")
    suspend fun obtenirCompagnon(@Query("id") id: Int): CompagnonStore

    @POST("compagnons.php")
    suspend fun creerCompagnon(@Body compagnon: CompagnonStore): CompagnonStore

    @PATCH("compagnons.php")
    suspend fun modifierCompagnon(@Query("id") id: Int, @Body compagnon: CompagnonStore): CompagnonStore

    @DELETE("compagnons.php")
    suspend fun supprimerCompagnon(@Query("id") id: Int): CompagnonStore
}