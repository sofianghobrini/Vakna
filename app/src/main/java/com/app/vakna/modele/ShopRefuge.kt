package com.app.vakna.modele

import android.content.Context
import android.widget.Toast
import com.app.vakna.adapters.GridData
import com.app.vakna.adapters.GridRefugeData
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.RefugeDAO
import com.app.vakna.modele.dao.RefugeStoreDAO

class ShopRefuge(context: Context) {
    private var refugeStoreDAO = RefugeStoreDAO(context)
    private var refugesStore = mutableSetOf<RefugeStore>()
    private val inventaire = Inventaire(context)
    private val gestionnaireRefuge = GestionnaireDeRefuge(context)


    init {
        refugeStoreDAO.obtenirTous().forEach { refugesStore.add(it) }
    }

    fun getRefugesStore(): Set<RefugeStore> {
        refugeStoreDAO.obtenirTous().forEach { refugesStore.add(it) }
        return refugesStore
    }

    fun getRefugeStoreParNom(nom: String): RefugeStore? {
        return refugesStore.find { it.getNom() == nom }
    }

    fun getRefugeStoreParId(id: Int): RefugeStore? {
        return refugesStore.find { it.getId() == id }
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

        if (inventaire.getPieces() < refugeStore.getPrix()) {
            return false
        }

        inventaire.ajouterPieces(-refugeStore.getPrix())

        val nouveauRefuge = Refuge(
            id = (gestionnaireRefuge.getRefuges().maxOfOrNull { it.getId() } ?: 0) + 1,
            nom = refugeStore.getNom(),
            modifFaim = refugeStore.getModifFaim(),
            modifHumeur = refugeStore.getModifHumeur(),
            modifXp = refugeStore.getModifXp(),
            modifPieces = refugeStore.getModifPieces()
        )

        gestionnaireRefuge.ajouterRefuge(nouveauRefuge)

        return true
    }

    companion object {
        fun setToGridRefugeDataArray(refugesStore: List<RefugeStore>): ArrayList<GridRefugeData> {
            val list = ArrayList<GridRefugeData>()
            for (refuge in refugesStore) {
                val listData = refuge.toGridRefugeData()
                list.add(listData)
            }
            return list
        }
    }
}