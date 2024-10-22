package com.app.vakna.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.R
import com.google.android.material.switchmaterial.SwitchMaterial

// Adapter pour la liste des tâches
open class ListAdapter(
    val dataArrayList: ArrayList<ListData>
) : RecyclerView.Adapter<ListAdapter.TachesViewHolder>() {

    // ViewHolder contenant les éléments de la tâche
    class TachesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listTypeIcon: ImageView = itemView.findViewById(R.id.listTypeImage)
        val listName: TextView = itemView.findViewById(R.id.listName)
        val listType: TextView = itemView.findViewById(R.id.listType)
        val listImportance: TextView = itemView.findViewById(R.id.listImportance)
        val listTermine: SwitchMaterial? = itemView.findViewById(R.id.listSwitchTermine)
        val cardView: CardView? = itemView.findViewById(R.id.cardView)
        val boutonArchiverTache: ImageButton? = itemView.findViewById(R.id.boutonArchiverTache)
        val boutonModifierTache: ImageButton? = itemView.findViewById(R.id.boutonModifierTache)
    }

    // Création du ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TachesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.liste_gerer_taches, parent, false)
        return TachesViewHolder(view)
    }

    // Affichage des éléments de la tâche
    override fun onBindViewHolder(holder: TachesViewHolder, position: Int) {
        val listData = dataArrayList[position]
        // Affichage des icônes, noms et informations sur la tâche
        holder.listTypeIcon.setImageResource(listData.icon)
        holder.listName.text = listData.name
        holder.listType.text = listData.type
        holder.listImportance.text = listData.importance
    }

    // Retourne le nombre d'éléments dans la liste
    override fun getItemCount(): Int {
        return dataArrayList.size
    }
}
