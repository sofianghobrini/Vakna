package com.app.vakna.modele

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.InventaireDAO
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ModeleInventaireTest {

    private lateinit var contexte: Context
    private lateinit var inventaire: Inventaire
    private lateinit var gestionnaireCompagnons: GestionnaireDeCompagnons
    private lateinit var inventaireDAO: InventaireDAO
    private lateinit var compagnonDAO: CompagnonDAO
    private lateinit var objetObtenu: ObjetObtenu
    private lateinit var compagnon: Compagnon

    @Before
    fun setUp() {
        contexte = ApplicationProvider.getApplicationContext()
        inventaireDAO = InventaireDAO(contexte)
        compagnonDAO = CompagnonDAO(contexte)
        gestionnaireCompagnons = GestionnaireDeCompagnons(contexte)
        objetObtenu = ObjetObtenu(12345, "Potion", 50, 2, TypeObjet.NOURRITURE, "Potion de soin", 100, "url_image")
        inventaire = Inventaire(contexte)
        compagnon = Compagnon(0, "Veolia la dragonne", espece = "Dragon", personnalite = Personnalite.GENTIL, actif = true)
        inventaire.getGestionnaireC().ajouterCompagnon(compagnon)

    }

    @BeforeEach
    fun resetAll(){
        for(Objet in inventaireDAO.obtenirTousObjetsObtenus()){
            inventaireDAO.supprimerObjetObtenu(Objet.getId())
        }
    }

    @After
    fun tearDown() {
        val fichierInventaire = File(contexte.filesDir, "inventaire.json")
        if (fichierInventaire.exists()) {
            fichierInventaire.delete()
        }
        val fichierCompagnons = File(contexte.filesDir, "compagnons.json")
        if (fichierCompagnons.exists()) {
            fichierCompagnons.delete()
        }
        resetAll()
    }

    @Test
    fun testUtiliserObjetSucces() {
        inventaire.ajouterObjet(objetObtenu, 5)
        inventaire.utiliserObjet("Potion", 2)
        assertEquals(3, inventaire.getObjetParNom("Potion")!!.getQuantite())
    }

    @Test
    fun testUtiliserObjetQuantiteInsuffisante() {
        inventaire.ajouterObjet(objetObtenu, 1)
        assertEquals(1, inventaire.getObjetParNom("Potion")!!.getQuantite())
        assertFailsWith<AssertionError> {
            inventaire.utiliserObjet("Potion", 5)
        }
        assertEquals(1, inventaire.getObjetParNom("Potion")!!.getQuantite())
    }

    @Test
    fun testUtiliserObjetInexistant() {
        assertFailsWith<AssertionError> {
            inventaire.utiliserObjet("ObjetInconnu", 1)
        }
    }

    @Test
    fun testUtiliserObjetQuantiteNulle(){
        inventaire.ajouterObjet(objetObtenu, 1)
        assertFailsWith<AssertionError> {
            inventaire.utiliserObjet("Potion", 0)
        }
    }

    @Test
    fun testAjouterObjet() {
        inventaire.ajouterObjet(objetObtenu, 1)
        assertEquals(1, inventaire.getObjetParNom("Potion")!!.getQuantite())
    }

    @Test
    fun testAjouterObjetExistant() {
        inventaire.ajouterObjet(objetObtenu, 1)
        assertEquals(1, inventaire.getObjetParNom("Potion")!!.getQuantite())
        inventaire.ajouterObjet(objetObtenu, 3)
        assertEquals(4, inventaire.getObjetParNom("Potion")!!.getQuantite())
    }

    @Test
    fun testAjouterPiecesSucces() {
        val piecesInitiales = inventaire.getPieces()
        inventaire.ajouterPieces(100)
        assertEquals(piecesInitiales+ 100, inventaire.getPieces())
        assertEquals(piecesInitiales+ 100, inventaireDAO.obtenirPieces())
    }

    @Test
    fun ajouterPiecesNegatifAssertionError() {
        val piecesInitiales = inventaire.getPieces()
        val valeurNegative = -piecesInitiales - 1
        assertFailsWith<AssertionError> {
            inventaire.ajouterPieces(valeurNegative)
        }
    }

}
