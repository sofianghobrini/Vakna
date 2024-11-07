package com.app.vakna.modele

import android.content.Context
import com.app.vakna.adapters.ListData
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.TacheDAO
import java.time.LocalDate
import kotlin.properties.Delegates

class GestionnaireDeTaches(context: Context) {
    private var tacheDAO = TacheDAO(context)
    private val setDeTaches = mutableSetOf<Tache>()
    private var gestionnaireCompagnons = GestionnaireDeCompagnons(CompagnonDAO(context))
    private var idCompagnon: Int = 1
    private var inventaire = Inventaire(context)

    init {
        tacheDAO.obtenirTous().forEach { setDeTaches.add(it) }
        var compagnon = gestionnaireCompagnons.obtenirActif()
        if(compagnon == null) {
            compagnon = gestionnaireCompagnons.obtenirCompagnons().first()
        }
        idCompagnon = compagnon.id
    }

    fun setCompagnon(id: Int) {
        this.idCompagnon = id
    }

    fun getGestionnaireCompagnon(): GestionnaireDeCompagnons {
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
            gestionnaireCompagnons.modifierHumeur(idCompagnon, (tache.importance.ordinal + 1) * (tache.frequence.ordinal + 1) * 3)
            gestionnaireCompagnons.gagnerXp(idCompagnon, (tache.importance.ordinal + 1) * (tache.frequence.ordinal + 1) * 3)
            when (tache.frequence) {
                Frequence.QUOTIDIENNE -> inventaire.ajouterPieces((tache.importance.ordinal + 1) * 3)
                Frequence.HEBDOMADAIRE -> inventaire.ajouterPieces((tache.importance.ordinal + 1) * 16)
                Frequence.MENSUELLE -> inventaire.ajouterPieces((tache.importance.ordinal + 1) * 42)
            }
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
        tacheDAO.modifier(nom, tache)
    }

    fun archiverTache(nom: String): Boolean {
        val tacheAArchiver = setDeTaches.find { it.nom == nom } ?: return false
        if (!tacheAArchiver.estTerminee)
            gestionnaireCompagnons.modifierHumeur(
                idCompagnon,
                tacheAArchiver.importance.ordinal + 1 * 20
            )
        tacheAArchiver.estArchivee = true
        tacheDAO.modifier(nom, tacheAArchiver)
        return true
    }

    fun verifierTacheNonAccomplies(): Boolean {
        val dateActuelle = LocalDate.now()
        tacheDAO.obtenirTous().forEach {
            if (!it.estArchivee) {
                when (it.frequence) {
                    Frequence.QUOTIDIENNE -> {
                        if (it.derniereValidation!!.plusDays(1).isBefore(dateActuelle) || it.derniereValidation!!.plusDays(1).isEqual(dateActuelle)) {
                            when (it.estTerminee) {
                                true -> {
                                    it.estTerminee = false
                                    it.derniereValidation = it.derniereValidation!!.plusDays(1)
                                    modifierTache(it.nom, it)
                                }
                                false -> {
                                    it.derniereValidation = it.derniereValidation!!.plusDays(1)
                                    gestionnaireCompagnons.modifierHumeur(idCompagnon, -(it.importance.ordinal + 1) * (it.frequence.ordinal + 1) * 2)
                                }
                            }
                        }
                    }

                    Frequence.HEBDOMADAIRE -> {
                        if (it.derniereValidation!!.plusWeeks(1).isBefore(dateActuelle) || it.derniereValidation!!.plusWeeks(1).isEqual(dateActuelle)) {
                            when (it.estTerminee) {
                                true -> {
                                    it.estTerminee = false
                                    it.derniereValidation = it.derniereValidation!!.plusWeeks(1)
                                    modifierTache(it.nom, it)
                                }
                                false -> {
                                    it.derniereValidation = it.derniereValidation!!.plusWeeks(1)
                                    gestionnaireCompagnons.modifierHumeur(idCompagnon, -(it.importance.ordinal + 1) * (it.frequence.ordinal + 1) * 2)
                                }
                            }
                        }
                    }

                    Frequence.MENSUELLE -> {
                        if (it.derniereValidation!!.plusMonths(1).isBefore(dateActuelle) || it.derniereValidation!!.plusMonths(1).isEqual(dateActuelle)) {
                            when (it.estTerminee) {
                                true -> {
                                    it.estTerminee = false
                                    it.derniereValidation = it.derniereValidation!!.plusMonths(1)
                                    modifierTache(it.nom, it)
                                }
                                false -> {
                                    it.derniereValidation = it.derniereValidation!!.plusMonths(1)
                                    gestionnaireCompagnons.modifierHumeur(idCompagnon, -(it.importance.ordinal + 1) * (it.frequence.ordinal + 1) * 2)
                                }
                            }
                        }
                    }
                }
            }
        }
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