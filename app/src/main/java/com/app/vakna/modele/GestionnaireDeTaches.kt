package com.app.vakna.modele

import android.content.Context
import com.app.vakna.adapters.ListData
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.TacheDAO
import java.time.LocalDate

class GestionnaireDeTaches(private var context: Context) {
    private var tacheDAO = TacheDAO(context)
    private val setDeTaches = mutableSetOf<Tache>()
    private var gestionnaireCompagnons = GestionnaireDeCompagnons(CompagnonDAO(context))
    private var idCompagnon: Int = 1
    private var inventaire = Inventaire(context)

    init {
        tacheDAO.obtenirTous().forEach { setDeTaches.add(it) }
    }

    fun setCompagnon(id: Int) {
        this.idCompagnon = id
    }

    fun getGestionnaireCompagnon(): GestionnaireDeCompagnons{
        return gestionnaireCompagnons
    }

    fun ajouterTache(tache: Tache): Boolean {
        if (tache.nom.isBlank()) {
            throw IllegalArgumentException("Le nom de la tâche ne peut pas être vide")
        }
        if (!setDeTaches.add(tache)) {
            throw IllegalArgumentException("Une tâche avec le nom '${tache.nom}' existe déjà")
        }
        return tacheDAO.inserer(tache)
    }

    /** Fonction inutile pour le moment
     fun ajouterTaches(taches: List<Tache>): Boolean {
     var toutesInsertionsOK = true
     if (taches.isEmpty()) {
     throw IllegalArgumentException("La liste de tâches à ajouter ne peut pas être vide")
     }
     for (t in taches) {
     ajouterTache(t)
     if (!tacheDAO.inserer(t)) {
     toutesInsertionsOK = false
     }
     }
     return toutesInsertionsOK
     }*/

    fun modifierTache(nom: String, nouvelleTache: Tache): Boolean {
        val tache = setDeTaches.find { it.nom == nom }
        if (tache != null) {
            tache.nom = nouvelleTache.nom
            tache.frequence = nouvelleTache.frequence
            tache.importance = nouvelleTache.importance
            tache.type = nouvelleTache.type
            tache.derniereValidation = nouvelleTache.derniereValidation
            tache.estTerminee = nouvelleTache.estTerminee

            return tacheDAO.modifier(nom, nouvelleTache)
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
                gestionnaireCompagnons.modifierHumeur(
                        idCompagnon,
                        -5 * (tache.importance.ordinal + 1)
                )
                gestionnaireCompagnons.gagnerXp(idCompagnon, -5 * (tache.importance.ordinal + 1))
            }
            setDeTaches.remove(tache)

            return tacheDAO.supprimer(tache.nom)
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun finirTache(nom: String) {
        val tache = setDeTaches.find { it.nom == nom }
        if (tache != null) {
            tache.estTerminee = true
            tache.derniereValidation = LocalDate.now()
            gestionnaireCompagnons.modifierHumeur(idCompagnon, 5 * (tache.importance.ordinal + 1))
            gestionnaireCompagnons.gagnerXp(idCompagnon, 5 * (tache.importance.ordinal + 1))
            inventaire.ajouterPieces(tache.importance.ordinal*5)
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
        tacheDAO.modifier(nom, tache)
    }

    fun archiverTache(nom: String): Boolean {
        val tacheAArchiver = setDeTaches.find { it.nom == nom } ?: return false
        tacheAArchiver.estArchivee = true
        tacheDAO.modifier(nom, tacheAArchiver)
        return true
    }

    fun obtenirTaches(): Set<Tache> {
        tacheDAO.obtenirTous().forEach { setDeTaches.add(it) }
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
            for (tache in taches) {
                val listData = tache.toListData()
                list.add(listData)
            }
            return list
        }
    }
}