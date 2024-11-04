package com.app.vakna.modele

import java.time.LocalDate

class Projet (
    var nom: String,
    var frequence: Frequence,
    var importance: Importance,
    var type: TypeTache,
    var derniereValidation: LocalDate? = null,
    var estTermine: Boolean = false,
    var estArchive: Boolean = false,
    private val nbFinis: Int = 0
) {

    override fun equals(other: Any?): Boolean {
        if (other !is Tache) return false
        return nom == other.nom
    }

    override fun hashCode(): Int {
        return nom.hashCode()
    }

    override fun toString(): String {
        return "$nom : $frequence $importance $type $derniereValidation (fini $nbFinis fois) " + if (estTermine) "Terminé" else "En cours" + if (estArchive) " ARCHIVE" else ""
    }
}
