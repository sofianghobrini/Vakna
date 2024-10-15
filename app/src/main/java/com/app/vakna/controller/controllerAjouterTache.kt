package com.app.vakna.controller
import android.widget.EditText
import android.view.View
import com.app.vakna.R

@Controller
class controllerCreationTache(private val view: View){
    fun recupererDonneeTache(){
        val nomTacheEditText = view.findViewById<EditText>(R.id.editTextNomTache)
        val nomTache = nomTacheEditText.text.toString()
    }
}

annotation class Controller
