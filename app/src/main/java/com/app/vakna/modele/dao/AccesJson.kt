package com.app.vakna.modele.dao

import android.content.Context

class AccesJson (nomFichier: String, context: Context) {
    private val ctxt = context
    private val cheminFichier = nomFichier + ".json"

    fun ecrireFichierJson( contenu: String) {
        ctxt.openFileOutput(cheminFichier, Context.MODE_PRIVATE).use { output ->
            output.write(contenu.toByteArray())
        }
    }

fun lireFichierJson(): String {
    return ctxt.openFileInput(cheminFichier).bufferedReader().use {
        it.readText()
    }
}

fun fichierExiste(): Boolean {
    val file = ctxt.getFileStreamPath(cheminFichier)
    return file.exists()
}
}