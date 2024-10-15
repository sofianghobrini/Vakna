package com.app.vakna.modele

import java.time.LocalDate

// Enums pour la tâche
enum class Frequence { QUOTIDIENNE, HEBDOMADAIRE, MENSUELLE, ANNUELLE }
enum class Importance { FAIBLE, MOYENNE, ELEVEE }
enum class TypeTache { PERSONNELLE, PROFESSIONNELLE, AUTRE }

class Tache(
    var nom: String,
    var frequence: Frequence,
    var importance: Importance,
    var type: TypeTache,
    var derniereValidation: LocalDate,
    var estTerminee: Boolean
) {
    override fun equals(other: Any?): Boolean{
        if (other !is Tache) return false
        return nom == other.nom
    }

    override fun hashCode(): Int {
        return nom.hashCode()
    }

    override fun toString(): String {
        return "$nom : $frequence $importance $type $derniereValidation " + if(estTerminee)"Finie" else "Pas Finie"
    }
}

class GestionnaireDeTaches() {
    private val setDeTaches = mutableSetOf<Tache>()
    private lateinit var compagnon: Compagnon

    fun setCompagnon(compagnon: Compagnon) {
        this.compagnon = compagnon
    }

    fun ajouterTache(tache: Tache) {
        if (tache.nom.isBlank()) {
            throw IllegalArgumentException("Le nom de la tâche ne peut pas être vide")
        }
        if (!setDeTaches.add(tache)) {
            throw IllegalArgumentException("Une tâche avec le nom '${tache.nom}' existe déjà")
        }
    }

    fun modifierTache(nom: String, nouvelleTache: Tache) {
        val tache = setDeTaches.find { it.nom == nom }
        if (tache != null) {
            tache.nom = nouvelleTache.nom
            tache.frequence = nouvelleTache.frequence
            tache.importance = nouvelleTache.importance
            tache.type = nouvelleTache.type
            tache.derniereValidation = nouvelleTache.derniereValidation
            tache.estTerminee = nouvelleTache.estTerminee
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun supprimerTache(nom: String) {
        val tache = setDeTaches.find { it.nom == nom }
        if (tache != null) {
            if (!tache.estTerminee) {
                tache.estTerminee = true
                compagnon.modifierHumeur(-5 * (tache.importance.ordinal + 1))
                compagnon.gagnerXp(-5 * (tache.importance.ordinal + 1))
            }
            setDeTaches.remove(tache)
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun finirTache(nom: String) {
        val tache = setDeTaches.find { it.nom == nom }
        if (tache != null) {
            tache.estTerminee = true
            compagnon.modifierHumeur(10 * (tache.importance.ordinal + 1))
            compagnon.gagnerXp(5 * (tache.importance.ordinal + 1))
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun obtenirTaches(): Set<Tache> {
        return setDeTaches
    }

    fun obtenirTachesParType(type: TypeTache): Set<Tache> {
        return setDeTaches.filter { it.type == type }.toSet()
    }

    fun obtenirTachesParNom(nom: String): Set<Tache> {
        return setDeTaches.filter { it.nom == nom }.toSet()
    }
}