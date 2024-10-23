package com.app.vakna

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.app.vakna.modele.GestionnaireDeCompagnons
import com.app.vakna.modele.Inventaire
import com.app.vakna.modele.ObjetObtenu
import com.app.vakna.modele.TypeObjet
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.InventaireDAO
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ModeleInventaireTest {

    private lateinit var contexte: Context
    private lateinit var inventaire: Inventaire
    private lateinit var gestionnaireCompagnons: GestionnaireDeCompagnons
    private lateinit var inventaireDAO: InventaireDAO
    private lateinit var compagnonDAO: CompagnonDAO
    private lateinit var objetObtenu: ObjetObtenu

    @Before
    fun setUp() {
        contexte = ApplicationProvider.getApplicationContext()
        inventaireDAO = InventaireDAO(contexte)
        compagnonDAO = CompagnonDAO(contexte)
        gestionnaireCompagnons = GestionnaireDeCompagnons(compagnonDAO)
        objetObtenu = ObjetObtenu(1, "Potion", 50, 2, TypeObjet.NOURRITURE, "Potion de soin", 1, "url_image")
        inventaire = Inventaire(contexte)
    }

    @Test
    fun testUtiliserObjetSucces() {
        inventaire.ajouterObjet(objetObtenu, 5)
        assertEquals(5, inventaire.getObjetParNom("Potion")!!.getQuantite())
        inventaire.utiliserObjet("Potion", 2)
        assertEquals(3, inventaire.getObjetParNom("Potion")!!.getQuantite())
        assertEquals(3, inventaireDAO.obtenirTousObjetsObtenus().first { it.getId() == 1 }.getQuantite())
    }

    @Test
    fun testUtiliserObjetQuantiteInsuffisante() {
        inventaire.ajouterObjet(objetObtenu, 1)
        assertEquals(4, inventaire.getObjetParNom("Potion")!!.getQuantite())
        assertFailsWith<AssertionError> {
            inventaire.utiliserObjet("Potion", 5)
        }
        assertEquals(4, inventaire.getObjetParNom("Potion")!!.getQuantite())
    }

    @Test
    fun testUtiliserObjetInexistant() {
        assertFailsWith<AssertionError> {
            inventaire.utiliserObjet("ObjetInconnu", 1)
        }
    }

    @Test
    fun testAjouterNouvelObjet() {
        inventaire.ajouterObjet(objetObtenu, 1)
        assertEquals(1, inventaire.getObjetParNom("Potion")!!.getQuantite())
        inventaire.ajouterObjet(objetObtenu, 3)
        assertEquals(4, inventaire.getObjetParNom("Potion")!!.getQuantite())
    }

    @Test
    fun testAjouterObjetQuantiteExistante() {
        inventaire.ajouterObjet(objetObtenu, 3)
        assertEquals(3, inventaire.getObjetParNom("Potion")!!.getQuantite())
    }

    @Test
    fun testAjouterPiecesSucces() {
        inventaire.ajouterPieces(100)
        assertEquals(100, inventaire.getPieces())
        assertEquals(100, inventaireDAO.obtenirPieces())
    }

}
