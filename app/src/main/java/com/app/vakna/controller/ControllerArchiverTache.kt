package com.app.vakna.controller

import android.view.View
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.dao.TacheDAO

class ControllerArchiverTache (private var view: View){
    fun archiverTache(nom: String) {
        GestionnaireDeTaches(view.context).archiverTache(nom)
    }
}