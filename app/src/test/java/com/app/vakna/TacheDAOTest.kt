package com.app.vakna

import com.app.vakna.modele.dao.TacheDAO
import com.app.vakna.modele.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate

class TacheDAOTest {

    private lateinit var dao: TacheDAO
    private lateinit var cheminFichier: String
    private lateinit var backupFilePath: Path

    @Before
    fun setUp() {
        // Initializer le DAO
        dao = TacheDAO()

        // Path à tache.json
        cheminFichier = System.getProperty("user.dir")?.plus("/src/bdd/tache.json") ?: ""

        // Créer une sauvegarde si le fichier existe
        val fichierPath = Paths.get(cheminFichier)
        if (Files.exists(fichierPath)) {
            backupFilePath = Paths.get("$cheminFichier.bak")
            Files.copy(fichierPath, backupFilePath, StandardCopyOption.REPLACE_EXISTING)
        } else {
            throw IllegalStateException("Le fichier tache.json est introuvable.")
        }
    }

    @After
    fun tearDown() {
        // Restaurer la version originale du fichier tache.json
        val fichierPath = Paths.get(cheminFichier)
        if (Files.exists(backupFilePath)) {
            Files.copy(backupFilePath, fichierPath, StandardCopyOption.REPLACE_EXISTING)
            Files.delete(backupFilePath)
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

        // Test pour l'insertion d'une nouvelle tâche
        val insertionReussie = dao.inserer(nouvelleTache)
        assertTrue(insertionReussie)

        // Verifier que la tâche a été ajoutée
        val tacheInseree = dao.obtenirParId(nouvelleTache.nom)
        assertNotNull(tacheInseree)
        assertEquals("TestTache", tacheInseree?.nom)
    }

    @Test
    fun testModificationTache() {
        val tacheExistant = dao.obtenirTous().firstOrNull()
        assertNotNull(tacheExistant)

        val nomTache = tacheExistant!!.nom
        val nouveauNom = "TacheModifiee"

        // Modifier le nom de la tâche
        tacheExistant.nom = nouveauNom
        val modificationReussie = dao.modifier(nomTache, tacheExistant)
        assertTrue(modificationReussie)

        // Verifier que la modification a bien eu lieu
        val tacheModifiee = dao.obtenirParId(nouveauNom)
        assertNotNull(tacheModifiee)
        assertEquals(nouveauNom, tacheModifiee?.nom)
    }

    @Test
    fun testSuppressionTache() {
        val tacheExistant = dao.obtenirTous().firstOrNull()
        assertNotNull(tacheExistant)

        val nomTache = tacheExistant!!.nom

        // Supprimer la tâche
        val suppressionReussie = dao.supprimer(nomTache)
        assertTrue(suppressionReussie)

        // Verifier que la tâche a bien été supprimée
        val tacheSupprimee = dao.obtenirParId(nomTache)
        assertNull(tacheSupprimee)
    }

    @Test
    fun testObtenirTous() {
        // Verifier que la liste des tâches n'est pas vide
        val taches = dao.obtenirTous()
        assertTrue(taches.isNotEmpty())
    }
}
