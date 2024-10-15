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
    var date: LocalDate,
    var estTerminee: Boolean = false,
    val compagnon: Compagnon,
    private val gestionnaire: GestionnaireDeTaches
)

class GestionnaireDeTaches {
    private val listeDeTaches = mutableListOf<Tache>()

    fun ajouterTache(tache: Tache) {
        listeDeTaches.add(tache)
    }

    fun modifierTache(nom: String, nouvelleTache: Tache) {
        val tache = listeDeTaches.find { it.nom == nom }
        if (tache != null) {
            tache.nom = nouvelleTache.nom
            tache.frequence = nouvelleTache.frequence
            tache.importance = nouvelleTache.importance
            tache.type = nouvelleTache.type
            tache.date = nouvelleTache.date
            tache.estTerminee = nouvelleTache.estTerminee
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun supprimerTache(nom: String) {
        val tache = listeDeTaches.find { it.nom == nom }
        if (tache != null) {
            if (!tache.estTerminee) {
                tache.estTerminee = true
                tache.compagnon.modifierHumeur(5 * tache.importance.ordinal)
                tache.compagnon.gagnerXp(5 * tache.importance.ordinal)
            }
            listeDeTaches.remove(tache)
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun finirTache(nom: String) {
        val tache = listeDeTaches.find { it.nom == nom }
        if (tache != null) {
            tache.estTerminee = true
            tache.compagnon.modifierHumeur(10 * tache.importance.ordinal)
            tache.compagnon.gagnerXp(5 * tache.importance.ordinal)
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun obtenirTaches(): List<Tache> {
        return listeDeTaches
    }

    fun obtenirTachesParType(type: TypeTache): List<Tache> {
        return listeDeTaches.filter { it.type == type }
    }
}
