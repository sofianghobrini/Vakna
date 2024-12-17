package com.app.vakna.modele

// Enums pour la tâche
enum class Frequence { QUOTIDIENNE, HEBDOMADAIRE, MENSUELLE }
enum class Importance { FAIBLE, MOYENNE, ELEVEE }
enum class TypeTache { PERSONNELLE, PROFESSIONNELLE, PROJET, ETUDES, SPORT, VIEQUO, AUTRE }
enum class TypeObjet { JOUET, NOURRITURE }
enum class Personnalite(val facteurFaim: Float, val facteurHumeur: Float, val facteurXp : Float, val facteurPiece : Float) {
    GOURMAND(0.75f, 1.25f,1.0f,1.0f),
    JOUEUR(1.0f, 1.25f,1.0f,1.0f),
    CALME(1.0f, 1.0f,1.0f,1.0f),
    CUPIDE(1.0f, 0.9f,1.0f,1.0f),
    AVARE(1.0f, 1.12f,1.0f,1.0f),
    GRINCHEUX(1.0f, 1.25f,1.0f,1.0f),
    RADIN(1.0f,1.0f, 1.25f,0.9f),
    GENTIL(1.0f,1.0f,1.05f, 1.25f),
    JOYEUX(1.0f,0.75f,1.0f,1.0f),
    TRAVAILLEUR(1.0f,1.0f,1.2f,1.3f)
}