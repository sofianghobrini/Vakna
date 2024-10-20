package com.app.vakna.modele

import android.util.Log
import com.app.vakna.adapters.ListData
import com.app.vakna.modele.dao.TacheDAO
import java.time.LocalDate

// Enums pour la tâche
enum class Frequence { QUOTIDIENNE, HEBDOMADAIRE, MENSUELLE, ANNUELLE }
enum class Importance { FAIBLE, MOYENNE, ELEVEE }
enum class TypeTache { PERSONNELLE, PROFESSIONNELLE, PROJET, ETUDES, SPORT, VIEQUO, AUTRE }

class Tache(
    var nom: String,
    var frequence: Frequence,
    var importance: Importance,
    var type: TypeTache,
    var derniereValidation: LocalDate,
    var estTerminee: Boolean
) {
    fun toListData(): ListData {
        return ListData(nom, type.name, importance.name, 0, estTerminee)
    }

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

class GestionnaireDeTaches(tacheDAO : TacheDAO) {
    private val setDeTaches = mutableSetOf<Tache>()
    private lateinit var compagnon: Compagnon
    private var dao = tacheDAO

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

    fun ajouterTaches(taches: List<Tache>): Boolean {
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
                tache.derniereValidation = LocalDate.now()
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
            tache.derniereValidation = LocalDate.now()
            compagnon.modifierHumeur(10 * (tache.importance.ordinal + 1))
            compagnon.gagnerXp(5 * (tache.importance.ordinal + 1))
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
        dao.modifier(nom, tache)
    }

    fun obtenirTaches(): Set<Tache> {
        dao.obtenirTous().forEach { setDeTaches.add(it) }
        return setDeTaches
    }


    fun obtenirTaches(type: TypeTache): Set<Tache> {
        return obtenirTaches().filter { it.type == type }.toSet()
    }

    fun obtenirTaches(nom: String): Set<Tache> {
        return obtenirTaches().filter { it.nom.contains(nom) }.toSet()
    }

    fun obtenirTaches(frequence: Frequence): Set<Tache> {
        return obtenirTaches().filter { it.frequence == frequence }.toSet()
    }
    fun obtenirTachesParFrequence(): Map<Frequence, Set<Tache>> {
        return obtenirTaches().groupBy { it.frequence }.mapValues { it.value.toSet() }
    }

    fun obtenirTachesParImportance(): Map<Importance, Set<Tache>> {
        return obtenirTaches().groupBy { it.importance }.mapValues { it.value.toSet() }
    }

    fun rechercherTache(nom: String): Set<Tache> {
        return obtenirTaches().filter { it.nom.contains(nom) }.toSet()
    }

    companion object {
        fun setToListDataArray(taches: Set<Tache>): ArrayList<ListData> {
            val list = ArrayList<ListData>()
            for( tache in taches) {
                val listData = tache.toListData()
                list.add(listData)
            }
            return list
        }
    }
}