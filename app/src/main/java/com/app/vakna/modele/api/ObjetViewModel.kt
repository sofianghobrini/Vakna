package com.app.vakna.modele.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.vakna.modele.Objet
import kotlinx.coroutines.launch

class ObjetViewModel: ViewModel() {
    val objets = MutableLiveData<List<Objet>>()

    /*fun fetchObjets() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getObjets()
                if (response.statusCode == 200 && response.data != null) {
                    objets.postValue(response.data)
                    println(response.data)
                } else {
                    Log.e("API_ERROR", "Erreur dans l'appel d'API: ${response.statusMsg}")
                }
            } catch (e: Exception) {
                Log.e("NETWORK_ERROR", "Exception: ${e.message}")
            }
        }
    }*/
}