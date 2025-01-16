package com.app.vakna.modele.gestionnaires

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.app.vakna.modele.dao.compagnon.CompagnonDAO
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.app.vakna.modele.dao.Personnalite
import com.app.vakna.modele.dao.compagnon.Compagnon
import com.app.vakna.notifications.NotificationReceiver

class GestionnaireDeCompagnons(private val context: Context) {

    private val intervalleFaim = 30 * 30L
    private val intervalleBonheur =  4 * 3600000L

    private val daoCompagnons = CompagnonDAO(context)
    private val setDeCompagnons = mutableSetOf<Compagnon>()
    private val handler = Handler(Looper.getMainLooper())

    init {
        daoCompagnons.obtenirTous().forEach { setDeCompagnons.add(it) }
    }

    fun obtenirCompagnons(): Set<Compagnon> {
        daoCompagnons.obtenirTous().forEach { setDeCompagnons.add(it) }
        return setDeCompagnons
    }

    fun obtenirCompagnon(id: Int): Compagnon? {
        return daoCompagnons.obtenirTous().find { it.id == id }
    }

    fun ajouterCompagnon(compagnon: Compagnon): Boolean {
        if (compagnon.nom.isBlank()) {
            throw IllegalArgumentException("Le nom du compagnon ne peut pas être vide")
        }
        if (!setDeCompagnons.add(compagnon)) {
            throw IllegalArgumentException("Une tâche avec le nom '${compagnon.nom}' existe déjà")
        }
        return daoCompagnons.inserer(compagnon)
    }

    fun modifierCompagnon(id: Int, nouveauCompagnon: Compagnon): Boolean {
        val compagnon = setDeCompagnons.find { it.id == id }
        if (compagnon != null) {
            compagnon.nom = nouveauCompagnon.nom
            compagnon.faim = nouveauCompagnon.faim
            compagnon.humeur = nouveauCompagnon.humeur
            compagnon.xp = nouveauCompagnon.xp
            compagnon.espece = nouveauCompagnon.espece
            compagnon.personnalite = nouveauCompagnon.personnalite

            return daoCompagnons.modifier(id, nouveauCompagnon)
        } else {
            throw IllegalArgumentException("Compagnon avec l'id $id introuvable")
        }
    }

    fun obtenirActif(): Compagnon? {
        return daoCompagnons.obtenirTous().find { it.actif == true }
    }

    fun setActif(id: Int) {
        obtenirActif()?.let {
            it.actif = false
            daoCompagnons.modifier(it.id, it)
        }
        obtenirCompagnon(id)?.let {
            it.actif = true
            daoCompagnons.modifier(id, it)
        }
    }

    fun relacherCompagnon(compagnon: Compagnon, id : Int): Int{
        val compagnonRecherche = setDeCompagnons.find { it.id == id }
        if(!setDeCompagnons.contains(compagnon)){
            throw IllegalArgumentException("Le compagnon n'existe pas")
        }
        val pieces = compagnon.niveau()*100
        setDeCompagnons.remove(compagnonRecherche)
        daoCompagnons.supprimer(compagnon.id)
        return pieces
    }

    fun modifierFaim(id: Int, niveau: Int) {
        // Vérification que le niveau est compris entre -100 et 100
        assert(niveau in -100..100) { "Le niveau de faim doit être compris entre -100 et 100."}
        val compagnon = setDeCompagnons.find { it.id == id }?: return

        compagnon.faim += niveau
        compagnon.faim = compagnon.faim.coerceIn(0, 100)
        daoCompagnons.modifier(id, compagnon)
    }

    // Méthode pour modifier le niveau d'humeur du compagnon
    fun modifierHumeur(id: Int, niveau: Int) {
        // Vérification que le niveau est compris entre -100 et 100
        assert(niveau in -100..100) { "Le niveau d'humeur doit être compris entre -100 et 100."}
        val compagnon = setDeCompagnons.find { it.id == id }?: return

        compagnon.humeur += niveau
        compagnon.humeur = compagnon.humeur.coerceIn(0, 100)
        daoCompagnons.modifier(id, compagnon)
    }

    // Méthode pour ajouter de l'expérience (XP) au compagnon
    fun gagnerXp(id: Int, montant: Int) {
        val compagnon = setDeCompagnons.find { it.id == id }?: return
        val facteurXp = compagnon.personnalite.facteurXp
        compagnon.xp += (montant * facteurXp).toInt()
        daoCompagnons.modifier(id, compagnon)
    }

    fun baisserNivFaim(id: Int, lastLaunch: Long?) {
        val compagnon = setDeCompagnons.find { it.id == id } ?: return
        val facteurFaim = compagnon.personnalite.facteurFaim // Récupérer le facteur faim
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
                daoCompagnons.obtenirTous().forEach { setDeCompagnons.add(it) }
                modifierFaim(id, (-1 * facteurFaim).toInt())
                handler.postDelayed(this, intervalleFaim)
            }
        }
        handler.postDelayed(faimRunnable, intervalleFaim)
    }

    fun baisserNivHumeur(id: Int, lastLaunch: Long?){
        val compagnon = setDeCompagnons.find { it.id == id } ?: return
        val facteurHumeur = compagnon.personnalite.facteurHumeur // Récupérer le facteur humeur

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
                daoCompagnons.obtenirTous().forEach { setDeCompagnons.add(it) }
                modifierHumeur(id, (-1 * facteurHumeur).toInt())
                handler.postDelayed(this, intervalleBonheur)
            }
        }
        handler.postDelayed(bonheurRunnable, intervalleBonheur)
    }

    fun notifierSiFaimOuHumeurAtteintZero(compagnon: Compagnon) {
        val faimTempsRestant = tempsPourAtteindreZero(compagnon.faim, compagnon.personnalite.facteurFaim.toLong(), intervalleFaim)
        val humeurTempsRestant = tempsPourAtteindreZero(compagnon.humeur, compagnon.personnalite.facteurHumeur.toLong(), intervalleBonheur)

        if (faimTempsRestant > 0) {
            planifierNotification(context, faimTempsRestant, "Votre compagnon a faim !")
        }

        if (humeurTempsRestant > 0) {
            planifierNotification(context, humeurTempsRestant, "Votre compagnon est malheureux !")
        }
    }

    private fun planifierNotification(context: Context, delayInMillis: Long, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("notificationMessage", message)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, message.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val triggerAtMillis = SystemClock.elapsedRealtime() + delayInMillis
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent)
    }


    private fun tempsPourAtteindreZero(currentValue: Int, facteur: Long, intervalle: Long): Long {
        if (facteur <= 0 || currentValue <= 0) return 0
        val reductionsNeeded = currentValue / (1 * facteur).toInt()
        return reductionsNeeded * intervalle
    }
}