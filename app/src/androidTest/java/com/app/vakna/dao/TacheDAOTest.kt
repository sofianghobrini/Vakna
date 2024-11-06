package com.app.vakna.dao

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.vakna.modele.dao.TacheDAO
import com.app.vakna.modele.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class TacheDAOTest {

    private lateinit var dao: TacheDAO
    private lateinit var cheminFichier: File
    /** private lateinit var backupFile: File*/
    private lateinit var context: android.content.Context
    private lateinit var backupFilePath: Path

    @Before
    fun setUp() {
        // Get the context using ApplicationProvider
        context = ApplicationProvider.getApplicationContext()

        // Initialize the DAO with the context
        dao = TacheDAO(context)

        // Path to tache.json in the internal storage of the app
        cheminFichier = File(context.filesDir, "tache.json")

        // Path to the tache.json file
        val fichierPath = cheminFichier.toPath()

        // Check if the original file exists
        if (Files.exists(fichierPath)) {
            // Create a backup only if the original file exists
            backupFilePath = Paths.get("${cheminFichier.path}.bak")
            Files.copy(fichierPath, backupFilePath, StandardCopyOption.REPLACE_EXISTING)
        } else {
            // If the original file does not exist, create an empty file
            Files.createFile(fichierPath)
            Files.write(fichierPath, """{"taches": []}""".toByteArray())
        }
    }

    @After
    fun tearDown() {
        // Path to the tache.json file
        val fichierPath = cheminFichier.toPath()

        // Check if the backup file was created and exists
        if (::backupFilePath.isInitialized && Files.exists(backupFilePath)) {
            // Restore the original version of tache.json if backup exists
            Files.copy(backupFilePath, fichierPath, StandardCopyOption.REPLACE_EXISTING)
            Files.delete(backupFilePath)
        } else {
            // Delete the file created during the test if no backup exists
            Files.deleteIfExists(fichierPath)
        }
    }

    @Test
    fun testInsertionTache() {
        val nouvelleTache = Tache(
            nom = "TestTache",
            frequence = Frequence.QUOTIDIENNE,
            importance = Importance.MOYENNE,
            type = TypeTache.PERSONNELLE,
            derniereValidation = LocalDate.now(),
            estTerminee = false
        )

        // Test insertion of a new task
        val insertionReussie = dao.inserer(nouvelleTache)
        assertTrue(insertionReussie)

        // Verify that the task was added
        val tacheInseree = dao.obtenirParId(nouvelleTache.nom)
        assertNotNull(tacheInseree)
        assertEquals("TestTache", tacheInseree?.nom)
    }

    @Test
    fun testInsertionTacheAvecNomDuplique() {
        val tache1 = Tache(
            nom = "TacheDupliquee",
            frequence = Frequence.QUOTIDIENNE,
            importance = Importance.MOYENNE,
            type = TypeTache.PERSONNELLE,
            derniereValidation = LocalDate.now(),
            estTerminee = false
        )

        val tache2 = Tache(
            nom = "TacheDupliquee",
            frequence = Frequence.HEBDOMADAIRE,
            importance = Importance.ELEVEE,
            type = TypeTache.PROFESSIONNELLE,
            derniereValidation = LocalDate.now(),
            estTerminee = true
        )

        // Insert the first task
        val insertionReussie1 = dao.inserer(tache1)
        assertTrue(insertionReussie1)

        // Try to insert the second task with the same name
        val insertionReussie2 = dao.inserer(tache2)
        assertFalse(insertionReussie2)
    }

    @Test
    fun testModificationTache() {
        // Insert a task first to ensure that there's something to modify
        val nouvelleTache = Tache(
            nom = "TestTache",
            frequence = Frequence.QUOTIDIENNE,
            importance = Importance.MOYENNE,
            type = TypeTache.PERSONNELLE,
            derniereValidation = LocalDate.now(),
            estTerminee = false
        )

        // Insert the new task
        val insertionReussie = dao.inserer(nouvelleTache)
        assertTrue(insertionReussie)

        // Retrieve the task to be modified
        val tacheExistant = dao.obtenirTous().firstOrNull()
        assertNotNull(tacheExistant) // Make sure we have an existing task to modify

        val nomTache = tacheExistant!!.nom
        val nouveauNom = "TacheModifiee"

        // Modify the task's name
        tacheExistant.nom = nouveauNom
        val modificationReussie = dao.modifier(nomTache, tacheExistant)
        assertTrue(modificationReussie)

        // Verify that the modification was successful
        val tacheModifiee = dao.obtenirParId(nouveauNom)
        assertNotNull(tacheModifiee)
        assertEquals(nouveauNom, tacheModifiee?.nom)
    }

    @Test
    fun testSuppressionTache() {
        val tacheExistant = dao.obtenirTous().firstOrNull()
        assertNotNull(tacheExistant)

        val nomTache = tacheExistant!!.nom

        // Delete the task
        val suppressionReussie = dao.supprimer(nomTache)
        assertTrue(suppressionReussie)

        // Verify that the task was deleted
        val tacheSupprimee = dao.obtenirParId(nomTache)
        assertNull(tacheSupprimee)
    }

    @Test
    fun testObtenirTous() {
        // TO DO
        dao.obtenirTous()
        assertTrue(true)
    }
}
