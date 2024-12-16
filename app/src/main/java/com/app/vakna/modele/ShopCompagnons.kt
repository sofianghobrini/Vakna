package com.app.vakna.modele

import android.content.Context
import android.widget.Toast
import com.app.vakna.adapters.GridData
import com.app.vakna.modele.dao.CompagnonDAO
import com.app.vakna.modele.dao.CompagnonStoreDAO

class ShopCompagnons (
    private val context: Context
) {
    private val gestionnaireCompagnons = GestionnaireDeCompagnons(CompagnonDAO(context))
    private var compagnonMagasin = mutableListOf<CompagnonStore>()
    private val inventaire = Inventaire(context)
    private val compagnonStoreDAO = CompagnonStoreDAO(context)

    init {
        compagnonStoreDAO.obtenirTous().forEach { compagnonMagasin.add(it) }
    }

    fun getCompagnons(): List<CompagnonStore> {
        return compagnonMagasin
    }

    fun getCompagnon(nom: String): CompagnonStore? {
        return compagnonMagasin.find { it.nom == nom }
    }

    fun getCompagnonParEspece(espece: String): CompagnonStore? {
        return compagnonMagasin.find { it.espece == espece }
    }

    fun ajouterCompagnon(compagnon: CompagnonStore): Boolean {
        if (compagnon.nom.isBlank()) {
            throw IllegalArgumentException("Le nom du compagnon ne peut pas être vide")
        }
        if (!compagnonMagasin.add(compagnon)) {
            throw IllegalArgumentException("Une tâche avec le nom '${compagnon.nom}' existe déjà")
        }
        return compagnonStoreDAO.inserer(compagnon)
    }

    fun acheterCompagnon(compagnonId: Int, compagnonNom: String): Boolean {
        val compagnonStore = compagnonStoreDAO.obtenirParId(compagnonId)
            ?: return false

        if (inventaire.getPieces() < compagnonStore.prix) {
            return false
        }

        if (gestionnaireCompagnons.obtenirCompagnons().count() >= 6) {
            Toast.makeText(context, "Vous avez atteint la limite de compagnons", Toast.LENGTH_SHORT).show()
            return false
        }

        if (gestionnaireCompagnons.obtenirCompagnons().any { it.nom == compagnonStore.nom }) {
            return false
        }

        inventaire.ajouterPieces(-compagnonStore.prix)

        val nouveauCompagnon = Compagnon(
            id = (gestionnaireCompagnons.obtenirCompagnons().maxOfOrNull { it.id } ?: 0) + 1,
            nom = compagnonNom,
            faim = 50,
            humeur = 50,
            xp = 0,
            espece = compagnonStore.espece,
            actif = false
        )

        gestionnaireCompagnons.ajouterCompagnon(nouveauCompagnon)

        return true
    }
    companion object {
        fun setToGridDataArray(compagnons: List<CompagnonStore>): ArrayList<GridData> {
            val list = ArrayList<GridData>()
            for (compagnon in compagnons) {
                val listData = compagnon.toGridData()
                list.add(listData)
            }
            return list
        }
    }
}