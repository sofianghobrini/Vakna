package com.app.vakna.modele.dao

import com.app.vakna.modele.Compagnon

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