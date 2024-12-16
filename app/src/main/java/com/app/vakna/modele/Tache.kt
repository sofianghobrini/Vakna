package com.app.vakna.modele

import com.app.vakna.adapters.ListData
import java.time.LocalDate
import java.time.LocalDateTime

class Tache(
    var nom: String,
    var frequence: Frequence,
    var importance: Importance,
    var type: TypeTache,
    var derniereValidation: LocalDate? = null,
    var prochaineValidation: LocalDateTime? = null,
    var estTerminee: Boolean = false,
    var estArchivee: Boolean = false
) {
    fun toListData(): ListData {
        return ListData(nom, type.name, importance.name, 0, estTerminee, estArchivee)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Tache) return false
        return nom == other.nom || type == other.type || importance == other.importance || frequence == other.frequence || derniereValidation == other.derniereValidation
    }

    override fun hashCode(): Int {
        return nom.hashCode()
    }

    override fun toString(): String {
        return "$nom : $frequence $importance $type $derniereValidation " + if (estTerminee) "Finie" else "Pas Finie" + if (estArchivee) " ARCHIVEE" else ""
    }
}
