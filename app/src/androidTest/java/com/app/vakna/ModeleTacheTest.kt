package com.app.vakna

import androidx.test.core.app.ApplicationProvider
import com.app.vakna.modele.*
import com.app.vakna.modele.dao.TacheDAO
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ModeleTacheTest {

    private lateinit var dao: TacheDAO
    private lateinit var gestionnaire: GestionnaireDeTaches
    private lateinit var compagnon: Compagnon

    private lateinit var cheminFichier: File

    @Before
    fun setUp() {
        // Initialize context using ApplicationProvider
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        // Initialize DAO and gestionnaire
        dao = TacheDAO(context)
        gestionnaire = GestionnaireDeTaches(dao)
        compagnon = Compagnon(0, "Veolia la dragonne", espece = "Dragon")
        gestionnaire.setCompagnon(compagnon)

        // Initialize file paths and ensure taches.json exists
        cheminFichier = File(context.filesDir, "taches.json")
        if (!cheminFichier.exists()) {
            // Create file if it doesn't exist and write default content
            cheminFichier.createNewFile()
            cheminFichier.writeText("""{"taches": []}""")
        }
    }

    @After
    fun tearDown() {
        // Cleanup after tests if needed (delete or reset file)
        if (cheminFichier.exists()) {
            cheminFichier.delete()
        }
    }

    @Test
    fun testObtenirTachesParFrequence() {
        val tache1 = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        val tache2 = Tache("Tâche 2", Frequence.HEBDOMADAIRE, Importance.MOYENNE, TypeTache.PROFESSIONNELLE, LocalDate.now(), false)
        val tache3 = Tache("Tâche 3", Frequence.QUOTIDIENNE, Importance.ELEVEE, TypeTache.SPORT, LocalDate.now(), false)

        gestionnaire.ajouterTache(tache1)
        gestionnaire.ajouterTache(tache2)
        gestionnaire.ajouterTache(tache3)

        val tachesParFrequence = gestionnaire.obtenirTachesParFrequence()

        assertEquals(2, tachesParFrequence[Frequence.QUOTIDIENNE]?.size)
        assertTrue(tachesParFrequence[Frequence.QUOTIDIENNE]?.contains(tache1) == true)
        assertTrue(tachesParFrequence[Frequence.QUOTIDIENNE]?.contains(tache3) == true)

        assertEquals(1, tachesParFrequence[Frequence.HEBDOMADAIRE]?.size)
        assertTrue(tachesParFrequence[Frequence.HEBDOMADAIRE]?.contains(tache2) == true)
    }

    @Test
    fun testObtenirTachesParImportance() {
        val tache1 = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        val tache2 = Tache("Tâche 2", Frequence.HEBDOMADAIRE, Importance.MOYENNE, TypeTache.PROFESSIONNELLE, LocalDate.now(), false)
        val tache3 = Tache("Tâche 3", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.SPORT, LocalDate.now(), false)

        gestionnaire.ajouterTache(tache1)
        gestionnaire.ajouterTache(tache2)
        gestionnaire.ajouterTache(tache3)

        val tachesParImportance = gestionnaire.obtenirTachesParImportance()

        assertEquals(2, tachesParImportance[Importance.FAIBLE]?.size)
        assertTrue(tachesParImportance[Importance.FAIBLE]?.contains(tache1) == true)
        assertTrue(tachesParImportance[Importance.FAIBLE]?.contains(tache3) == true)

        assertEquals(1, tachesParImportance[Importance.MOYENNE]?.size)
        assertTrue(tachesParImportance[Importance.MOYENNE]?.contains(tache2) == true)
    }
    @Test
    fun testAjouterTache() {
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        val insertionReussie = gestionnaire.ajouterTache(tache)
        assertTrue(insertionReussie)
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
        val modificationReussie = gestionnaire.modifierTache("Tâche 1", tacheModifiee)
        assertTrue(modificationReussie)

        val taches = gestionnaire.obtenirTaches()
        assertEquals(1, taches.size)
        val tache = taches.first()
        assertEquals(tacheModifiee.frequence, tache.frequence)
        assertEquals(tacheModifiee.importance, tache.importance)
        assertEquals(tacheModifiee.type, tache.type)
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

        val suppressionReussie = gestionnaire.supprimerTache("Tâche 1")
        assertTrue(suppressionReussie)
        assertEquals(0, gestionnaire.obtenirTaches().size)
    }

    @Test
    fun testSupprimerTacheAffecteHumeur() {
        compagnon.humeur = 50
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tache)
        val humeurInitiale = compagnon.humeur

        gestionnaire.supprimerTache("Tâche 1")
        assertEquals(humeurInitiale - (5 * (tache.importance.ordinal + 1)), compagnon.humeur)
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
        compagnon.humeur = 0
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.ELEVEE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tache)

        gestionnaire.finirTache("Tâche 1")
        assertTrue(tache.estTerminee)
        val expectedHumeur = 0 + 10 * (tache.importance.ordinal + 1)
        val expectedXp = 0 + 5 * (tache.importance.ordinal + 1)
        assertEquals(expectedHumeur, compagnon.humeur)
        assertEquals(expectedXp, compagnon.xp)
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

        val tachesPersonnelles = gestionnaire.obtenirTaches(TypeTache.PERSONNELLE)
        assertEquals(1, tachesPersonnelles.size)
        assertTrue(tachesPersonnelles.contains(tachePersonnelle))

        val tachesProfessionnelles = gestionnaire.obtenirTaches(TypeTache.PROFESSIONNELLE)
        assertEquals(1, tachesProfessionnelles.size)
        assertTrue(tachesProfessionnelles.contains(tacheProfessionnelle))
    }

    @Test
    fun testRechercherTache() {
        val tache1 = Tache("Faire les courses", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        val tache2 = Tache("Faire du sport", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tache1)
        gestionnaire.ajouterTache(tache2)
        val resultat = gestionnaire.rechercherTache("Faire")
        assertEquals(2, resultat.size)
        val resultat2 = gestionnaire.rechercherTache("Travailler")
        assertEquals(0, resultat2.size)
    }

    @Test
    fun testChangementDateValidationFinir() {
        val tache1 = Tache("Se scrum le master", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now().minusDays(1), false)
        gestionnaire.ajouterTache(tache1)
        gestionnaire.finirTache("Se scrum le master")
        assertEquals(LocalDate.now(), tache1.derniereValidation)
    }

    @Test
    fun testChangementDateValidationSupprimer() {
        val tache1 = Tache("Se scrum le master", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now().minusDays(1), false)
        gestionnaire.ajouterTache(tache1)
        gestionnaire.supprimerTache("Se scrum le master")
        assertEquals(LocalDate.now(), tache1.derniereValidation)
    }
}
