package com.app.vakna.modele

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RefugeTest {
    private lateinit var refuge: Refuge

    @Before
    fun setUp() {
        refuge = Refuge(1, "Dragon")
    }

    @Test
    fun testToString() {
        assertEquals(refuge.toString(), "Refuge Dragon(1) : faim : 1, humeur : 1, xp : 1, pieces : 1")
    }

    @Test
    fun testApparence() {
        assertEquals(refuge.apparence(), "file:///android_asset/refuges/refuge_dragon.png")
    }

    @Test
    fun testEquals() {
        val refuge2 = Refuge(1, "Dragon")
        assertTrue(refuge.equals(refuge2))
    }

    @Test
    fun testEqualsClasseDifferente() {
        val autreType = 1
        assertFalse(refuge.equals(autreType))
    }

    @Test
    fun testEqualsFalse() {
        val idDifferent = Refuge(2, "Dragon")
        val nomDifferent = Refuge(1, "Lapin")
        val toutDifferent = Refuge(2, "Lapin")
        assertFalse(refuge.equals(idDifferent))
        assertFalse(refuge.equals(nomDifferent))
        assertFalse(refuge.equals(toutDifferent))
    }
}