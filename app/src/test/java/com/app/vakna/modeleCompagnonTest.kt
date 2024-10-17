package com.app.vakna

import com.app.vakna.modele.Compagnon
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.test.assertEquals

class modeleCompagnonTest {

    // Chemin vers le fichier compagnon.json
    private val cheminFichier = System.getProperty("user.dir")?.plus("/src/bdd/compagnon.json") ?: ""
    private lateinit var backupFilePath: Path
    private lateinit var originalFilePath: Path
    private val compagnon = Compagnon(0, "Veolia la dragonne", espece = "Dragon")

    @Before
    fun setUp() {
        // Chemin vers le fichier original
        originalFilePath = Paths.get(cheminFichier)

        // Chemin vers le fichier de sauvegarde
        backupFilePath = Paths.get(cheminFichier + ".bak")

        // Crée une sauvegarde du fichier original avant chaque test
        if (Files.exists(originalFilePath)) {
            Files.copy(originalFilePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING)
        } else {
            throw IllegalStateException("Le fichier compagnon.json est introuvable.")
        }
    }

    @After
    fun tearDown() {
        // Restaure le fichier original après chaque test
        if (Files.exists(backupFilePath)) {
            Files.copy(backupFilePath, originalFilePath, StandardCopyOption.REPLACE_EXISTING)
            Files.delete(backupFilePath)
        }
    }

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
