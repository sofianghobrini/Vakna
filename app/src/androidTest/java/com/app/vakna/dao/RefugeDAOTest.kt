package com.app.vakna.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.vakna.modele.Refuge
import com.app.vakna.modele.dao.RefugeDAO
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class RefugeDAOTest {
    private lateinit var dao: RefugeDAO
    private lateinit var cheminFichier: File
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        dao = RefugeDAO(context)
        cheminFichier = File(context.filesDir, "refuges.json")
        if (!cheminFichier.exists()) {
            cheminFichier.createNewFile()
            cheminFichier.writeText("""{"refuges": []}""")
        }
    }

    @After
    fun tearDown() {
        if (cheminFichier.exists()) {
            cheminFichier.delete()
        }
    }

    @Test
    fun testObtenirTous() {
        assertTrue(dao.obtenirTous().isEmpty())
    }

    @Test
    fun testObtenirTousMultiple() {
        val refuge1 = Refuge(1, "Dragon", 1, 1, 1, 1)
        val refuge2 = Refuge(2, "Lapin", 1, 1, 1, 1)
        dao.inserer(refuge1)
        dao.inserer(refuge2)
        assertEquals(dao.obtenirTous().size, 2)
    }

    @Test
    fun testInserer() {
        val refuge = Refuge(1, "Dragon", 1, 1, 1, 1)
        assertTrue(dao.inserer(refuge))
        val refuges = dao.obtenirTous()
        assertEquals(1, refuges.size)
        assertEquals(refuge, refuges[0])
    }

    @Test
    fun testInsererRefugeExistant() {
        val refuge = Refuge(1, "Dragon", 1, 1, 1, 1)
        assertTrue(dao.inserer(refuge))
        assertFalse(dao.inserer(refuge))
        val refuges = dao.obtenirTous()
        assertEquals(1, refuges.size)
    }

    @Test
    fun testObtenirParId() {
        val refuge = Refuge(1, "Dragon", 1, 1, 1, 1)
        dao.inserer(refuge)
        assertEquals(dao.obtenirParId(1), refuge)
    }

    @Test
    fun testObtenirParIdNonExistant() {
        val refuge = Refuge(1, "Dragon", 1, 1, 1, 1)
        dao.inserer(refuge)
        assertNull(dao.obtenirParId(3))
    }

    @Test
    fun testModifier() {
        val refuge = Refuge(1, "Dragon", 1, 1, 1, 1)
        dao.inserer(refuge)
        val nouveauRefuge = Refuge(1, "Tigre", 1, 1, 1, 1)
        assertTrue(dao.modifier(1, nouveauRefuge))
        assertEquals(dao.obtenirParId(1), nouveauRefuge)
    }

    @Test
    fun testModifierRefugeInnexistant() {
        val refuge = Refuge(1, "Dragon", 1, 1, 1, 1)
        dao.inserer(refuge)
        val nouveauRefuge = Refuge(2, "Tigre", 1, 1, 1, 1)
        assertFalse(dao.modifier(2, nouveauRefuge))
    }

    @Test
    fun testSupprimer() {
        val refuge = Refuge(1, "Dragon", 1, 1, 1, 1)
        dao.inserer(refuge)
        assertTrue(dao.supprimer(1))
        assertTrue(dao.obtenirTous().isEmpty())
    }

    @Test
    fun testSupprimerRefugeInnexistant() {
        val refuge = Refuge(1, "Dragon", 1, 1, 1, 1)
        dao.inserer(refuge)
        assertFalse(dao.supprimer(2))
        assertEquals(dao.obtenirTous().size, 1)
    }
}