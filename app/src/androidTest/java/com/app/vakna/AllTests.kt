package com.app.vakna

import com.app.vakna.dao.CompagnonDAOTest
import com.app.vakna.dao.InventaireDAOTest
import com.app.vakna.dao.ObjetDAOTest
import com.app.vakna.dao.RefugeDAOTest
import com.app.vakna.dao.TacheDAOTest
import com.app.vakna.modele.ModeleCompagnonTest
import com.app.vakna.modele.ModeleInventaireTest
import com.app.vakna.modele.ModeleObjetTest
import com.app.vakna.modele.ModeleTacheTest
import com.app.vakna.modele.ObjetObtenuTest
import com.app.vakna.modele.RefugeTest
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
    RefugeTest::class

)
class AllTests {
    // Cette classe reste vide, utilisée uniquement pour l'annotation de la suite.
}
