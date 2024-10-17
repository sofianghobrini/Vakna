package com.app.vakna

import com.app.vakna.modele.Compagnon
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CompagnonTest {
    private val compagnon = Compagnon(1, "Veolia la dragonne", espece = "Dragon")

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
        assertEquals(compagnon.faim, 100)
    }

    @Test
    fun testModifierFaimResultatInferieurA0() {
        compagnon.modifierFaim(-75)
        assertEquals(compagnon.faim, 0)
    }

    @Test
    fun testModifierFaim() {
        compagnon.modifierFaim(-5)
        assertEquals(compagnon.faim, 45)
    }

    @Test
    fun testModifierHumeur() {
        compagnon.modifierHumeur(-5)
        println(compagnon.humeur)
        assertEquals(compagnon.humeur, 45)
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
        assertEquals(compagnon.humeur, 100)
    }

    @Test
    fun testModifierHumeurResultatInferieurA0() {
        compagnon.modifierHumeur(-75)
        assertEquals(compagnon.humeur, 0)
    }

    @Test
    fun testGagnerXp() {
        compagnon.gagnerXp(12)
        assertEquals(compagnon.xp, 12)
    }

    @Test
    fun testGagnerXpNegatif() {
        compagnon.gagnerXp(120)
        compagnon.gagnerXp(-30)
    }

    @Test
    fun testNiveau0() {
        assertEquals(compagnon.niveau(), 0)
    }
    @Test
    fun testNiveau1() {
        compagnon.gagnerXp(100)
        assertEquals(compagnon.niveau(), 1)
    }
    @Test
    fun testNiveau2() {
        compagnon.gagnerXp(250)
        assertEquals(compagnon.niveau(), 2)
    }
    @Test
    fun testNiveau3() {
        compagnon.gagnerXp(1000)
        assertEquals(compagnon.niveau(), 3)
    }
}
