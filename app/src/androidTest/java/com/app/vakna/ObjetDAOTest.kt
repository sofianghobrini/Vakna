package com.app.vakna.modele.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.vakna.modele.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ObjetDAOTest {

    private lateinit var dao: ObjetDAO
    private lateinit var cheminFichier: File
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()

        dao = ObjetDAO(context)

        cheminFichier = File(context.filesDir, "objets.json")

        if (!cheminFichier.exists()) {
            cheminFichier.createNewFile()
            cheminFichier.writeText("""{"objets": []}""")
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
    fun testInsererObjet() {
        val objet = Objet(1, "Objet Test", 100, 1, TypeObjet.JOUET, "C'est un objet de test.", "test")

        assertTrue(dao.inserer(objet))

        val objets = dao.obtenirTous()
        assertEquals(1, objets.size)
        assertEquals("Objet Test", objets[0].getNom())
    }

    @Test
    fun testInsererObjetAvecIdDuplique() {
        dao.inserer(Objet(1, "Objet Test", 100, 1, TypeObjet.JOUET, "C'est un objet de test.", "test"))

        assertFalse(dao.inserer(Objet(1, "Objet Test Dupplique", 150, 1, TypeObjet.NOURRITURE, "C'est un objet dupliqué.", "test")))
    }

    @Test
    fun testObtenirParId() {
        val objet = Objet(1, "Objet Test", 100, 1, TypeObjet.JOUET, "C'est un objet de test.", "test")
        dao.inserer(objet)

        val objetRecupere = dao.obtenirParId(1)
        assertNotNull(objetRecupere)
        assertEquals("Objet Test", objetRecupere?.getNom())
    }

    @Test
    fun testObtenirParId_Inexistant() {
        val objetRecupere = dao.obtenirParId(99)
        assertNull(objetRecupere)
    }

    @Test
    fun testModifierObjet() {
        val objet = Objet(1, "Objet Test", 100, 1, TypeObjet.JOUET, "C'est un objet de test.", "test")
        dao.inserer(objet)

        val nouvelObjet = Objet(1, "Objet Modifie", 200, 2, TypeObjet.NOURRITURE, "C'est un objet modifié.", "test")
        val modificationReussie = dao.modifier(1, nouvelObjet)
        assertTrue(modificationReussie)

        val objetModifie = dao.obtenirParId(1)
        assertNotNull(objetModifie)
        assertEquals("Objet Modifie", objetModifie?.getNom())
    }

    @Test
    fun testModifierObjet_Inexistant() {
        val nouvelObjet = Objet(99, "Objet Modifie", 200, 2, TypeObjet.NOURRITURE, "C'est un objet modifié.", "test")
        val modificationReussie = dao.modifier(99, nouvelObjet)
        assertFalse(modificationReussie) // Should fail since it does not exist
    }

    @Test
    fun testSupprimerObjet() {
        val objet = Objet(1, "Objet Test", 100, 1, TypeObjet.JOUET, "C'est un objet de test.", "test")
        dao.inserer(objet)
        val suppressionReussie = dao.supprimer(1)
        assertTrue(suppressionReussie)
        val objetSupprime = dao.obtenirParId(1)
        assertNull(objetSupprime)
    }

    @Test
    fun testSupprimerObjet_Inexistant() {
        val suppressionReussie = dao.supprimer(99)
        assertFalse(suppressionReussie)
    }

    @Test
    fun testObtenirParType() {
        val objet1 = Objet(1, "Objet Test", 100, 1, TypeObjet.JOUET, "C'est un objet de test.", "test")
        val objet2 = Objet(2, "Objet Nourriture", 150, 1, TypeObjet.NOURRITURE, "C'est un objet de nourriture.", "test")
        dao.inserer(objet1)
        dao.inserer(objet2)

        val objetsJouets = dao.obtenirParType(TypeObjet.JOUET)
        assertEquals(1, objetsJouets.size)
        assertEquals("Objet Test", objetsJouets[0].getNom())

        val objetsNourriture = dao.obtenirParType(TypeObjet.NOURRITURE)
        assertEquals(1, objetsNourriture.size)
        assertEquals("Objet Nourriture", objetsNourriture[0].getNom())
    }
}
