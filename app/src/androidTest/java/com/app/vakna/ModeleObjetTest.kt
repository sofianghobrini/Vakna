import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.vakna.modele.Objet
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class ModeleObjetTest {

    private lateinit var objet: Objet

    @Before
    fun setup() {
        objet = Objet(1, "Épée légendaire", 1000, 5, "Arme", "Une épée rare et puissante")
    }

    @Test
    fun testGetId() {
        assertEquals(1, objet.getId())
    }

    @Test
    fun testGetNom() {
        assertEquals("Épée légendaire", objet.getNom())
    }

    @Test
    fun testGetPrix() {
        assertEquals(1000, objet.getPrix())
    }

    @Test
    fun testGetNiveau() {
        assertEquals(5, objet.getNiveau())
    }

    @Test
    fun testGetType() {
        assertEquals("Arme", objet.getType())
    }

    @Test
    fun testGetDetails() {
        assertEquals("Une épée rare et puissante", objet.getDetails())
    }
}
