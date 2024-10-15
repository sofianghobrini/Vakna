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

    @Test
    fun testAjouterTache() {
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false, compagnon, gestionnaire)
        gestionnaire.ajouterTache(tache)
        assertEquals(1, gestionnaire.obtenirTaches().size)
        assertEquals(tache, gestionnaire.obtenirTaches()[0])
    }

    @Test
    fun testModifierTache() {
        val tacheInitiale = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false, compagnon, gestionnaire)
        gestionnaire.ajouterTache(tacheInitiale)

        val tacheModifiee = Tache("Tâche 1 Modifiée", Frequence.HEBDOMADAIRE, Importance.MOYENNE, TypeTache.PROFESSIONNELLE, LocalDate.now(), false, compagnon, gestionnaire)
        gestionnaire.modifierTache("Tâche 1", tacheModifiee)

        val taches = gestionnaire.obtenirTaches()
        assertEquals(1, taches.size)
        assertEquals("Tâche 1 Modifiée", taches[0].nom)
        assertEquals(Frequence.HEBDOMADAIRE, taches[0].frequence)
        assertEquals(Importance.MOYENNE, taches[0].importance)
        assertEquals(TypeTache.PROFESSIONNELLE, taches[0].type)
    }

    @Test
    fun testSupprimerTache() {
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false, compagnon, gestionnaire)
        gestionnaire.ajouterTache(tache)

        gestionnaire.supprimerTache("Tâche 1")
        assertEquals(0, gestionnaire.obtenirTaches().size)
    }

    @Test
    fun testSupprimerTacheAffecteHumeur() {
        compagnon.modifierHumeur(- compagnon.getHumeur())
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false, compagnon, gestionnaire)
        gestionnaire.ajouterTache(tache)
        val humeurInitiale = compagnon.getHumeur()
        gestionnaire.supprimerTache("Tâche 1")
        assertEquals(humeurInitiale - (5 * tache.importance.ordinal), compagnon.getHumeur())
    }

    @Test
    fun testFinirTache() {
        compagnon.modifierHumeur(-compagnon.getHumeur()) // Reset du niveau de l'humeur
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.ELEVEE, TypeTache.PERSONNELLE, LocalDate.now(), false, compagnon, gestionnaire)
        gestionnaire.ajouterTache(tache)

        gestionnaire.finirTache("Tâche 1")
        assertTrue(tache.estTerminee)
        assertEquals(30, compagnon.getHumeur())
        assertEquals(15, compagnon.getXp())
    }

    @Test
    fun testObtenirTachesParType() {
        val tachePersonnelle = Tache("Tâche Personnelle", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false, compagnon, gestionnaire)
        val tacheProfessionnelle = Tache("Tâche Professionnelle", Frequence.HEBDOMADAIRE, Importance.MOYENNE, TypeTache.PROFESSIONNELLE, LocalDate.now(), false, compagnon, gestionnaire)
        gestionnaire.ajouterTache(tachePersonnelle)
        gestionnaire.ajouterTache(tacheProfessionnelle)

        val tachesPersonnelles = gestionnaire.obtenirTachesParType(TypeTache.PERSONNELLE)
        assertEquals(1, tachesPersonnelles.size)
        assertEquals(tachePersonnelle, tachesPersonnelles[0])

        val tachesProfessionnelles = gestionnaire.obtenirTachesParType(TypeTache.PROFESSIONNELLE)
        assertEquals(1, tachesProfessionnelles.size)
        assertEquals(tacheProfessionnelle, tachesProfessionnelles[0])
    }

    @Test
    fun testModifierTacheIntrouvable() {
        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.modifierTache("Tâche Inexistante", Tache("Tâche Inexistante", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false, compagnon, gestionnaire))
        }
        assertEquals("Tâche avec le nom Tâche Inexistante introuvable", exception.message)
    }

    @Test
    fun testSupprimerTacheIntrouvable() {
        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.supprimerTache("Tâche Inexistante")
        }
        assertEquals("Tâche avec le nom Tâche Inexistante introuvable", exception.message)
    }

    @Test
    fun testFinirTacheIntrouvable() {
        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.finirTache("Tâche Inexistante")
        }
        assertEquals("Tâche avec le nom Tâche Inexistante introuvable", exception.message)
    }
}
