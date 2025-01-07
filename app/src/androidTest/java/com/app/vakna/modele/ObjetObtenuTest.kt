package com.app.vakna.modele

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.objetobtenu.ObjetObtenu
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ObjetObtenuTest {

    private lateinit var objet: ObjetObtenu

    @Before
    fun setUp() {
        objet = ObjetObtenu(1, "Objet de Test", 100, 1, TypeObjet.JOUET, "Ceci est un objet de test.", 10, "http://example.com/image.png")
    }

    @Test
    fun testUpdateQuantite_augmentation() {
        objet.updateQuantite(5)
        assertEquals(15, objet.getQuantite())
    }

    @Test
    fun testUpdateQuantite_reduction() {
        objet.updateQuantite(-3)
        assertEquals(7, objet.getQuantite())
    }

    @Test(expected = AssertionError::class)
    fun testUpdateQuantite_negtiveQuantityThrowsError() {
        objet.updateQuantite(-15)
    }
}
