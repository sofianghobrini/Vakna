package com.app.vakna

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CompagnonDAOTest::class,
    ModeleCompagnonTest::class,
    ModeleTacheTest::class,
    TacheDAOTest::class,
    ModeleObjetTest::class,
    ObjetObtenuTest::class,
    ObjetDAOTest::class,
    ModeleInventaireTest::class,
    InventaireDAOTest::class
)
class AllTests {
    // Cette classe reste vide, utilisée uniquement pour l'annotation de la suite.
}
