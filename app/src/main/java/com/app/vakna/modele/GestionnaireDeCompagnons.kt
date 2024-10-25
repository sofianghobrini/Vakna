package com.app.vakna.modele

import android.content.Context
import android.util.Log
import com.app.vakna.modele.dao.CompagnonDAO
import android.os.Handler
import android.os.Looper
import com.app.vakna.controller.ControllerCompagnon


class GestionnaireDeCompagnons(private var dao : CompagnonDAO) {
    private val setDeCompagnons = mutableSetOf<Compagnon>()
    private val handler = Handler(Looper.getMainLooper())
    private val intervalleFaim = 10 * 1000L  // apres la presentation le mettre en 30 min
    private val intervalleBonheur =  10 * 1000L  // Quatre heures en millisecondes

    init {
        dao.obtenirTous().forEach { setDeCompagnons.add(it) }
    }

    fun ajouterCompagnon(compagnon: Compagnon): Boolean {
        if (compagnon.nom.isBlank()) {
            throw IllegalArgumentException("Le nom du compagnon ne peut pas être vide")
        }
        if (!setDeCompagnons.add(compagnon)) {
            throw IllegalArgumentException("Une tâche avec le nom '${compagnon.nom}' existe déjà")
        }
        return dao.inserer(compagnon)
    }

    fun modifierCompagnon(id: Int, nouveauCompagnon: Compagnon): Boolean {
        val compagnon = setDeCompagnons.find { it.id == id }
        if (compagnon != null) {
            compagnon.nom = nouveauCompagnon.nom
            compagnon.faim = nouveauCompagnon.faim
            compagnon.humeur = nouveauCompagnon.humeur
            compagnon.xp = nouveauCompagnon.xp
            compagnon.espece = nouveauCompagnon.espece

            return dao.modifier(id, nouveauCompagnon)
        } else {
            throw IllegalArgumentException("Compagnon avec l'id $id introuvable")
        }
    }

    fun modifierNom(id: Int, nom: String) {
        val compagnon = setDeCompagnons.find { it.id == id }?: return

        compagnon.nom = nom
        dao.modifier(id, compagnon)
    }

    fun modifierFaim(id: Int, niveau: Int) {
        // Vérification que le niveau est compris entre -100 et 100
        assert(niveau in -100..100) { "Le niveau de faim doit être compris entre -100 et 100." }
        val compagnon = setDeCompagnons.find { it.id == id }?: return

        compagnon.faim += niveau
        compagnon.faim = compagnon.faim.coerceIn(0, 100)
        dao.modifier(id, compagnon)
    }

    // Méthode pour modifier le niveau d'humeur du compagnon
    fun modifierHumeur(id: Int, niveau: Int) {
        // Vérification que le niveau est compris entre -100 et 100
        assert(niveau in -100..100) { "Le niveau d'humeur doit être compris entre -100 et 100." }
        val compagnon = setDeCompagnons.find { it.id == id }?: return

        // Modification du niveau d'humeur et forçage de la valeur entre 0 et 100
        compagnon.humeur += niveau
        compagnon.humeur = compagnon.humeur.coerceIn(0, 100)
        dao.modifier(id, compagnon)
    }

    // Méthode pour ajouter de l'expérience (XP) au compagnon
    fun gagnerXp(id: Int, montant: Int) {

        //assert(0 < montant) { "Le montant d'xp a gagné doit être strictement positif" }
        Log.i("test2", "gagnerXP montant : $montant, idCompa : $id")
        val compagnon = setDeCompagnons.find { it.id == id }?: return
        compagnon.xp += montant
        Log.i("test2", "ca a marché ????")
        dao.modifier(id, compagnon)
    }

    fun obtenirCompagnons(): Set<Compagnon> {
        dao.obtenirTous().forEach { setDeCompagnons.add(it) }
        return setDeCompagnons
    }

    fun obtenirCompagnon(id: Int): Compagnon? {
        return obtenirCompagnons().find { it.id == id }
    }

    fun baisserNivFaim(id :Int, context: Context, lastLaunch: Long?) {
        val compagnon = setDeCompagnons.find { it.id == id }?: return
        if(lastLaunch != null) {
            // Calculer le temps écoulé depuis la dernière utilisation en minutes
            val currentTime = System.currentTimeMillis()
            //val elapsedTime = (currentTime - lastUsageTime) / (1000 * 60)  // Temps en minutes
            val elapsedTime = (currentTime - lastLaunch) / 1000  // Temps en secondes

            // Calculer combien de points de faim ont été perdus pendant l'absence apres la présentation mettre en 30 min
            //val pointsToReduce = (elapsedTime / 30).toInt()
            var pointsToReduce = (elapsedTime / 10).toInt()
            pointsToReduce = if(pointsToReduce > 100) 100 else pointsToReduce
            modifierHumeur(id, -pointsToReduce)

            modifierFaim(id, -pointsToReduce)
        }

        val faimRunnable = object : Runnable {
            override fun run() {
                modifierFaim(id, -1)
                handler.postDelayed(this, intervalleFaim)  // Reprogrammer pour toutes les 30 minutes
            }
        }
        handler.post(faimRunnable)  // Lancer la première réduction de faim
    }
    fun baisserNivHumeur(id: Int, context: Context, lastLaunch: Long?){
        val compagnon = setDeCompagnons.find { it.id == id } ?: return

        if(lastLaunch != null) {
            val currentTime = System.currentTimeMillis()
            //val elapsedTime = (currentTime - lastUsageTime) / (1000 * 60)
            val elapsedTime = (currentTime - lastLaunch) / 1000  // Temps en secondes

            //val pointsToReduce = (elapsedTime / (4 * 60)).toInt()
            var pointsToReduce = (elapsedTime / 10).toInt()
            pointsToReduce = if(pointsToReduce > 100) 100 else pointsToReduce
            modifierHumeur(id, -pointsToReduce)
        }

        val bonheurRunnable = object : Runnable {
            override fun run() {
                modifierHumeur(id, -1)
                handler.postDelayed(this, intervalleBonheur)
            }
        }
        handler.post(bonheurRunnable)
    }
}