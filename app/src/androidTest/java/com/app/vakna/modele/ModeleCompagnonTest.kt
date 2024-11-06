package com.app.vakna.modele

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.vakna.modele.dao.CompagnonDAO
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class ModeleCompagnonTest {

    private lateinit var compagnonDAO: CompagnonDAO
    private lateinit var gestionnaireDeCompagnons: GestionnaireDeCompagnons

    // Create a test instance of Compagnon
    private val compagnon = Compagnon(0, "Veolia la dragonne", espece = "Dragon")

    @Before
    fun setUp() {
        // Initialize the real DAO with application context
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        compagnonDAO = CompagnonDAO(context)


        // Initialize the gestionnaire with the real DAO
        gestionnaireDeCompagnons = GestionnaireDeCompagnons(compagnonDAO)

        // Reset the compagnon attributes before each test
        compagnon.faim = 50
        compagnon.humeur = 50
        compagnon.xp = 0

        // Add the test compagnon to the gestionnaire
        gestionnaireDeCompagnons.ajouterCompagnon(compagnon)
    }

    @After
    fun tearDown() {
        // Clean up the DAO by removing the test compagnon after each test
        gestionnaireDeCompagnons.obtenirCompagnons().forEach {
            compagnonDAO.supprimer(it.id)
        }
    }

    @Test
    fun testModifierFaimNiveauSuperieurA100() {
        val exception = assertThrows<AssertionError> {
            gestionnaireDeCompagnons.modifierFaim(compagnon.id, 101)
        }
        assertEquals("Le niveau de faim doit être compris entre -100 et 100.", exception.message)
    }

    @Test
    fun testModifierFaimNiveauInferieurA100() {
        val exception = assertThrows<AssertionError> {
            gestionnaireDeCompagnons.modifierFaim(compagnon.id, -101)
        }
        assertEquals("Le niveau de faim doit être compris entre -100 et 100.", exception.message)
    }

    @Test
    fun testModifierFaimResultatSuperieurA100() {
        gestionnaireDeCompagnons.modifierFaim(compagnon.id, 75)
        assertEquals(100, compagnon.faim)
    }

    @Test
    fun testModifierFaimResultatInferieurA0() {
        gestionnaireDeCompagnons.modifierFaim(compagnon.id, -75)
        assertEquals(0, compagnon.faim)
    }

    @Test
    fun testModifierFaim() {
        gestionnaireDeCompagnons.modifierFaim(compagnon.id, -5)
        assertEquals(45, compagnon.faim)
    }

    @Test
    fun testModifierHumeur() {
        gestionnaireDeCompagnons.modifierHumeur(compagnon.id, -5)
        assertEquals(45, compagnon.humeur)
    }

    @Test
    fun testModifierHumeurNiveauSuperieurA100() {
        val exception = assertThrows<AssertionError> {
            gestionnaireDeCompagnons.modifierHumeur(compagnon.id, 101)
        }
        assertEquals("Le niveau d'humeur doit être compris entre -100 et 100.", exception.message)
    }

    @Test
    fun testModifierHumeurNiveauInferieurA100() {
        val exception = assertThrows<AssertionError> {
            gestionnaireDeCompagnons.modifierHumeur(compagnon.id, -101)
        }
        assertEquals("Le niveau d'humeur doit être compris entre -100 et 100.", exception.message)
    }

    @Test
    fun testModifierHumeurResultatSuperieurA100() {
        gestionnaireDeCompagnons.modifierHumeur(compagnon.id, 75)
        assertEquals(100, compagnon.humeur)
    }

    @Test
    fun testModifierHumeurResultatInferieurA0() {
        gestionnaireDeCompagnons.modifierHumeur(compagnon.id, -75)
        assertEquals(0, compagnon.humeur)
    }

    @Test
    fun testGagnerXp() {
        gestionnaireDeCompagnons.gagnerXp(compagnon.id, 12)
        assertEquals(12, compagnon.xp)
    }

    @Test
    fun testGagnerXpNegatif() {
        gestionnaireDeCompagnons.gagnerXp(compagnon.id, 120)
        gestionnaireDeCompagnons.gagnerXp(compagnon.id, -30)
        assertEquals(90, compagnon.xp)
    }

    @Test
    fun testNiveau0() {
        assertEquals(0, compagnon.niveau())
    }

    @Test
    fun testNiveau1() {
        gestionnaireDeCompagnons.gagnerXp(compagnon.id, 100)
        assertEquals(1, compagnon.niveau())
    }

    @Test
    fun testNiveau2() {
        gestionnaireDeCompagnons.gagnerXp(compagnon.id, 250)
        assertEquals(2, compagnon.niveau())
    }

    @Test
    fun testNiveau3() {
        gestionnaireDeCompagnons.gagnerXp(compagnon.id, 310)
        assertEquals(3, compagnon.niveau())
    }
}
