package com.app.vakna

import com.app.vakna.dao.*
import com.app.vakna.modele.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CompagnonDAOTest::class,
    TacheDAOTest::class,
    ObjetDAOTest::class,
    InventaireDAOTest::class,
    RefugeDAOTest::class,

    ModeleCompagnonTest::class,
    ModeleTacheTest::class,
    ModeleObjetTest::class,
    ModeleInventaireTest::class,
    ObjetObtenuTest::class,
    RefugeTest::class,
    GestionnaireDeRefugeTest::class

)
class AllTests {
    // Cette classe reste vide, utilisée uniquement pour l'annotation de la suite.
}
