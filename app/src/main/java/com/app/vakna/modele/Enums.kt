package com.app.vakna.modele

// Enums pour la tâche
enum class Frequence { QUOTIDIENNE, HEBDOMADAIRE, MENSUELLE }
enum class Importance { FAIBLE, MOYENNE, ELEVEE }
enum class TypeTache { PERSONNELLE, PROFESSIONNELLE, PROJET, ETUDES, SPORT, VIEQUO, AUTRE }
enum class TypeObjet { JOUET, NOURRITURE }
enum class Personnalite(val facteurFaim: Float, val facteurHumeur: Float, val facteurPrix : Float, val facteurXp : Float ) {
    GOURMAND(0.75f, 1.25f,1.0f,1.0f),
    JOUEUR(1.0f, 1.25f,1.0f,1.0f),
    CALME(1.0f, 1.0f,1.0f,1.0f),
    CUPIDE(1.0f, 0.9f,1.0f,1.0f),
    AVARE(1.0f, 1.25f,1.0f,1.0f),
    GRINCHEUX(1.0f, 1.25f,1.0f,1.0f),
    RADIN(1.0f,1.0f, 1.25f,1.0f),
    GENTIL(1.0f,1.0f,0.75f, 1.0f),
    JOYEUX(1.0f,0.75f,1.0f,1.0f),
    RACISTE(2.0f,2.0f,2.0f,2.0f)
}