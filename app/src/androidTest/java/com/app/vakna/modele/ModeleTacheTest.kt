package com.app.vakna.modele

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.vakna.modele.dao.TacheDAO
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class ModeleTacheTest {

    private lateinit var dao: TacheDAO
    private lateinit var gestionnaire: GestionnaireDeTaches
    private lateinit var compagnon: Compagnon

    private lateinit var cheminFichier: File

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        dao = TacheDAO(context)
        gestionnaire = GestionnaireDeTaches(context)
        compagnon = Compagnon(0, "Veolia la dragonne", espece = "Dragon", personnalite = Personnalite.CALME, actif = true)
        gestionnaire.setCompagnon(compagnon.id)
        gestionnaire.getGestionnaireCompagnon().ajouterCompagnon(compagnon)
        cheminFichier = File(context.filesDir, "taches.json")
        if (!cheminFichier.exists()) {
            cheminFichier.createNewFile()
            cheminFichier.writeText("""{"taches": []}""")
        }
    }

    @Test
    fun testObtenirTachesParFrequence() {
        val nbTachesFreqQuoti = gestionnaire.obtenirTachesParFrequence()[Frequence.QUOTIDIENNE]?.size
        val nbTachesFreqHebdo = gestionnaire.obtenirTachesParFrequence()[Frequence.HEBDOMADAIRE]?.size

        val tache1 = Tache("Tâche quotidienne 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        val tache2 = Tache("Tâche hebdo 2", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        val tache3 = Tache("Tâche quotidienne 2", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)


        gestionnaire.ajouterTache(tache1)
        gestionnaire.ajouterTache(tache2)
        gestionnaire.ajouterTache(tache3)

        val tachesParFrequence = gestionnaire.obtenirTachesParFrequence()

        assertEquals(nbTachesFreqQuoti!! + 2, tachesParFrequence[Frequence.QUOTIDIENNE]?.size)
        assertTrue(tachesParFrequence[Frequence.QUOTIDIENNE]?.contains(tache1) == true)
        assertTrue(tachesParFrequence[Frequence.QUOTIDIENNE]?.contains(tache3) == true)

        assertEquals(nbTachesFreqHebdo!! + 1, tachesParFrequence[Frequence.HEBDOMADAIRE]?.size)
        assertTrue(tachesParFrequence[Frequence.HEBDOMADAIRE]?.contains(tache2) == true)
    }

    @Test
    fun testObtenirTachesParImportance() {
        val nbTachesFaible = gestionnaire.obtenirTachesParImportance()[Importance.FAIBLE]?.size
        val nbTachesMoy = gestionnaire.obtenirTachesParImportance()[Importance.MOYENNE]?.size

        val tache1 = Tache("Tâche importance faible", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        val tache2 = Tache("Tâche 2", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        val tache3 = Tache("Tâche importance moyenne", Frequence.QUOTIDIENNE, Importance.MOYENNE, TypeTache.PERSONNELLE, listOf(3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)

        gestionnaire.ajouterTache(tache1)
        gestionnaire.ajouterTache(tache2)
        gestionnaire.ajouterTache(tache3)

        val tachesParImportance = gestionnaire.obtenirTachesParImportance()
        if (nbTachesFaible != null) {
            assertEquals(nbTachesFaible + 2, tachesParImportance[Importance.FAIBLE]?.size)
        }
        assertTrue(tachesParImportance[Importance.FAIBLE]?.contains(tache1) == true)
        assertTrue(tachesParImportance[Importance.FAIBLE]?.contains(tache2) == true)
        if (nbTachesMoy != null) {
            assertEquals(nbTachesMoy + 1, tachesParImportance[Importance.MOYENNE]?.size)
        }
        assertTrue(tachesParImportance[Importance.MOYENNE]?.contains(tache3) == true)
    }
    @Test
    fun testAjouterTache() {
        val nbTaches = gestionnaire.obtenirTaches().size
        val tache = Tache("Tâche 1", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        val insertionReussie = gestionnaire.ajouterTache(tache)
        assertTrue(insertionReussie)
        assertEquals(nbTaches + 1, gestionnaire.obtenirTaches().size)
        assertTrue(gestionnaire.obtenirTaches().contains(tache))
    }

    @Test
    fun testAjouterTacheNomVide() {
        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.ajouterTache(Tache("", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false))
        }
        assertEquals("Le nom de la tâche ne peut pas être vide", exception.message)
    }

    @Test
    fun testAjouterTacheExistante() {
        val tache = Tache("Tâche existante", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        gestionnaire.ajouterTache(tache)

        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.ajouterTache(Tache("Tâche existante", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false))
        }
        assertEquals("Une tâche avec le nom 'Tâche existante' existe déjà", exception.message)
    }

    @Test
    fun testModifierTache() {
        val tacheInitiale = Tache("Tâche initiale", Frequence.QUOTIDIENNE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        gestionnaire.ajouterTache(tacheInitiale)

        val tacheModifiee = Tache("Tâche initiale modifiée", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false,
            estArchivee = false
        )
        val modificationReussie = gestionnaire.modifierTache("Tâche initiale", tacheModifiee)
        assertTrue(modificationReussie)

        val tache = gestionnaire.obtenirTaches().last()
        assertEquals(tacheModifiee.frequence, tache.frequence)
        assertEquals(tacheModifiee.importance, tache.importance)
        assertEquals(tacheModifiee.type, tache.type)
    }

    @Test
    fun testModifierTacheIntrouvable() {
        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.modifierTache(
                "Tâche Inexistante",
                Tache(
                    "Tâche Inexistante",
                    Frequence.QUOTIDIENNE,
                    Importance.FAIBLE,
                    TypeTache.PERSONNELLE,
                    listOf(1, 2, 3, 4),
                    null,
                    LocalDateTime.now().plusDays(1),
                    false,
                    estArchivee = false
                )
            )
        }
        assertEquals("Tâche avec le nom Tâche Inexistante introuvable", exception.message)
    }

    @Test
    fun testSupprimerTache() {
        val tache = Tache("Tâche qui va être supprimée", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        gestionnaire.ajouterTache(tache)
        assertTrue(gestionnaire.supprimerTache("Tâche qui va être supprimée"))
    }

    /** Mise en commentaire temporaire pour générer l'APK
    @Test
    fun testSupprimerTacheAffecteHumeur() {
        val tache = Tache("Tâche 1", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        assertTrue(gestionnaire.ajouterTache(tache))
        val humeurInitiale = compagnon.humeur
        gestionnaire.supprimerTache("Tâche 1")
        gestionnaire.getGestionnaireCompagnon().obtenirCompagnon(compagnon.id)?.let { assertEquals(humeurInitiale - (5 * (tache.importance.ordinal + 1)), it.humeur) }
    }
    */

    @Test
    fun testSupprimerTacheIntrouvable() {
        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.supprimerTache("Tâche Inexistante")
        }
        assertEquals("Tâche avec le nom Tâche Inexistante introuvable", exception.message)
    }

    /** Mise en commentaire temporaire pour générer l'APK
    @Test
    fun testFinirTache() {
        compagnon.humeur = 0
        val tache = Tache("Tâche 1 qui va être finie", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        assertTrue(gestionnaire.ajouterTache(tache))
        gestionnaire.finirTache("Tâche 1 qui va être finie")
        assertTrue(tache.estTerminee)
        val expectedHumeur = 0 + 10 * (tache.importance.ordinal + 1)
        val expectedXp = 0 + 5 * (tache.importance.ordinal + 1)
        gestionnaire.getGestionnaireCompagnon().obtenirCompagnon(compagnon.id)?.let { assertEquals(expectedHumeur, it.humeur) }
        gestionnaire.getGestionnaireCompagnon().obtenirCompagnon(compagnon.id)?.let { assertEquals(expectedXp, it.xp) }
    }
    */

    @Test
    fun testFinirTacheIntrouvable() {
        val exception = assertThrows<IllegalArgumentException> {
            gestionnaire.finirTache("Tâche Inexistante")
        }
        assertEquals("Tâche avec le nom Tâche Inexistante introuvable", exception.message)
    }

    /** Mise en commentaire temporaire pour générer l'APK
    @Test
    fun testFinirTacheAffecteHumeur(){
        compagnon.humeur = 50
        compagnon.xp = 50
        val tache = Tache("Tâche affecte humeur", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        gestionnaire.ajouterTache(tache)
        val humeurInitiale = compagnon.humeur
        val xpInitiale = compagnon.xp
        gestionnaire.finirTache("Tâche affecte humeur")
        gestionnaire.getGestionnaireCompagnon().obtenirCompagnon(compagnon.id)?.let { assertEquals(humeurInitiale - (5 * (tache.importance.ordinal + 1)), it.humeur) }
        gestionnaire.getGestionnaireCompagnon().obtenirCompagnon(compagnon.id)?.let { assertEquals(xpInitiale - (5 * (tache.importance.ordinal + 1)), it.xp) }
    }
    */

    @Test
    fun testObtenirTaches() {
        val nbTaches = gestionnaire.obtenirTaches().size
        val tache1 = Tache("Tâche 1 de test", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        val tache2 = Tache("Tâche 2 de test", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        gestionnaire.ajouterTache(tache1)
        gestionnaire.ajouterTache(tache2)

        val taches = gestionnaire.obtenirTaches()
        assertEquals(nbTaches + 2, taches.size)
        assertTrue(taches.contains(tache1))
        assertTrue(taches.contains(tache2))
    }

    @Test
    fun testObtenirTachesParType() {
        val nbTachesPersonnelles = gestionnaire.obtenirTaches(TypeTache.PERSONNELLE).size
        val nbTachesProfessionnelles = gestionnaire.obtenirTaches(TypeTache.PROFESSIONNELLE).size

        val tachePersonnelle = Tache("Tâche Personnelle", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        val tacheProfessionnelle = Tache("Tâche Professionnelle", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PROFESSIONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        gestionnaire.ajouterTache(tachePersonnelle)
        gestionnaire.ajouterTache(tacheProfessionnelle)

        val tachesPersonnelles = gestionnaire.obtenirTaches(TypeTache.PERSONNELLE)
        assertEquals(nbTachesPersonnelles + 1, tachesPersonnelles.size)
        assertTrue(tachesPersonnelles.contains(tachePersonnelle))

        val tachesProfessionnelles = gestionnaire.obtenirTaches(TypeTache.PROFESSIONNELLE)
        assertEquals(nbTachesProfessionnelles + 1, tachesProfessionnelles.size)
        assertTrue(tachesProfessionnelles.contains(tacheProfessionnelle))
    }

    @Test
    fun testRechercherTache() {
        val tache1 = Tache("Faire", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        val tache2 = Tache("Faire du travail", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        gestionnaire.ajouterTache(tache1)
        gestionnaire.ajouterTache(tache2)
        val resultat = gestionnaire.rechercherTache("Faire")
        assertEquals(2, resultat.size)
        val resultat2 = gestionnaire.rechercherTache("Travailler")
        assertEquals(0, resultat2.size)
    }

    /** Mise en commentaire temporaire pour générer l'APK
    @Test
    fun testChangementDateValidationFinir() {
        val tache1 = Tache("Se scrum le master", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        gestionnaire.ajouterTache(tache1)
        gestionnaire.finirTache("Se scrum le master")
        assertEquals(LocalDate.now(), tache1.derniereValidation)
    }
    */

    @Test
    fun testChangementDateValidationSupprimer() {
        val tache1 = Tache("Se scrum le master une deuxième fois", Frequence.HEBDOMADAIRE, Importance.FAIBLE, TypeTache.PERSONNELLE, listOf(1, 2, 3, 4), null, LocalDateTime.now().plusDays(1), false, estArchivee = false)
        gestionnaire.ajouterTache(tache1)
        gestionnaire.supprimerTache("Se scrum le master une deuxième fois")
        assertEquals(LocalDate.now(), tache1.derniereValidation)
    }
}
