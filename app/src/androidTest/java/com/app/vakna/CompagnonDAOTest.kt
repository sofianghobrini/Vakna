package com.app.vakna

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.Compagnon
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class CompagnonDAOTest {

    private lateinit var dao: CompagnonDAO
    private lateinit var context: Context
    private lateinit var cheminFichier: File
    private var backupFile: File? = null  // Use a nullable type for the backup file

    @Before
    fun setUp() {
        // Initialize the DAO with Application context from InstrumentationRegistry
        context = InstrumentationRegistry.getInstrumentation().targetContext
        dao = CompagnonDAO(context)

        // Get the file reference for compagnon.json in internal storage
        cheminFichier = File(context.filesDir, "compagnon.json")

        // Create a backup of the file if it exists
        if (cheminFichier.exists()) {
            backupFile = File(context.filesDir, "compagnon.json.bak")
            cheminFichier.copyTo(backupFile!!, overwrite = true)  // Use non-null assertion since we check existence
        } else {
            // Initialize the JSON file if it doesn't exist
            cheminFichier.createNewFile()
            cheminFichier.writeText("""{"compagnons":[]}""")
        }
    }

    @After
    fun tearDown() {
        // Restore the original version of compagnon.json after tests if backup exists
        backupFile?.let {
            if (it.exists()) {
                it.copyTo(cheminFichier, overwrite = true)
                it.delete()  // Remove the backup file after restoration
            }
        }
    }

    @Test
    fun testInsertionCompagnon() {
        val nouveauCompagnon = Compagnon(id = 0, nom = "TestCompagnon", faim = 60, humeur = 70, xp = 20, espece = "Dragon")

        // Test insertion of a new compagnon
        val insertionReussie = dao.inserer(nouveauCompagnon)
        assertTrue(insertionReussie)

        // Verify that the compagnon was added
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

        // Modify the compagnon’s name
        compagnonExistant.nom = nouveauNom
        val modificationReussie = dao.modifier(idCompagnon, compagnonExistant)
        assertTrue(modificationReussie)

        // Verify that the modification was successful
        val compagnonModifie = dao.obtenirParId(idCompagnon)
        assertNotNull(compagnonModifie)
        assertEquals(nouveauNom, compagnonModifie?.nom)
    }

    @Test
    fun testSuppressionCompagnon() {
        val compagnonExistant = dao.obtenirTous().firstOrNull()
        assertNotNull(compagnonExistant)

        val idCompagnon = compagnonExistant!!.id

        // Delete the compagnon
        val suppressionReussie = dao.supprimer(idCompagnon)
        assertTrue(suppressionReussie)

        // Verify that the compagnon was deleted
        val compagnonSupprime = dao.obtenirParId(idCompagnon)
        assertNull(compagnonSupprime)
    }

    @Test
    fun testObtenirTous() {
        // Delete all existing compagnons
        val compagnonsExistants = dao.obtenirTous()
        for (compagnon in compagnonsExistants) {
            dao.supprimer(compagnon.id)
        }

        // Add four new compagnons
        val nouveauxCompagnons = listOf(
            Compagnon(id = 1, nom = "Compagnon1", faim = 50, humeur = 50, xp = 10, espece = "Dragon"),
            Compagnon(id = 2, nom = "Compagnon2", faim = 60, humeur = 60, xp = 20, espece = "Phoenix"),
            Compagnon(id = 3, nom = "Compagnon3", faim = 70, humeur = 70, xp = 30, espece = "Griffon"),
            Compagnon(id = 4, nom = "Compagnon4", faim = 80, humeur = 80, xp = 40, espece = "Licorne")
        )
        for (compagnon in nouveauxCompagnons) {
            dao.inserer(compagnon)
        }

        // Verify that the list of compagnons is not empty and contains the new compagnons
        val compagnons = dao.obtenirTous()
        assertEquals(4, compagnons.size)

        // Verify that each compagnon has valid properties
        for (compagnon in compagnons) {
            assertNotNull(compagnon.id)
            assertNotNull(compagnon.nom)
            assertNotNull(compagnon.espece)
            assertTrue(compagnon.faim in 0..100)
            assertTrue(compagnon.humeur in 0..100)
            assertTrue(compagnon.xp >= 0)
        }

        // Verify that there are no duplicate IDs
        val ids = compagnons.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }
}
