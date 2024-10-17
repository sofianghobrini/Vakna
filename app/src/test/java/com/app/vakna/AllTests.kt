package com.app.vakna

import org.junit.runner.RunWith
import org.junit.runners.Suite

// Spécifie le runner de la suite de tests
@RunWith(Suite::class)
// Spécifie toutes les classes de tests à inclure dans cette suite
@Suite.SuiteClasses(
    CompagnonDAOTest::class,
    modeleCompagnonTest::class,
    modeleTacheTest::class,
    TacheDAOTest::class
)
class AllTests {
    // Cette classe ne contient aucun code
}
