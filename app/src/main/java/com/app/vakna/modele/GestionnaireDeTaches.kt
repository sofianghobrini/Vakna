package com.app.vakna.modele

import android.content.Context
import com.app.vakna.adapters.ListData
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.TacheDAO
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

class GestionnaireDeTaches(context: Context) {
    private var tacheDAO = TacheDAO(context)
    private val setDeTaches = mutableSetOf<Tache>()
    private val setDeCompagnons = mutableSetOf<Compagnon>()
    private var gestionnaireCompagnons = GestionnaireDeCompagnons(CompagnonDAO(context))
    private var gestionnaireDeRefuge = GestionnaireDeRefuge(context)
    private var idCompagnon: Int = 1
    private var idRefuge: Int = 1
    private var inventaire = Inventaire(context)

    init {
        tacheDAO.obtenirTous().forEach { setDeTaches.add(it) }
        var compagnon = gestionnaireCompagnons.obtenirActif()
        if (compagnon == null) {
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
            tache.prochaineValidation = nouvelleTache.prochaineValidation
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
            val modifImportance = tache.importance.ordinal + 1
            val modifFrequence = tache.frequence.ordinal + 1
            val refuge = gestionnaireDeRefuge.getRefugeParId(idRefuge)
            gestionnaireDeRefuge.getRefuges().forEach { println("Tous les refuges" + it.getNom()) }
            println("Nom du refuge" + refuge?.getNom())
            gestionnaireCompagnons.modifierHumeur(
                idCompagnon,
                (modifImportance * modifFrequence * 3 * refuge!!.getModifHumeur()).toInt()
            )
            gestionnaireCompagnons.gagnerXp(
                idCompagnon,
                (modifImportance * modifFrequence * 3 * refuge.getModifXp()).toInt()
            )
            val compagnon = gestionnaireCompagnons.obtenirCompagnon(idCompagnon)
            var facteurPersonalite = when (compagnon!!.personnalite) {
                Personnalite.GOURMAND -> compagnon.personnalite.facteurPiece
                Personnalite.JOUEUR -> compagnon.personnalite.facteurPiece
                Personnalite.CALME -> compagnon.personnalite.facteurPiece
                Personnalite.CUPIDE -> compagnon.personnalite.facteurPiece
                Personnalite.AVARE -> compagnon.personnalite.facteurPiece
                Personnalite.GRINCHEUX -> compagnon.personnalite.facteurPiece
                Personnalite.RADIN -> compagnon.personnalite.facteurPiece
                Personnalite.GENTIL -> compagnon.personnalite.facteurPiece
                Personnalite.JOYEUX -> compagnon.personnalite.facteurPiece
                Personnalite.TRAVAILLEUR -> compagnon.personnalite.facteurPiece
            }
            when (tache.frequence) {
                Frequence.QUOTIDIENNE -> {
                    if (compagnon.personnalite == Personnalite.CUPIDE) {
                        inventaire.ajouterPieces((((modifImportance * 3 * refuge.getModifPieces()) * facteurPersonalite) * 0.9).toInt())
                        gestionnaireCompagnons.modifierHumeur(compagnon.id, 2)
                    } else {
                        inventaire.ajouterPieces(((modifImportance * 3 * refuge.getModifPieces()) * facteurPersonalite).toInt())
                    }
                }

                Frequence.HEBDOMADAIRE -> {
                    if (compagnon.personnalite == Personnalite.CUPIDE) {
                        inventaire.ajouterPieces((((modifImportance * 16 * refuge.getModifPieces()) * facteurPersonalite) * 0.8).toInt())
                        gestionnaireCompagnons.modifierHumeur(compagnon.id, 4)
                    } else {
                        inventaire.ajouterPieces(((modifImportance * 16 * refuge.getModifPieces()) * facteurPersonalite).toInt())
                    }
                }

                Frequence.MENSUELLE -> {
                    if (compagnon.personnalite == Personnalite.CUPIDE) {
                        inventaire.ajouterPieces((((modifImportance * 42 * refuge.getModifPieces()) * facteurPersonalite) * 0.72).toInt())
                        gestionnaireCompagnons.modifierHumeur(compagnon.id, 7)
                    } else {
                        inventaire.ajouterPieces(((modifImportance * 42 * refuge.getModifPieces()) * facteurPersonalite).toInt())
                    }
                }

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
        val dateActuelle = LocalDateTime.now()
        tacheDAO.obtenirTous().forEach {
            if (it.jours == null || it.jours?.isEmpty() == true) {
                if (!it.estArchivee) {
                    when (it.frequence) {
                        Frequence.QUOTIDIENNE -> {
                            while (dateActuelle.isAfter(it.prochaineValidation) || dateActuelle.isEqual(
                                    it.prochaineValidation
                                )
                            ) {
                                when (it.estTerminee) {
                                    true -> {
                                        it.estTerminee = false
                                        modifierTache(it.nom, it)
                                    }

                                    false -> {
                                        gestionnaireCompagnons.modifierHumeur(
                                            idCompagnon,
                                            -(it.importance.ordinal + 1) * (it.frequence.ordinal + 1) * 2
                                        )
                                    }
                                }
                                it.prochaineValidation = it.prochaineValidation!!.plusDays(1)
                                modifierTache(it.nom, it)
                            }
                        }

                        Frequence.HEBDOMADAIRE -> {
                            while (dateActuelle.isAfter(it.prochaineValidation) || dateActuelle.isEqual(
                                    it.prochaineValidation
                                )
                            ) {
                                when (it.estTerminee) {
                                    true -> {
                                        it.estTerminee = false
                                        modifierTache(it.nom, it)
                                    }

                                    false -> {
                                        gestionnaireCompagnons.modifierHumeur(
                                            idCompagnon,
                                            -(it.importance.ordinal + 1) * (it.frequence.ordinal + 1) * 2
                                        )
                                    }
                                }
                                it.prochaineValidation = it.prochaineValidation!!.plusWeeks(1)
                                modifierTache(it.nom, it)
                            }
                        }

                        Frequence.MENSUELLE -> {
                            while (dateActuelle.isAfter(it.prochaineValidation) || dateActuelle.isEqual(
                                    it.prochaineValidation
                                )
                            ) {
                                when (it.estTerminee) {
                                    true -> {
                                        it.estTerminee = false
                                        modifierTache(it.nom, it)
                                    }

                                    false -> {
                                        gestionnaireCompagnons.modifierHumeur(
                                            idCompagnon,
                                            -(it.importance.ordinal + 1) * (it.frequence.ordinal + 1) * 2
                                        )
                                    }
                                }
                                it.prochaineValidation = it.prochaineValidation!!.plusMonths(1)
                                modifierTache(it.nom, it)
                            }
                        }
                    }
                }
            } else {
                if (!it.estArchivee) {
                    when (it.frequence) {
                        Frequence.HEBDOMADAIRE -> {
                            val jours = it.jours?.map { DayOfWeek.of(it) } ?: emptyList()
                            var prochaineValidation = it.prochaineValidation!!

                            while (!prochaineValidation.isAfter(LocalDateTime.now())) {
                                jours.forEach { jour ->
                                    if (prochaineValidation.dayOfWeek == jour) {
                                        if (dateActuelle.isAfter(prochaineValidation) || dateActuelle.isEqual(prochaineValidation)) {
                                            when (it.estTerminee) {
                                                true -> {
                                                    it.estTerminee = false
                                                    modifierTache(it.nom, it)
                                                }
                                                false -> {
                                                    gestionnaireCompagnons.modifierHumeur(
                                                        idCompagnon,
                                                        -(it.importance.ordinal + 1) * (it.frequence.ordinal + 1) * 2
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                do {
                                    prochaineValidation = prochaineValidation.plusDays(1)
                                } while (!jours.contains(prochaineValidation.dayOfWeek))
                            }
                            it.prochaineValidation = prochaineValidation
                            modifierTache(it.nom, it)
                        }

                        Frequence.MENSUELLE -> {
                            val jours = it.jours ?: emptyList()
                            var prochaineValidation = it.prochaineValidation!!

                            while (!prochaineValidation.isAfter(LocalDateTime.now())) {
                                jours.forEach { jour ->
                                    if (prochaineValidation.dayOfMonth == jour) {
                                        if (dateActuelle.isAfter(prochaineValidation) || dateActuelle.isEqual(prochaineValidation)) {
                                            when (it.estTerminee) {
                                                true -> {
                                                    it.estTerminee = false
                                                    modifierTache(it.nom, it)
                                                }
                                                false -> {
                                                    gestionnaireCompagnons.modifierHumeur(
                                                        idCompagnon,
                                                        -(it.importance.ordinal + 1) * (it.frequence.ordinal + 1) * 2
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                do {
                                    prochaineValidation = prochaineValidation.plusDays(1)
                                } while (!jours.contains(prochaineValidation.dayOfMonth))
                            }
                            it.prochaineValidation = prochaineValidation
                            modifierTache(it.nom, it)
                        }
                        Frequence.QUOTIDIENNE -> throw IllegalArgumentException("Freq quotidienne avec des jours assigné")
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

    fun obtenirTache(nom: String): Tache? {
        return obtenirTaches().find { it.nom.equals(nom) }
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