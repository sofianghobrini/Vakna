package com.app.vakna

import com.app.vakna.modele.Compagnon
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CompagnonTest {
    private val compagnon = Compagnon("Veolia la dragonne", espece = "Dragon")

    @Test
    fun testModifierFaimNiveauSuperieurA100() {
        val exception = assertThrows<AssertionError> {
            compagnon.modifierFaim(101)
        }
        assertEquals(exception.message, "Le niveau de faim doit être compris entre -100 et 100.")
    }

    @Test
    fun testModifierFaimNiveauInferieurA100() {
        val exception = assertThrows<AssertionError> {
            compagnon.modifierFaim(-101)
        }
        assertEquals(exception.message, "Le niveau de faim doit être compris entre -100 et 100.")
    }

    @Test
    fun testModifierFaimResultatSuperieurA100() {
        compagnon.modifierFaim(75)
        assertEquals(compagnon.getFaim(), 100)
    }

    @Test
    fun testModifierFaimResultatInferieurA0() {
        compagnon.modifierFaim(-75)
        assertEquals(compagnon.getFaim(), 0)
    }

    @Test
    fun testModifierFaim() {
        compagnon.modifierFaim(-5)
        assertEquals(compagnon.getFaim(), 45)
    }

    @Test
    fun testModifierHumeur() {
        compagnon.modifierHumeur(-5)
        println(compagnon.getHumeur())
        assertEquals(compagnon.getHumeur(), 45)
    }

    @Test
    fun testModifierHumeurNiveauSuperieurA100() {
        val exception = assertThrows<AssertionError> {
            compagnon.modifierHumeur(101)
        }
        assertEquals(exception.message, "Le niveau d'humeur doit être compris entre -100 et 100.")
    }

    @Test
    fun testModifierHumeurNiveauInferieurA100() {
        val exception = assertThrows<AssertionError> {
            compagnon.modifierHumeur(-101)
        }
        assertEquals(exception.message, "Le niveau d'humeur doit être compris entre -100 et 100.")
    }

    @Test
    fun testModifierHumeurResultatSuperieurA100() {
        compagnon.modifierHumeur(75)
        assertEquals(compagnon.getHumeur(), 100)
    }

    @Test
    fun testModifierHumeurResultatInferieurA0() {
        compagnon.modifierHumeur(-75)
        assertEquals(compagnon.getHumeur(), 0)
    }

    @Test
    fun testGagnerXp() {
        compagnon.gagnerXp(12)
        assertEquals(compagnon.getXp(), 12)
    }

    @Test
    fun testGagnerXpNegatif() {
        val exception = assertThrows<AssertionError> {
            compagnon.gagnerXp(-5)
        }
        assertEquals(exception.message, "L'xp gagné doit être positif")
    }

    @Test
    fun testNiveau() {
        assertEquals(compagnon.niveau(), 0)
    }
}
