package com.app.vakna

import com.app.vakna.modele.Objet
import com.app.vakna.modele.TypeObjet
import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class ModeleObjetTest {

    @Test
    fun testConstructeurValide() {
        val objet = Objet(1, "Ballon", 10, 1, TypeObjet.JOUET, "Un ballon rouge", "https://example.com/ballon.jpg")
        assertEquals("Ballon", objet.getNom())
        assertEquals(10, objet.getPrix())
        assertEquals(1, objet.getNiveau())
        assertEquals(TypeObjet.JOUET, objet.getType())
        assertEquals("Un ballon rouge", objet.getDetails())
    }

    @Test
    fun testConstructeurNomVide() {
        assertFailsWith<IllegalArgumentException>("Le nom ne peut pas être vide ou seulement des espaces.") {
            Objet(1, "", 10, 1, TypeObjet.JOUET, "Un ballon rouge", "https://example.com/ballon.jpg")
        }
    }

    @Test
    fun testConstructeurPrixNegatif() {
        assertFailsWith<IllegalArgumentException>("Le prix ne peut pas être négatif.") {
            Objet(1, "Ballon", -5, 1, TypeObjet.JOUET, "Un ballon rouge", "https://example.com/ballon.jpg")
        }
    }

    @Test
    fun testConstructeurDetailsVides() {
        assertFailsWith<IllegalArgumentException>("Les détails ne peuvent pas être vides.") {
            Objet(1, "Ballon", 10, 1, TypeObjet.JOUET, "", "https://example.com/ballon.jpg")
        }
    }

    @Test
    fun testSetNomValide() {
        val objet = Objet(1, "Ballon", 10, 1, TypeObjet.JOUET, "Un ballon rouge", "https://example.com/ballon.jpg")
        objet.setNom("Peluche")
        assertEquals("Peluche", objet.getNom())
    }

    @Test
    fun testSetNomVide() {
        val objet = Objet(1, "Ballon", 10, 1, TypeObjet.JOUET, "Un ballon rouge", "https://example.com/ballon.jpg")
        assertFailsWith<IllegalArgumentException>("Le nom ne peut pas être vide ou seulement des espaces.") {
            objet.setNom("")
        }
    }

    @Test
    fun testSetPrixValide() {
        val objet = Objet(1, "Ballon", 10, 1, TypeObjet.JOUET, "Un ballon rouge", "https://example.com/ballon.jpg")
        objet.setPrix(20)
        assertEquals(20, objet.getPrix())
    }

    @Test
    fun testSetPrixNegatif() {
        val objet = Objet(1, "Ballon", 10, 1, TypeObjet.JOUET, "Un ballon rouge", "https://example.com/ballon.jpg")
        assertFailsWith<IllegalArgumentException>("Le prix ne peut pas être négatif.") {
            objet.setPrix(-10)
        }
    }

    @Test
    fun testSetDetailsValide() {
        val objet = Objet(1, "Ballon", 10, 1, TypeObjet.JOUET, "Un ballon rouge", "https://example.com/ballon.jpg")
        objet.setDetails("Un ballon bleu")
        assertEquals("Un ballon bleu", objet.getDetails())
    }

    @Test
    fun testSetDetailsVide() {
        val objet = Objet(1, "Ballon", 10, 1, TypeObjet.JOUET, "Un ballon rouge", "https://example.com/ballon.jpg")
        assertFailsWith<IllegalArgumentException>("Les détails ne peuvent pas être vides.") {
            objet.setDetails("")
        }
    }

    @Test
    fun testSetType() {
        val objet = Objet(1, "Ballon", 10, 1, TypeObjet.JOUET, "Un ballon rouge", "https://example.com/ballon.jpg")
        objet.setType(TypeObjet.NOURRITURE)
        assertEquals(TypeObjet.NOURRITURE, objet.getType())
    }

    @Test
    fun testSetNiveau() {
        val objet = Objet(1, "Ballon", 10, 1, TypeObjet.JOUET, "Un ballon rouge", "https://example.com/ballon.jpg")
        objet.setNiveau(2)
        assertEquals(2, objet.getNiveau())
    }
}
