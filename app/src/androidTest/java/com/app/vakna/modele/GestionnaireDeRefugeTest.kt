package com.app.vakna.modele

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.vakna.modele.dao.refuge.Refuge
import com.app.vakna.modele.dao.refuge.RefugeDAO
import com.app.vakna.modele.gestionnaires.GestionnaireDeRefuge
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class GestionnaireDeRefugeTest {
    private lateinit var dao: RefugeDAO
    private lateinit var cheminFichier: File
    private lateinit var context: Context
    private lateinit var gestionnaireRefuge: GestionnaireDeRefuge

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        dao = RefugeDAO(context)
        cheminFichier = File(context.filesDir, "refuges.json")
        if (!cheminFichier.exists()) {
            cheminFichier.createNewFile()
            cheminFichier.writeText("""{"refuges": []}""")
        }
        gestionnaireRefuge = GestionnaireDeRefuge(context)
    }

    @After
    fun tearDown() {
        if (cheminFichier.exists()) {
            cheminFichier.delete()
        }
    }

    @Test
    fun testObtenirTous() {
        assertTrue(gestionnaireRefuge.obtenirRefuges().isEmpty())
    }

    @Test
    fun testObtenirTousMultiple() {
        val refuge1 = Refuge(1, "Dragon", 1.0f, 1.0f, 1.0f, 1.0f)
        val refuge2 = Refuge(2, "Lapin", 1.0f, 1.0f, 1.0f, 1.0f)
        gestionnaireRefuge.ajouterRefuge(refuge1)
        gestionnaireRefuge.ajouterRefuge(refuge2)
        assertEquals(gestionnaireRefuge.obtenirRefuges().size, 2)
    }

    @Test
    fun testObtenirParId() {
        val refuge = Refuge(1, "Dragon", 1.0f, 1.0f, 1.0f, 1.0f)
        gestionnaireRefuge.ajouterRefuge(refuge)
        assertEquals(gestionnaireRefuge.obtenirRefuge(1), refuge)
    }

    @Test
    fun testObtenirParIdInexistant() {
        val refuge = Refuge(1, "Dragon", 1.0f, 1.0f, 1.0f, 1.0f)
        gestionnaireRefuge.ajouterRefuge(refuge)
        assertNull(gestionnaireRefuge.obtenirRefuge(2))
    }

    @Test
    fun testObtenirParNom() {
        val refuge = Refuge(1, "Dragon", 1.0f, 1.0f, 1.0f, 1.0f)
        gestionnaireRefuge.ajouterRefuge(refuge)
        assertEquals(gestionnaireRefuge.obtenirRefuge("Dragon"), refuge)
    }

    @Test
    fun testObtenirParNomInexistant() {
        val refuge = Refuge(1, "Dragon", 1.0f, 1.0f, 1.0f, 1.0f)
        gestionnaireRefuge.ajouterRefuge(refuge)
        assertNull(gestionnaireRefuge.obtenirRefuge("Lapin"))
    }

    @Test
    fun testAjouterRefuge() {
        val refuge = Refuge(1, "Dragon", 1.0f, 1.0f, 1.0f, 1.0f)
        gestionnaireRefuge.ajouterRefuge(refuge)
        assertEquals(gestionnaireRefuge.obtenirRefuges().size, 1)
    }
}