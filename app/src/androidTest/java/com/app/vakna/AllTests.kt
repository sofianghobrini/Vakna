package com.app.vakna

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CompagnonDAOTest::class,
    ModeleCompagnonTest::class,
    ModeleTacheTest::class,
    TacheDAOTest::class
)
class AllTests {
    // This class remains empty, used only to hold the annotations for running the suite.
}
