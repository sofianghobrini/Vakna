package com.app.vakna.modele

import com.app.vakna.adapters.ListData
import java.time.LocalDate

class Projet (
    var nom: String,
    var importance: Importance,
    var type: TypeTache,
    var derniereValidation: LocalDate? = null,
    var estTermine: Boolean = false,
    var estArchive: Boolean = false,
    var nbAvancements: Int = 0
) {

    fun toListData(): ListData {
        return ListData(nom, type.name, importance.name, 0, estTermine, estArchive)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Tache) return false
        return nom == other.nom
    }

    override fun hashCode(): Int {
        return nom.hashCode()
    }

    override fun toString(): String {
        return "$nom : $importance $type $derniereValidation (fini $nbAvancements fois) " + if (estTermine) "Terminé" else "En cours" + if (estArchive) " ARCHIVE" else ""
    }
}
