package com.app.vakna.modele.gestionnaires

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.app.vakna.adapters.ListData
import com.app.vakna.modele.dao.compagnon.Compagnon
import com.app.vakna.modele.dao.Frequence
import com.app.vakna.modele.dao.Personnalite
import com.app.vakna.modele.dao.refuge.Refuge
import com.app.vakna.modele.dao.tache.Tache
import com.app.vakna.modele.dao.TypeTache
import com.app.vakna.modele.dao.tache.TacheDAO
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

class GestionnaireDeTaches(context: Context) {

    private val facteurPiecesJournalier = 3
    private val facteurPiecesHebdomadaire = 16
    private val facteurPiecesMensuel = 42
    private val facteurVolJournalier = 0.9
    private val facteurVolHebdomadaire = 0.8
    private val facteurVolMensuel = 0.72
    private val facteurHumeurJournalier = 2
    private val facteurHumeurHebdomadaire = 4
    private val facteurHumeurMensuel = 7

    private var tacheDAO = TacheDAO(context)
    private val setDeTaches = mutableSetOf<Tache>()
    private var gestionnaireCompagnons = GestionnaireDeCompagnons(context)
    private var gestionnaireDeRefuge = GestionnaireDeRefuge(context)
    private var inventaire = Inventaire(context)
    private var idCompagnon: Int = gestionnaireCompagnons.obtenirActif().id
    private var idRefuge: Int = gestionnaireDeRefuge.obtenirActif()?.getId() ?: 1
    private lateinit var refuge: Refuge
    private lateinit var compagnon: Compagnon

    init {
        tacheDAO.obtenirTous().forEach { setDeTaches.add(it) }
    }

    fun obtenirTaches(): Set<Tache> {
        tacheDAO.obtenirTous().forEach { setDeTaches.add(it) }
        return setDeTaches
    }

    fun obtenirTaches(type: TypeTache): Set<Tache> {
        return obtenirTaches().filter { it.type == type }.toSet()
    }

    fun obtenirTaches(frequence: Frequence): Set<Tache> {
        return obtenirTaches().filter { it.frequence == frequence }.toSet()
    }

    fun obtenirTache(nom: String): Tache? {
        return obtenirTaches().find { it.nom.equals(nom) }
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun supprimerTache(nom: String): Boolean {
        val tache = setDeTaches.find { it.nom == nom }
        if (tache != null) {
            handleAbandon(tache)
            setDeTaches.remove(tache)
            return tacheDAO.supprimer(tache.nom)
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun finirTache(nom: String) {
        val tache = setDeTaches.find { it.nom == nom }
        if (tache != null) {
            compagnon = gestionnaireCompagnons.obtenirActif()
            refuge = gestionnaireDeRefuge.obtenirRefuge(idRefuge) ?: throw IllegalStateException("Refuge avec ID $idRefuge introuvable")

            tache.estTerminee = true
            tache.derniereValidation = LocalDate.now()
            val modifImportance = tache.importance.ordinal + 1
            val modifFrequence = tache.frequence.ordinal + 1

            gestionnaireCompagnons.modifierHumeur(idCompagnon, (modifImportance * modifFrequence * 3 * refuge.getModifHumeur()).toInt())
            gestionnaireCompagnons.gagnerXp(idCompagnon, (modifImportance * modifFrequence * 3 * refuge.getModifXp()).toInt())

            when (tache.frequence) {
                Frequence.QUOTIDIENNE -> {
                    calculRecompense(modifImportance, facteurPiecesJournalier, facteurVolJournalier, facteurHumeurJournalier)
                }
                Frequence.HEBDOMADAIRE ->{
                    calculRecompense(modifImportance, facteurPiecesHebdomadaire, facteurVolHebdomadaire, facteurHumeurHebdomadaire)
                }
                Frequence.MENSUELLE ->{
                    calculRecompense(modifImportance, facteurPiecesMensuel, facteurVolMensuel, facteurHumeurMensuel)
                }
            }
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
        tacheDAO.modifier(nom, tache)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun archiverTache(nom: String): Boolean {
        val tache = setDeTaches.find { it.nom == nom } ?: return false
        handleAbandon(tache)
        tache.estArchivee = true
        tacheDAO.modifier(nom, tache)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleAbandon(tache: Tache) {
        if (!tache.estTerminee) {
            gestionnaireCompagnons.modifierHumeur(
                idCompagnon,
                tache.importance.ordinal + 1 * 20
            )
        }
    }

    private fun calculRecompense(
        modifImportance: Int,
        facteurPieces: Int,
        facteurVol: Double,
        facteurHumeur: Int
    ) {
        val facteurPersonalite = recupererFacteurPieces(compagnon)
        if (compagnon.personnalite == Personnalite.RADIN) {
            inventaire.ajouterPieces((((modifImportance * facteurPieces * refuge.getModifPieces()) * facteurPersonalite) * facteurVol).toInt())
            gestionnaireCompagnons.modifierHumeur(compagnon.id, facteurHumeur)
        } else {
            inventaire.ajouterPieces(((modifImportance * facteurPieces * refuge.getModifPieces()) * facteurPersonalite).toInt())
        }
    }

    private fun recupererFacteurPieces(compagnon: Compagnon): Float {
        val facteurPersonalite = when (compagnon.personnalite) {
            Personnalite.GOURMAND -> compagnon.personnalite.facteurPiece
            Personnalite.JOUEUR -> compagnon.personnalite.facteurPiece
            Personnalite.CALME -> compagnon.personnalite.facteurPiece
            Personnalite.AVARE -> compagnon.personnalite.facteurPiece
            Personnalite.GRINCHEUX -> compagnon.personnalite.facteurPiece
            Personnalite.RADIN -> compagnon.personnalite.facteurPiece
            Personnalite.GENTIL -> compagnon.personnalite.facteurPiece
            Personnalite.JOYEUX -> compagnon.personnalite.facteurPiece
            Personnalite.TRAVAILLEUR -> compagnon.personnalite.facteurPiece
        }
        return facteurPersonalite
    }

    companion object {
        fun setToListDataArray(taches: Set<Tache>, context: Context): ArrayList<ListData> {
            val list = ArrayList<ListData>()
            for (tache in taches) {
                val listData = tache.toListData(context)
                list.add(listData)
            }
            return list
        }
    }
}