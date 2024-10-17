package com.app.vakna

import com.app.vakna.modele.*
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class modeleTacheTest {

    // Gestionnaire de tâches et compagnon utilisés pour les tests
    private val gestionnaire = GestionnaireDeTaches()
    private val compagnon = Compagnon(0, "Veolia la dragonne", espece = "Dragon")

    // Chemin du fichier JSON et sauvegarde
    private val cheminFichier = System.getProperty("user.dir")?.plus("/src/bdd/tache.json") ?: ""
    private lateinit var backupFilePath: Path
    private lateinit var originalFilePath: Path

    init {
        // Associer le compagnon au gestionnaire
        gestionnaire.setCompagnon(compagnon)
    }

    @Before
    fun setUp() {
        // Chemin vers le fichier original
        originalFilePath = Paths.get(cheminFichier)

        // Chemin vers le fichier de sauvegarde
        backupFilePath = Paths.get(cheminFichier + ".bak")

        // Crée une sauvegarde du fichier original avant chaque test
        if (Files.exists(originalFilePath)) {
            Files.copy(originalFilePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING)
        } else {
            throw IllegalStateException("Le fichier tache.json est introuvable.")
        }
    }

    @After
    fun tearDown() {
        // Restaure le fichier original après chaque test
        if (Files.exists(backupFilePath)) {
            Files.copy(backupFilePath, originalFilePath, StandardCopyOption.REPLACE_EXISTING)
            Files.delete(backupFilePath)
        }
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
        compagnon.modifierHumeur(-compagnon.humeur)
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tache)
        val humeurInitiale = compagnon.humeur

        gestionnaire.supprimerTache("Tâche 1")
        assertEquals(humeurInitiale - (5 * tache.importance.ordinal), compagnon.humeur)
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
        compagnon.modifierHumeur(-compagnon.humeur) // Reset du niveau de l'humeur
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.ELEVEE, TypeTache.PERSONNELLE, LocalDate.now(), false)
        gestionnaire.ajouterTache(tache)

        gestionnaire.finirTache("Tâche 1")
        assertTrue(tache.estTerminee)
        assertEquals(30, compagnon.humeur)
        assertEquals(15, compagnon.xp)
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
    fun testRechercherTache(){
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
    fun testChangementDateValidationFinir(){
        val tache1 = Tache("Se scrum le master", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now().minusDays(1), false)
        gestionnaire.ajouterTache(tache1)
        gestionnaire.finirTache("Se scrum le master")
        assertEquals(LocalDate.now(), tache1.derniereValidation)
    }

    @Test
    fun testChangementDateValidationSupprimer(){
        val tache1 = Tache("Se scrum le master", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, LocalDate.now().minusDays(1), false)
        gestionnaire.ajouterTache(tache1)
        gestionnaire.supprimerTache("Se scrum le master")
        assertEquals(LocalDate.now(), tache1.derniereValidation)
    }
}
