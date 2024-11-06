package com.app.vakna.modele

sealed class Frequence {
    object Quotidienne : Frequence()
    data class Hebdomadaire(val jours: Set<JourSemaine>) : Frequence()
    data class Mensuelle(val joursDuMois: Set<Int>) : Frequence()
}