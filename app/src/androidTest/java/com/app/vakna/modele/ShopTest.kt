package com.app.vakna.modele

import androidx.test.core.app.ApplicationProvider
import com.app.vakna.modele.dao.InventaireDAO
import com.app.vakna.modele.dao.ObjetDAO
import com.app.vakna.modele.dao.CompagnonDAO
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ShopTest {

    private lateinit var shop: Shop
    private lateinit var inventaireDAO: InventaireDAO
    private lateinit var objetDAO: ObjetDAO
    private lateinit var inventaire: Inventaire


    @Before
    fun setUp() {
        // Initialize context using ApplicationProvider
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        // Initialize the DAOs
        inventaireDAO = InventaireDAO(context)
        objetDAO = ObjetDAO(context)

        val gestionnaireDeCompagnons = GestionnaireDeCompagnons(context)

        // Initialize the shop
        shop = Shop(context)

        // Initialize an inventory with 100 pieces and some existing objects
        inventaire = Inventaire(context)

        // Manually insert objects into ObjetDAO
        val potion = Objet(1, "Potion", 10, 1, TypeObjet.NOURRITURE, "Restores health", "https://example.com/potion.jpg")
        val sword = Objet(2, "Sword", 50, 5, TypeObjet.JOUET, "A sharp weapon", "https://example.com/sword.jpg")

        objetDAO.inserer(potion)
        objetDAO.inserer(sword)
    }

    @Test
    fun testGetObjetReturnsObject() {
        // Verify that an object can be fetched from the shop
        val objet = shop.getObjet("Potion")
        assertNotNull(objet)
        assertEquals("Potion", objet?.getNom())
        assertEquals(10, objet?.getPrix())
    }

    @Test
    fun testGetObjetReturnsNullForNonExistingObject() {
        // Verify that null is returned for non-existing object
        val objet = shop.getObjet("NonExistingItem")
        assertNull(objet)
    }

    @Test
    fun testAcheterUpdatesInventory() {
        // Buy 2 Potions
        shop.acheter("Potion", 2)

        // Verify that the quantity and pieces have been updated correctly
        assertEquals(80, inventaire.getPieces()) // 100 - 2 * 10 = 80

        // Verify that the object has been added or updated in the inventory
        val objetsObtenus = inventaire.getObjets()
        assertEquals(1, objetsObtenus.size) // Expect only 1 entry for "Potion"
        assertEquals("Potion", objetsObtenus[0].getNom())
        assertEquals(2, objetsObtenus[0].getQuantite()) // Should have exactly 2 Potions after purchase
    }

    @Test
    fun testAcheterFailsForInsufficientFunds() {
        // Try to buy 3 Swords, but the inventory has only 100 pieces (3 * 50 = 150)
        shop.acheter("Sword", 3)

        // Verify that the purchase fails and nothing is updated in the inventory
        assertEquals(100, inventaire.getPieces())
        assertTrue(inventaire.getObjets().isEmpty())
    }

    @Test
    fun testListerObjetByType() {
        // List objects by type NOURRITURE
        val nourritureList = shop.listerObjet(TypeObjet.NOURRITURE)
        assertEquals(1, nourritureList.size)
        assertEquals("Potion", nourritureList[0].getNom())

        // List objects by type JOUET
        val armeList = shop.listerObjet(TypeObjet.JOUET)
        assertEquals(1, armeList.size)
        assertEquals("Sword", armeList[0].getNom())
    }

}
