package com.app.vakna

import com.app.vakna.modele.*
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GestionnaireDeTachesTest {
    private val gestionnaire = GestionnaireDeTaches()
    private val compagnon = Compagnon("Veolia la dragonne", espece = "Dragon")

    init {
        gestionnaire.setCompagnon(compagnon)
    }

    @Test
    fun testAjouterTache() {
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        val test = gestionnaire.ajouterTache(tache)
        println(test)
        assertEquals(1, gestionnaire.obtenirTaches().size)
        assertTrue(gestionnaire.obtenirTaches().contains(tache))
    }

    @Test
    fun testAjouterTacheNomVide() {
        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.ajouterTache(Tache("", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false))
        }
        assertEquals("Le nom de la tâche ne peut pas être vide", exception.message)
    }

    @Test
    fun testAjouterTacheExistante() {
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tache)

        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.ajouterTache(Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false))
        }
        assertEquals("Une tâche avec le nom 'Tâche 1' existe déjà", exception.message)
    }

    @Test
    fun testModifierTache() {
        val tacheInitiale = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tacheInitiale)

        val tacheModifiee = Tache("Tâche 1", Frequence.HEBDOMADAIRE, Importance.MOYENNE, TypeTache.PROFESSIONNELLE, LocalDate.now(), false)
        gestionnaire.modifierTache("Tâche 1", tacheModifiee)

        val taches = gestionnaire.obtenirTaches()
        assertEquals(1, taches.size)
        assertEquals(tacheModifiee.frequence, taches.first().frequence)
        assertEquals(tacheModifiee.importance, taches.first().importance)
        assertEquals(tacheModifiee.type, taches.first().type)
    }

    @Test
    fun testModifierTacheIntrouvable() {
        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.modifierTache("Tâche Inexistante", Tache("Tâche Inexistante", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false))
        }
        assertEquals("Tâche avec le nom Tâche Inexistante introuvable", exception.message)
    }

    @Test
    fun testSupprimerTache() {
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tache)

        gestionnaire.supprimerTache("Tâche 1")
        assertEquals(0, gestionnaire.obtenirTaches().size)
    }

    @Test
    fun testSupprimerTacheAffecteHumeur() {
        compagnon.modifierHumeur(-compagnon.getHumeur())
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tache)
        val humeurInitiale = compagnon.getHumeur()

        gestionnaire.supprimerTache("Tâche 1")
        assertEquals(humeurInitiale - (5 * tache.importance.ordinal), compagnon.getHumeur())
    }

    @Test
    fun testSupprimerTacheIntrouvable() {
        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.supprimerTache("Tâche Inexistante")
        }
        assertEquals("Tâche avec le nom Tâche Inexistante introuvable", exception.message)
    }

    @Test
    fun testFinirTache() {
        compagnon.modifierHumeur(-compagnon.getHumeur()) // Reset du niveau de l'humeur
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.ELEVEE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tache)

        gestionnaire.finirTache("Tâche 1")
        assertTrue(tache.estTerminee)
        assertEquals(30, compagnon.getHumeur())
        assertEquals(15, compagnon.getXp())
    }

    @Test
    fun testFinirTacheIntrouvable() {
        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.finirTache("Tâche Inexistante")
        }
        assertEquals("Tâche avec le nom Tâche Inexistante introuvable", exception.message)
    }

    @Test
    fun testObtenirTaches() {
        val tache1 = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        val tache2 = Tache("Tâche 2", Frequence.HEBDOMADAIRE, Importance.MOYENNE, TypeTache.PROFESSIONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tache1)
        gestionnaire.ajouterTache(tache2)

        val taches = gestionnaire.obtenirTaches()
        assertEquals(2, taches.size)
        assertTrue(taches.contains(tache1))
        assertTrue(taches.contains(tache2))
    }

    @Test
    fun testObtenirTachesParType() {
        val tachePersonnelle = Tache("Tâche Personnelle", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        val tacheProfessionnelle = Tache("Tâche Professionnelle", Frequence.HEBDOMADAIRE, Importance.MOYENNE, TypeTache.PROFESSIONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tachePersonnelle)
        gestionnaire.ajouterTache(tacheProfessionnelle)

        val tachesPersonnelles = gestionnaire.obtenirTache(TypeTache.PERSONNELLE)
        assertEquals(1, tachesPersonnelles.size)
        assertTrue(tachesPersonnelles.contains(tachePersonnelle))

        val tachesProfessionnelles = gestionnaire.obtenirTache(TypeTache.PROFESSIONNELLE)
        assertEquals(1, tachesProfessionnelles.size)
        assertTrue(tachesProfessionnelles.contains(tacheProfessionnelle))
    }

    @Test
    fun testObtenirTachesParNom() {
        val tache1 = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        val tache2 = Tache("Tâche 2", Frequence.HEBDOMADAIRE, Importance.MOYENNE, TypeTache.PROFESSIONNELLE, LocalDate.now(), false)

        gestionnaire.ajouterTache(tache1)
        gestionnaire.ajouterTache(tache2)

        val tachesNom1 = gestionnaire.obtenirTache("Tâche 1")
        assertEquals(1, tachesNom1.size)
        assertTrue(tachesNom1.contains(tache1))

        val tachesNom2 = gestionnaire.obtenirTache("Tâche 2")
        assertEquals(1, tachesNom2.size)
        assertTrue(tachesNom2.contains(tache2))

        val tachesNomInexistant = gestionnaire.obtenirTache("Tâche Inexistante")
        assertEquals(0, tachesNomInexistant.size)
    }

}
