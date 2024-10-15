package com.app.vakna.modele.dao

import com.app.vakna.modele.Frequence
import com.app.vakna.modele.Importance
import com.app.vakna.modele.Tache
import com.app.vakna.modele.TypeTache
import java.time.LocalDate

fun main() {
    val tacheTest = Tache("Faire des tests", Frequence.QUOTIDIENNE, Importance.ELEVEE, TypeTache.PROFESSIONNELLE, LocalDate.parse("2024-10-15"), false)
    val tacheTest2 = Tache("Faire des tests mieux", Frequence.QUOTIDIENNE, Importance.ELEVEE, TypeTache.PROFESSIONNELLE, LocalDate.parse("2024-10-15"), false)
    val truc = TacheDAO()

    println(truc.obtenirTous())
    println("")

    println(truc.obtenirParId("Faire du sport"))
    println("")


    truc.inserer(tacheTest)
    truc.obtenirTous().forEach { println(it) }
    println("")

    truc.modifier("Faire des tests", tacheTest2)
    truc.obtenirTous().forEach { println(it) }
    println("")

    truc.supprimer("Faire des tests mieux")
    truc.obtenirTous().forEach({println(it)})
    println("")



}