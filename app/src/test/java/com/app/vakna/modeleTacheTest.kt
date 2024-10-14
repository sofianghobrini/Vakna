import com.app.vakna.modele.Frequence
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.Importance
import com.app.vakna.modele.Tache
import com.app.vakna.modele.TypeTache
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.time.LocalDate

class ModeleTacheTest {

    private val gestionnaire = GestionnaireDeTaches()

    @Test
    fun testAjouterTache() {
        val tache = Tache(
            nom = "Faire les courses",
            frequence = Frequence.HEBDOMADAIRE,
            importance = Importance.MOYENNE,
            type = TypeTache.PERSONNELLE,
            date = LocalDate.now(),
            gestionnaire = gestionnaire
        )
        gestionnaire.ajouterTache(tache)

        val taches = gestionnaire.obtenirTaches()
        assertEquals(1, taches.size)
        assertEquals("Faire les courses", taches[0].nom)
    }
    @Test
    fun testModifierTache() {
        val tache = Tache(
            nom = "Faire les courses",
            frequence = Frequence.HEBDOMADAIRE,
            importance = Importance.MOYENNE,
            type = TypeTache.PERSONNELLE,
            date = LocalDate.now(),
            gestionnaire = gestionnaire
        )
        gestionnaire.ajouterTache(tache)

        val nouvelleTache = Tache(
            nom = "Faire la lessive",
            frequence = Frequence.HEBDOMADAIRE,
            importance = Importance.MOYENNE,
            type = TypeTache.PERSONNELLE,
            date = LocalDate.now(),
            gestionnaire = gestionnaire
        )
        gestionnaire.modifierTache("Faire les courses", nouvelleTache)

        val taches = gestionnaire.obtenirTaches()
        assertEquals(1, taches.size)
        assertEquals("Faire la lessive", taches[0].nom)
    }

    @Test
    fun testSupprimerTache() {
        val tache = Tache(
            nom = "Faire les courses",
            frequence = Frequence.HEBDOMADAIRE,
            importance = Importance.MOYENNE,
            type = TypeTache.PERSONNELLE,
            date = LocalDate.now(),
            gestionnaire = gestionnaire
        )
        gestionnaire.ajouterTache(tache)
        assertEquals(1, gestionnaire.obtenirTaches().size)

        gestionnaire.supprimerTache("Faire les courses")
        assertEquals(0, gestionnaire.obtenirTaches().size)
    }

    @Test
    fun testFinirTache() {
        val tache = Tache(
            nom = "Faire les courses",
            frequence = Frequence.HEBDOMADAIRE,
            importance = Importance.MOYENNE,
            type = TypeTache.PERSONNELLE,
            date = LocalDate.now(),
            gestionnaire = gestionnaire
        )
        gestionnaire.ajouterTache(tache)

        gestionnaire.finirTache("Faire les courses")
        val tacheModifiee = gestionnaire.obtenirTaches().first()

        assertTrue(tacheModifiee.estTerminee)
    }

    @Test
    fun testModifierTacheIntrouvable() {
        val tache = Tache(
            nom = "Une tâche",
            frequence = Frequence.HEBDOMADAIRE,
            importance = Importance.MOYENNE,
            type = TypeTache.PERSONNELLE,
            date = LocalDate.now(),
            gestionnaire = gestionnaire
        )
        gestionnaire.ajouterTache(tache)

        val nouvelleTache = Tache(
            nom = "Nouvelle tâche",
            frequence = Frequence.HEBDOMADAIRE,
            importance = Importance.MOYENNE,
            type = TypeTache.PERSONNELLE,
            date = LocalDate.now(),
            gestionnaire = gestionnaire
        )

        try {
            gestionnaire.modifierTache("Tâche inexistante", nouvelleTache)
            fail("Une exception aurait dû être levée pour une tâche introuvable.")
        } catch (e: IllegalArgumentException) {
            assertEquals("Tâche avec le nom Tâche inexistante introuvable", e.message)
        }
    }

    @Test
    fun testSupprimerTacheIntrouvable() {
        try {
            gestionnaire.supprimerTache("Tâche inexistante")
            fail("Une exception aurait dû être levée pour une tâche introuvable.")
        } catch (e: IllegalArgumentException) {
            assertEquals("Tâche avec le nom Tâche inexistante introuvable", e.message)
        }
    }

    @Test
    fun testFinirTacheIntrouvable() {
        try {
            gestionnaire.finirTache("Tâche inexistante")
            fail("Une exception aurait dû être levée pour une tâche introuvable.")
        } catch (e: IllegalArgumentException) {
            assertEquals("Tâche avec le nom Tâche inexistante introuvable", e.message)
        }
    }
}
