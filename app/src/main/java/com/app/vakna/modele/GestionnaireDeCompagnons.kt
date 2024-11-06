package com.app.vakna.modele

import com.app.vakna.modele.dao.CompagnonDAO
import android.os.Handler
import android.os.Looper

class GestionnaireDeCompagnons(private var dao : CompagnonDAO) {
    private val setDeCompagnons = mutableSetOf<Compagnon>()
    private val handler = Handler(Looper.getMainLooper())
    private val intervalleFaim = 30 * 60000L
    private val intervalleBonheur =  4 * 3600000L

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
        val compagnon = setDeCompagnons.find { it.id == id }?: return
        compagnon.xp += montant
        dao.modifier(id, compagnon)
    }

    fun obtenirCompagnons(): Set<Compagnon> {
        dao.obtenirTous().forEach { setDeCompagnons.add(it) }
        return setDeCompagnons
    }

    fun obtenirCompagnon(id: Int): Compagnon? {
        return obtenirCompagnons().find { it.id == id }
    }

    fun baisserNivFaim(id: Int, lastLaunch: Long?) {
        setDeCompagnons.find { it.id == id }?: return
        if(lastLaunch != null) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - lastLaunch

            var pointsToReduce = (elapsedTime / intervalleFaim).toInt()
            pointsToReduce = if(pointsToReduce > 100) 100 else pointsToReduce
            modifierFaim(id, -pointsToReduce)
        }

        val faimRunnable = object : Runnable {
            override fun run() {
                setDeCompagnons.removeAll {true}
                dao.obtenirTous().forEach { setDeCompagnons.add(it) }
                modifierFaim(id, -1)
                handler.postDelayed(this, intervalleFaim)
            }
        }
        handler.postDelayed(faimRunnable, intervalleFaim)
    }
    fun baisserNivHumeur(id: Int, lastLaunch: Long?){
        setDeCompagnons.find { it.id == id } ?: return

        if(lastLaunch != null) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = (currentTime - lastLaunch)

            var pointsToReduce = (elapsedTime / intervalleBonheur).toInt()
            pointsToReduce = if(pointsToReduce > 100) 100 else pointsToReduce
            modifierHumeur(id, -pointsToReduce)
        }

        val bonheurRunnable = object : Runnable {
            override fun run() {
                setDeCompagnons.removeAll {true}
                dao.obtenirTous().forEach { setDeCompagnons.add(it) }
                modifierHumeur(id, -1)
                handler.postDelayed(this, intervalleBonheur)
            }
        }
        handler.postDelayed(bonheurRunnable, intervalleBonheur)
    }
}