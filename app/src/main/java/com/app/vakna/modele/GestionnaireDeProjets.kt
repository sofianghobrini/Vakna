package com.app.vakna.modele

import android.content.Context
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.ProjetDAO
import java.time.LocalDate

class GestionnaireDeProjets(context: Context) {
    private var projetDAO = ProjetDAO(context)
    private val setDeProjets = mutableSetOf<Projet>()
    private var gestionnaireCompagnons = GestionnaireDeCompagnons(CompagnonDAO(context))
    private var idCompagnon: Int = 1

    init {
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
        if (!setDeProjets.add(projet)) {
            throw IllegalArgumentException("Un projet avec le nom '${projet.nom}' existe déjà")
        }
        return projetDAO.inserer(projet)
    }

    fun modifierProjet(nom: String, nouvelleProjet: Projet): Boolean {
        val projet = setDeProjets.find { it.nom == nom }
        if (projet != null) {
            projet.nom = nouvelleProjet.nom
            projet.frequence = nouvelleProjet.frequence
            projet.importance = nouvelleProjet.importance
            projet.type = nouvelleProjet.type
            projet.derniereValidation = nouvelleProjet.derniereValidation
            projet.estTermine = nouvelleProjet.estTermine

            return projetDAO.modifier(nom, nouvelleProjet)
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

    fun finirProjet(nom: String) {
        val projet = setDeProjets.find { it.nom == nom }
        if (projet != null) {
            projet.estTermine = true
            projet.derniereValidation = LocalDate.now()
            gestionnaireCompagnons.modifierHumeur(idCompagnon, 5 * (projet.importance.ordinal + 1))
            gestionnaireCompagnons.gagnerXp(idCompagnon, 5 * (projet.importance.ordinal + 1))
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
        projetDAO.modifier(nom, projet)
    }

    fun archiverProjet(nom: String): Boolean {
        val projetAArchiver = setDeProjets.find { it.nom == nom } ?: return false
        projetAArchiver.estArchive = true
        projetDAO.modifier(nom, projetAArchiver)
        return true
    }

    private fun obtenirProjets(): Set<Projet> {
        projetDAO.obtenirTous().forEach { setDeProjets.add(it) }
        return setDeProjets
    }


    fun obtenirProjets(type: TypeTache): Set<Projet> {
        return obtenirProjets().filter { it.type == type }.toSet()
    }

    fun obtenirProjets(nom: String): Set<Projet> {
        return obtenirProjets().filter { it.nom.contains(nom) }.toSet()
    }

    fun obtenirProjets(frequence: Frequence): Set<Projet> {
        return obtenirProjets().filter { it.frequence == frequence }.toSet()
    }

    fun obtenirProjetsParFrequence(): Map<Frequence, Set<Projet>> {
        return obtenirProjets().groupBy { it.frequence }.mapValues { it.value.toSet() }
    }

    fun obtenirProjetsParImportance(): Map<Importance, Set<Projet>> {
        return obtenirProjets().groupBy { it.importance }.mapValues { it.value.toSet() }
    }

    fun rechercherProjet(nom: String): Set<Projet> {
        return obtenirProjets().filter { it.nom.contains(nom) }.toSet()
    }
}