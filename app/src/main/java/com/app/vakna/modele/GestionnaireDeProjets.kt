package com.app.vakna.modele

import android.content.Context
import android.util.Log
import com.app.vakna.adapters.ListData
import com.app.vakna.modele.dao.AccesJson
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.ProjetDAO
import java.time.LocalDate

class GestionnaireDeProjets(private val context: Context) {
    private var projetDAO = ProjetDAO(context)
    private val setDeProjets = mutableSetOf<Projet>()
    private var gestionnaireCompagnons = GestionnaireDeCompagnons(CompagnonDAO(context))
    private var idCompagnon: Int = 1

    init {
        Log.d("test", setToListDataArray(obtenirProjets()).toString())
        projetDAO.obtenirTous().forEach { setDeProjets.add(it) }
    }

    fun setCompagnon(id: Int) {
        this.idCompagnon = id
    }

    fun getGestionnaireCompagnon(): GestionnaireDeCompagnons{
        return gestionnaireCompagnons
    }

    fun ajouterProjet(projet: Projet): Boolean {
        if (projet.nom.isBlank()) {
            throw IllegalArgumentException("Le nom du projet ne peut pas être vide")
        }
        if (setDeProjets.any { it.nom == projet.nom }) {
            throw IllegalArgumentException("Un projet avec le nom '${projet.nom}' existe déjà")
        }
        if (setDeProjets.add(projet)) {
            return projetDAO.inserer(projet)
        }
        return false
    }


    /** Fonction inutile pour le moment
    fun ajouterProjets(projets: List<Projet>): Boolean {
    var toutesInsertionsOK = true
    if (projets.isEmpty()) {
    throw IllegalArgumentException("La liste de tâches à ajouter ne peut pas être vide")
    }
    for (t in projets) {
    ajouterProjet(t)
    if (!projetDAO.inserer(t)) {
    toutesInsertionsOK = false
    }
    }
    return toutesInsertionsOK
    }*/

    fun modifierProjet(nom: String, nouveauProjet: Projet): Boolean {
        val projet = setDeProjets.find { it.nom == nom }
        if (projet != null) {
            projet.nom = nouveauProjet.nom
            projet.importance = nouveauProjet.importance
            projet.type = nouveauProjet.type
            projet.derniereValidation = nouveauProjet.derniereValidation
            projet.estTermine = nouveauProjet.estTermine
            projet.nbAvancements = nouveauProjet.nbAvancements

            return projetDAO.modifier(nom, nouveauProjet)
        } else {
            throw IllegalArgumentException("Projet avec le nom $nom introuvable")
        }
    }

    fun supprimerProjet(nom: String): Boolean {
        val projet = setDeProjets.find { it.nom == nom }
        if (projet != null) {
            if (!projet.estTermine) {
                projet.estTermine = true
                projet.derniereValidation = LocalDate.now()
                gestionnaireCompagnons.modifierHumeur(
                    idCompagnon,
                    -5 * (projet.importance.ordinal + 1)
                )
                gestionnaireCompagnons.gagnerXp(idCompagnon, -5 * (projet.importance.ordinal + 1))
            }
            setDeProjets.remove(projet)

            return projetDAO.supprimer(projet.nom)
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun avancerProjet(nom: String, qteTravail: Importance) {
        val projet = setDeProjets.find { it.nom == nom }
        if (projet != null) {
            projet.derniereValidation = LocalDate.now()
            projet.estArchive = true
            projet.nbAvancements++
            gestionnaireCompagnons.modifierHumeur(idCompagnon, 3 * (projet.importance.ordinal + qteTravail.ordinal + 2))
            gestionnaireCompagnons.gagnerXp(idCompagnon, 3 * (projet.importance.ordinal + qteTravail.ordinal + 2))
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
        projetDAO.modifier(nom, projet)
    }

    fun finirProjet(nom: String) {
        val projet = setDeProjets.find { it.nom == nom }
        if (projet != null) {
            projet.estTermine = true
            projet.derniereValidation = LocalDate.now()
            projet.nbAvancements++
            gestionnaireCompagnons.modifierHumeur(idCompagnon, 10 * (projet.importance.ordinal + 1) * projet.nbAvancements)
            gestionnaireCompagnons.gagnerXp(idCompagnon, 10 * (projet.importance.ordinal + 1) * projet.nbAvancements)
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun archiverProjet(nom: String): Boolean {
        val projetAArchiver = setDeProjets.find { it.nom == nom } ?: return false
        projetAArchiver.estArchive = true
        projetDAO.modifier(nom, projetAArchiver)
        return true
    }

    fun obtenirProjets(): Set<Projet> {
        setDeProjets.clear()
        projetDAO.obtenirTous().forEach { setDeProjets.add(it) }
        return setDeProjets
    }


    fun obtenirProjets(type: TypeTache): Set<Projet> {
        return obtenirProjets().filter { it.type == type }.toSet()
    }

    fun obtenirProjets(nom: String): Set<Projet> {
        return obtenirProjets().filter { it.nom.contains(nom) }.toSet()
    }

    fun obtenirProjetsParImportance(): Map<Importance, Set<Projet>> {
        return obtenirProjets().groupBy { it.importance }.mapValues { it.value.toSet() }
    }

    fun rechercherProjet(nom: String): Set<Projet> {
        return obtenirProjets().filter { it.nom.contains(nom) }.toSet()
    }

    companion object {
        fun setToListDataArray(projets: Set<Projet>): ArrayList<ListData> {
            val list = ArrayList<ListData>()
            for (projet in projets) {
                val listData = projet.toListData()
                list.add(listData)
            }
            return list
        }
    }
}