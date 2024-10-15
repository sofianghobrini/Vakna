package com.app.vakna.modele

import com.app.vakna.modele.dao.TacheDAO
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
    private val dao = TacheDAO()

    fun setCompagnon(compagnon: Compagnon) {
        this.compagnon = compagnon
    }

    fun ajouterTache(tache: Tache): Boolean {
        if (tache.nom.isBlank()) {
            throw IllegalArgumentException("Le nom de la tâche ne peut pas être vide")
        }
        if (!setDeTaches.add(tache)) {
            throw IllegalArgumentException("Une tâche avec le nom '${tache.nom}' existe déjà")
        }
        return dao.inserer(tache)
    }

    fun ajouterTache(taches: List<Tache>): Boolean {
        var toutesInsertionsOK = true
        if (taches.isEmpty()) {
            throw IllegalArgumentException("La liste de tâches à ajouter ne peut pas être vide")
        }
        for (t in taches) {
            ajouterTache(t)
            if (!dao.inserer(t)) {
                toutesInsertionsOK = false
            }
        }
        return toutesInsertionsOK
    }

    fun modifierTache(nom: String, nouvelleTache: Tache): Boolean {
        val tache = setDeTaches.find { it.nom == nom }
        if (tache != null) {
            tache.nom = nouvelleTache.nom
            tache.frequence = nouvelleTache.frequence
            tache.importance = nouvelleTache.importance
            tache.type = nouvelleTache.type
            tache.derniereValidation = nouvelleTache.derniereValidation
            tache.estTerminee = nouvelleTache.estTerminee

            return dao.modifier(nom, nouvelleTache)
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun supprimerTache(nom: String): Boolean {
        val tache = setDeTaches.find { it.nom == nom }
        if (tache != null) {
            if (!tache.estTerminee) {
                tache.estTerminee = true
                compagnon.modifierHumeur(-5 * (tache.importance.ordinal + 1))
                compagnon.gagnerXp(-5 * (tache.importance.ordinal + 1))
            }
            setDeTaches.remove(tache)

            return dao.supprimer(tache.nom)
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


    fun obtenirTache(type: TypeTache): Set<Tache> {
        return setDeTaches.filter { it.type == type }.toSet()
    }

    fun obtenirTache(nom: String): Set<Tache> {
        return setDeTaches.filter { it.nom == nom }.toSet()
    }
}