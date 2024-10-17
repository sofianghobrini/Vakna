package com.app.vakna

import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.Compagnon
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class CompagnonDAOTest {

    private lateinit var dao: CompagnonDAO
    private lateinit var cheminFichier: String
    private lateinit var backupFilePath: Path

    @Before
    fun setUp() {
        // Initialiser le DAO
        dao = CompagnonDAO()

        // Chemin du fichier compagnon.json
        cheminFichier = System.getProperty("user.dir")?.plus("/src/bdd/compagnon.json") ?: ""

        // Vérifier si le fichier existe avant de créer une sauvegarde
        val fichierPath = Paths.get(cheminFichier)
        if (Files.exists(fichierPath)) {
            // Utiliser Paths.get() au lieu de Path.of() pour Java 8
            backupFilePath = Paths.get("$cheminFichier.bak")
            Files.copy(fichierPath, backupFilePath, StandardCopyOption.REPLACE_EXISTING)
        } else {
            throw IllegalStateException("Le fichier compagnon.json est introuvable.")
        }
    }

    @After
    fun tearDown() {
        // Restaurer la version originale du fichier compagnon.json
        val fichierPath = Paths.get(cheminFichier)
        if (Files.exists(backupFilePath)) {
            Files.copy(backupFilePath, fichierPath, StandardCopyOption.REPLACE_EXISTING)
            Files.delete(backupFilePath)
        }
    }

    @Test
    fun testInsertionCompagnon() {
        val nouveauCompagnon = Compagnon(id = 0, nom = "TestCompagnon", faim = 60, humeur = 70, xp = 20, espece = "Dragon")

        // Tester l'insertion d'un nouveau compagnon
        val insertionReussie = dao.inserer(nouveauCompagnon)
        assertTrue(insertionReussie)

        // Vérifier que le compagnon a été ajouté
        val compagnonInsere = dao.obtenirParId(nouveauCompagnon.id)
        assertNotNull(compagnonInsere)
        assertEquals("TestCompagnon", compagnonInsere?.nom)
    }

    @Test
    fun testModificationCompagnon() {
        val compagnonExistant = dao.obtenirTous().firstOrNull()
        assertNotNull(compagnonExistant)

        val idCompagnon = compagnonExistant!!.id
        val nouveauNom = "CompagnonModifie"

        // Modifier le nom du compagnon
        compagnonExistant.nom = nouveauNom
        val modificationReussie = dao.modifier(idCompagnon, compagnonExistant)
        assertTrue(modificationReussie)

        // Vérifier que la modification a bien eu lieu
        val compagnonModifie = dao.obtenirParId(idCompagnon)
        assertNotNull(compagnonModifie)
        assertEquals(nouveauNom, compagnonModifie?.nom)
    }

    @Test
    fun testSuppressionCompagnon() {
        val compagnonExistant = dao.obtenirTous().firstOrNull()
        assertNotNull(compagnonExistant)

        val idCompagnon = compagnonExistant!!.id

        // Supprimer le compagnon
        val suppressionReussie = dao.supprimer(idCompagnon)
        assertTrue(suppressionReussie)

        // Vérifier que le compagnon a bien été supprimé
        val compagnonSupprime = dao.obtenirParId(idCompagnon)
        assertNull(compagnonSupprime)
    }

    @Test
    fun testObtenirTous() {
        // Vérifier que la liste des compagnons n'est pas vide
        val compagnons = dao.obtenirTous()
        assertTrue(compagnons.isNotEmpty())
    }
}
