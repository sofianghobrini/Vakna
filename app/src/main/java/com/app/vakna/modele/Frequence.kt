package com.app.vakna.modele

sealed class Frequence {
    data object QUOTIDIENNE : Frequence()
    data class HEBDOMADAIRE(val jours: Set<JourSemaine>) : Frequence()
    data class MENSUELLE(val joursDuMois: Set<Int>) : Frequence()
}