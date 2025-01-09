package com.app.vakna.modele.gestionnaires

import android.content.Context
import com.app.vakna.adapters.GridData
import com.app.vakna.modele.dao.refuge.Refuge
import com.app.vakna.modele.dao.refugestore.RefugeStore
import com.app.vakna.modele.dao.refugestore.RefugeStoreDAO

class ShopRefuge(context: Context) {

    private var refugeStoreDAO = RefugeStoreDAO(context)
    private var refugesStore = mutableSetOf<RefugeStore>()
    private val inventaire = Inventaire(context)
    private val gestionnaireRefuge = GestionnaireDeRefuge(context)

    init {
        refugeStoreDAO.obtenirTous().forEach { refugesStore.add(it) }
    }

    fun obtenirRefugesStore(): Set<RefugeStore> {
        refugeStoreDAO.obtenirTous().forEach { refugesStore.add(it) }
        return refugesStore
    }

    fun obtenirRefugeStore(nom: String): RefugeStore? {
        return refugesStore.find { it.getNom() == nom }
    }

    fun ajouterRefugeStore(refugeStore: RefugeStore): Boolean {
        if (refugeStore.getNom().isBlank()) {
            throw IllegalArgumentException("Le nom du refuge ne peut pas être vide")
        }
        if (!refugesStore.add(refugeStore)) {
            throw IllegalArgumentException("Un Refuge avec le nom '${refugeStore.getNom()}' existe déjà")
        }
        return refugeStoreDAO.inserer(refugeStore)
    }

    fun acheterRefuge(refugeId: Int): Boolean {
        val refugeStore = refugeStoreDAO.obtenirParId(refugeId) ?: return false

        if (inventaire.obtenirPieces() < refugeStore.getPrix()) {
            return false
        }

        inventaire.ajouterPieces(-refugeStore.getPrix())

        val nouveauRefuge = Refuge(
            id = (gestionnaireRefuge.obtenirRefuges().maxOfOrNull { it.getId() } ?: 0) + 1,
            nom = refugeStore.getNom(),
            modifFaim = refugeStore.getModifFaim(),
            modifHumeur = refugeStore.getModifHumeur(),
            modifXp = refugeStore.getModifXp(),
            modifPieces = refugeStore.getModifPieces()
        )

        gestionnaireRefuge.ajouterRefuge(nouveauRefuge)

        refugesStore.remove(refugeStore)
        refugeStoreDAO.supprimer(refugeId)

        return true
    }

    companion object {
        fun setToGridDataArray(refugesStore: List<RefugeStore>): ArrayList<GridData> {
            val list = ArrayList<GridData>()
            for (refuge in refugesStore) {
                val listData = refuge.toGridData()
                list.add(listData)
            }
            return list
        }
    }
}