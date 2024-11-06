package com.app.vakna.dao

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.vakna.modele.dao.InventaireDAO
import com.app.vakna.modele.ObjetObtenu
import com.app.vakna.modele.TypeObjet
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import java.io.File
import kotlin.test.*

@RunWith(AndroidJUnit4::class)
class InventaireDAOTest {

    private lateinit var dao: InventaireDAO
    private lateinit var context: Context
    private lateinit var cheminFichier: File
    private var backupFile: File? = null

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        dao = InventaireDAO(context)
        cheminFichier = File(context.filesDir, "inventaire.json")
        if (cheminFichier.exists()) {
            backupFile = File(context.filesDir, "inventaire.json.bak")
            cheminFichier.copyTo(backupFile!!, overwrite = true)
        } else {
            cheminFichier.createNewFile()
            cheminFichier.writeText("""{"pieces": 0, "objets_obtenus": []}""")
        }
    }

    @BeforeEach
    fun resetAll(){
        for(objet in dao.obtenirTousObjetsObtenus()){
            dao.supprimerObjetObtenu(objet.getId())
        }
        dao.mettreAJourPieces(0)
    }

    @After
    fun tearDown() {
        backupFile?.let {
            if (it.exists()) {
                it.copyTo(cheminFichier, overwrite = true)
                it.delete()
            }
        }
        resetAll()
    }

    @Test
    fun testMettreAJourPieces() {
        val pieces = dao.obtenirPieces()
        assertEquals(0, pieces)
        dao.mettreAJourPieces(100)
        assertEquals(100, dao.obtenirPieces())
    }

    @Test
    fun testInsererObjetObtenuSucces() {
        val nbObjets = dao.obtenirTousObjetsObtenus().size
        assertEquals(0, nbObjets)
        assertTrue(dao.insererObjetObtenu(ObjetObtenu(12345, "Potion", 50, 1, TypeObjet.NOURRITURE, "Potion de soin", 2, "url_image")))
        assertEquals(nbObjets + 1, dao.obtenirTousObjetsObtenus().size)
    }

    @Test
    fun testInsererObjetObtenuDejaExistant() {
        val nouvelObjet = ObjetObtenu(12345, "Potion", 50, 1, TypeObjet.NOURRITURE, "Potion de soin", 2, "url_image")
        dao.insererObjetObtenu(nouvelObjet)
        val objets = dao.obtenirTousObjetsObtenus() // Récup les objets dans l'inventaire
        assertEquals(1, objets.size) // Normalement 1
        assertFalse(dao.insererObjetObtenu(nouvelObjet)) // Echec parce que déjà existant
        assertEquals(1, objets.size) // Normalement 1 toujours car Echec
    }

    @Test
    fun testMettreAJourQuantiteObjetSucces() {
        val nouvelObjet = ObjetObtenu(1, "Potion", 50, 1, TypeObjet.NOURRITURE, "Potion de soin", 2, "url_image")
        dao.insererObjetObtenu(nouvelObjet)
        assertTrue(dao.mettreAJourQuantiteObjet(1, 5))
        val objets = dao.obtenirTousObjetsObtenus()
        assertEquals(5, objets[0].getQuantite())
    }

    @Test
    fun testMettreAJourQuantiteObjetObjNonTrouve() { // ID introuvable en gros
        assertFalse(dao.mettreAJourQuantiteObjet(99, 5))
    }

    @Test
    fun testObtenirTousObjetsObtenusVide() {
        assertTrue(dao.obtenirTousObjetsObtenus().isEmpty())
    }

    @Test
    fun testObtenirParType() {
        dao.insererObjetObtenu(ObjetObtenu(1, "Potion", 50, 1, TypeObjet.NOURRITURE, "Potion de soin", 2, "url_image"))
        dao.insererObjetObtenu(ObjetObtenu(2, "Jouet", 30, 1, TypeObjet.JOUET, "Jouet amusant", 1, "url_image"))
        val nourritures = dao.obtenirParType(TypeObjet.NOURRITURE)
        val jouets = dao.obtenirParType(TypeObjet.JOUET)
        assertEquals(1, nourritures.size)
        assertEquals("Potion", nourritures[0].getNom())
        assertEquals(1, jouets.size)
        assertEquals("Jouet", jouets[0].getNom())
    }
}
