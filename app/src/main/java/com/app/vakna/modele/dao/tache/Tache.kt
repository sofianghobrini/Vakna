package com.app.vakna.modele.dao.tache

import android.content.Context
import com.app.vakna.R
import com.app.vakna.adapters.ListData
import com.app.vakna.modele.dao.Frequence
import com.app.vakna.modele.dao.Importance
import com.app.vakna.modele.dao.TypeTache
import java.time.LocalDate
import java.time.LocalDateTime

class Tache(
    var nom: String,
    var frequence: Frequence,
    var importance: Importance,
    var type: TypeTache,
    var jours: List<Int>? = null,
    var derniereValidation: LocalDate? = null,
    var prochaineValidation: LocalDateTime? = null,
    var estTerminee: Boolean = false,
    var estArchivee: Boolean = false
) {
    fun toListData(context: Context): ListData {
        val nomType = when(type) {
            TypeTache.PERSONNELLE -> context.getString(R.string.type_personnelle)
            TypeTache.PROFESSIONNELLE -> context.getString(R.string.type_professionnelle)
            TypeTache.PROJET -> context.getString(R.string.type_projet)
            TypeTache.ETUDES -> context.getString(R.string.type_etudes)
            TypeTache.SPORT -> context.getString(R.string.type_sport)
            TypeTache.AUTRE -> context.getString(R.string.type_autre)
        }
        return ListData(nom, nomType, importance.name, 0, estTerminee, estArchivee)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Tache) return false
        return nom == other.nom || type == other.type || importance == other.importance || frequence == other.frequence || derniereValidation == other.derniereValidation
    }

    override fun hashCode(): Int {
        return nom.hashCode()
    }

    override fun toString(): String {
        return "$nom : $frequence $jours $importance $type $derniereValidation " + if (estTerminee) "Finie" else "Pas Finie" + if (estArchivee) " ARCHIVEE" else ""
    }
}
