package com.app.vakna.controller

/*import android.view.View
import com.app.vakna.modele.*
import com.app.vakna.modele.dao.*
import java.time.LocalDate

class ControllerGererTache(private val view :View, private val dao:TacheDAO){
    fun recupModifNom():String{

    }
    fun recupNom(): String {

    }
    fun recupModifType() : TypeTache {

    }
    fun recupModifFrequence() :Frequence{

    }
    fun recupModifImportance() :Importance{

    }
    fun modifierTache(){
        val nomTache = recupModifNom()
        if (nomTache.isBlank()) {
            // Afficher un message d'erreur ou retourner une valeur d'erreur
            return
        }
        val typeTache = recupModifType()
        val importanceTache = recupModifImportance()
        val frequenceTache = recupModifFrequence()
        val derniereValidation = LocalDate.now()
        val tache = Tache(nomTache, frequenceTache, importanceTache, typeTache, derniereValidation,false)
        val gestionnaireDeTaches = GestionnaireDeTaches(dao)
        gestionnaireDeTaches.ajouterTache(tache)
    }
    fun supprimerTache() {
        val nomDeLaTache = recupNom()
        val gestionnaireDeTaches = GestionnaireDeTaches(dao)
        gestionnaireDeTaches.supprimerTache(nomDeLaTache)
    }
}*/