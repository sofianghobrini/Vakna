package com.app.vakna.modele.api

import android.content.Context
import android.util.Log
import com.app.vakna.modele.dao.compagnon.Compagnon
import com.app.vakna.modele.dao.compagnon.CompagnonToJson
import com.app.vakna.modele.dao.compagnon.JsonToCompagnon
import com.app.vakna.modele.dao.compagnonstore.CompagnonStore
import com.app.vakna.modele.dao.compagnonstore.CompagnonStoreToJson
import com.app.vakna.modele.dao.compagnonstore.JsonToCompagnonStore
import com.app.vakna.modele.dao.objet.Objet
import com.app.vakna.modele.dao.objet.JsonToObjet
import com.app.vakna.modele.dao.objet.ObjetToJson
import com.app.vakna.modele.dao.refuge.Refuge
import com.app.vakna.modele.dao.refugestore.JsonToRefugeStore
import com.app.vakna.modele.dao.refugestore.RefugeStore
import com.app.vakna.modele.dao.refugestore.RefugeStoreToJson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object RetrofitInstance {
    const val BASE_URL = "https://vakna.boulbicorp.fr/"

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Objet::class.java, JsonToObjet())      // Votre Serializer
        .registerTypeAdapter(Objet::class.java, ObjetToJson())    // Votre Deserializer
        .registerTypeAdapter(CompagnonStore::class.java, JsonToCompagnonStore())      // Votre Serializer
        .registerTypeAdapter(CompagnonStore::class.java, CompagnonStoreToJson())    // Votre Deserializer
        .registerTypeAdapter(RefugeStore::class.java, JsonToRefugeStore())      // Votre Serializer
        .registerTypeAdapter(RefugeStore::class.java, RefugeStoreToJson())    // Votre Deserializer
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

    fun downloadImage(context: Context, fileUrl: String, fileName: String, directory: String) {
        val call = apiService.downloadFile(fileUrl)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        saveFile(context, body, fileName, directory)
                    } ?: Log.e("test","Le corps de la réponse est vide.")
                } else {
                    Log.e("test","Erreur lors du téléchargement : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("test","Échec du téléchargement : ${t.message}")
            }
        })
    }

    private fun saveFile(context: Context, body: ResponseBody, fileName: String, directory: String) {
        try {
            val dir = File(context.filesDir, directory) // Enregistrer dans filesDir
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val file = File(dir, fileName)

            val inputStream: InputStream = body.byteStream()
            val outputStream = FileOutputStream(file)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            Log.d("test","Image téléchargée et sauvegardée dans : ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e("test","Erreur lors de la sauvegarde du fichier : ${e.message}")
        }
    }
}