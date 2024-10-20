package com.app.vakna

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.vakna.modele.Compagnon
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class ModeleCompagnonTest {

    // Create a test instance of Compagnon
    private val compagnon = Compagnon(0, "Veolia la dragonne", espece = "Dragon")

    @Before
    fun setUp() {
        // Reset the compagnon attributes before each test
        compagnon.faim = 50
        compagnon.humeur = 50
        compagnon.xp = 0
    }

    @After
    fun tearDown() {
        // No teardown necessary since we're not working with files or external resources
    }

    @Test
    fun testModifierFaimNiveauSuperieurA100() {
        val exception = assertThrows<AssertionError> {
            compagnon.modifierFaim(101)
        }
        assertEquals("Le niveau de faim doit être compris entre -100 et 100.", exception.message)
    }

    @Test
    fun testModifierFaimNiveauInferieurA100() {
        val exception = assertThrows<AssertionError> {
            compagnon.modifierFaim(-101)
        }
        assertEquals("Le niveau de faim doit être compris entre -100 et 100.", exception.message)
    }

    @Test
    fun testModifierFaimResultatSuperieurA100() {
        compagnon.modifierFaim(75)
        assertEquals(100, compagnon.faim)
    }

    @Test
    fun testModifierFaimResultatInferieurA0() {
        compagnon.modifierFaim(-75)
        assertEquals(0, compagnon.faim)
    }

    @Test
    fun testModifierFaim() {
        compagnon.modifierFaim(-5)
        assertEquals(45, compagnon.faim)
    }

    @Test
    fun testModifierHumeur() {
        compagnon.modifierHumeur(-5)
        assertEquals(45, compagnon.humeur)
    }

    @Test
    fun testModifierHumeurNiveauSuperieurA100() {
        val exception = assertThrows<AssertionError> {
            compagnon.modifierHumeur(101)
        }
        assertEquals("Le niveau d'humeur doit être compris entre -100 et 100.", exception.message)
    }

    @Test
    fun testModifierHumeurNiveauInferieurA100() {
        val exception = assertThrows<AssertionError> {
            compagnon.modifierHumeur(-101)
        }
        assertEquals("Le niveau d'humeur doit être compris entre -100 et 100.", exception.message)
    }

    @Test
    fun testModifierHumeurResultatSuperieurA100() {
        compagnon.modifierHumeur(75)
        assertEquals(100, compagnon.humeur)
    }

    @Test
    fun testModifierHumeurResultatInferieurA0() {
        compagnon.modifierHumeur(-75)
        assertEquals(0, compagnon.humeur)
    }

    @Test
    fun testGagnerXp() {
        compagnon.gagnerXp(12)
        assertEquals(12, compagnon.xp)
    }

    @Test
    fun testGagnerXpNegatif() {
        compagnon.gagnerXp(120)
        compagnon.gagnerXp(-30)
        assertEquals(90, compagnon.xp)
    }

    @Test
    fun testNiveau0() {
        assertEquals(0, compagnon.niveau())
    }

    @Test
    fun testNiveau1() {
        compagnon.gagnerXp(100)
        assertEquals(1, compagnon.niveau())
    }

    @Test
    fun testNiveau2() {
        compagnon.gagnerXp(250)
        assertEquals(2, compagnon.niveau())
    }

    @Test
    fun testNiveau3() {
        compagnon.gagnerXp(310)
        assertEquals(3, compagnon.niveau())
    }
}
