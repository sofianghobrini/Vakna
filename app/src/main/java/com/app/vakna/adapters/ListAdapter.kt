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
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter

// Adapter pour la liste des tâches
open class ListAdapter(
    val dataArrayList: ArrayList<ListData>
) : RecyclerView.Adapter<ListAdapter.TachesViewHolder>() {

    // ViewHolder contenant les éléments de la tâche
    class TachesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listTypeIcon: ImageView = itemView.findViewById(R.id.listTypeImage)
        val listName: TextView = itemView.findViewById(R.id.listName)
        val listType: TextView = itemView.findViewById(R.id.listType)
        val flameGreen: ImageView = itemView.findViewById(R.id.flame_green)
        val flameOrange: ImageView = itemView.findViewById(R.id.flame_orange)
        val flameRed: ImageView = itemView.findViewById(R.id.flame_red)
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

        // Création du filtre noir et blanc
        val grayScaleMatrix = ColorMatrix().apply { setSaturation(0f) }
        val grayScaleFilter = ColorMatrixColorFilter(grayScaleMatrix)

        when (listData.importance) {
            "ELEVEE" -> {
                holder.flameGreen.alpha = 0.3f
                holder.flameGreen.colorFilter = grayScaleFilter
                holder.flameOrange.alpha = 0.3f
                holder.flameOrange.colorFilter = grayScaleFilter
                holder.flameRed.alpha = 1.0f  // Affiche la flamme rouge
            }
            "MOYENNE" -> {
                holder.flameGreen.alpha = 0.3f
                holder.flameGreen.colorFilter = grayScaleFilter
                holder.flameOrange.alpha = 1.0f  // Affiche la flamme orange
                holder.flameRed.alpha = 0.2f
                holder.flameRed.colorFilter = grayScaleFilter
            }
            "FAIBLE" -> {
                holder.flameGreen.alpha = 1.0f  // Affiche la flamme verte
                holder.flameOrange.alpha = 0.3f
                holder.flameOrange.colorFilter = grayScaleFilter
                holder.flameRed.alpha = 0.2f
                holder.flameRed.colorFilter = grayScaleFilter
            }
            else -> {
                // Si `importance` n'est pas défini, toutes les flammes sont en transparence et noir et blanc
                holder.flameGreen.alpha = 0.3f
                holder.flameGreen.colorFilter = grayScaleFilter
                holder.flameOrange.alpha = 0.3f
                holder.flameOrange.colorFilter = grayScaleFilter
                holder.flameRed.alpha = 0.3f
                holder.flameRed.colorFilter = grayScaleFilter
            }
        }
    }



    // Retourne le nombre d'éléments dans la liste
    override fun getItemCount(): Int {
        return dataArrayList.size
    }
}
