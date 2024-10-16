package com.app.vakna.modele.dao

import com.app.vakna.modele.Compagnon
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.Importance
import com.app.vakna.modele.Tache
import com.app.vakna.modele.TypeTache
import java.time.LocalDate

fun main() {
    val truc = CompagnonDAO()
    val compaTest = Compagnon("Prout", espece = "Chat")
    val compaTest2 = Compagnon("Caca", espece = "Chat")

    truc.obtenirTous().forEach { println(it) }
    println()

    truc.inserer(compaTest)
    truc.obtenirTous().forEach { println(it) }
    println()

    truc.modifier("Prout", compaTest2)
    truc.obtenirTous().forEach { println(it) }
    println()

    truc.supprimer(compaTest2.nom)
    truc.obtenirTous().forEach { println(it) }
    println()

    println(truc.obtenirParId("Vakno"))
}