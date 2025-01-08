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

    private val flammeNonActif: Float = 0.3f
    private val flammeActif: Float = 1.0f
    private val grayScaleFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
    private val normalColorFilter = ColorMatrixColorFilter(ColorMatrix())

    // ViewHolder contenant les éléments de la tâche
    class TachesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listTypeIcon: ImageView = itemView.findViewById(R.id.listTypeImage)
        val listName: TextView = itemView.findViewById(R.id.listName)
        val listType: TextView = itemView.findViewById(R.id.listType)
        val flammeVert: ImageView = itemView.findViewById(R.id.flame_green)
        val flammeOrange: ImageView = itemView.findViewById(R.id.flame_orange)
        val flammeRouge: ImageView = itemView.findViewById(R.id.flame_red)
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

        holder.listTypeIcon.setImageResource(listData.icon)
        holder.listName.text = listData.name
        holder.listType.text = listData.type

        afficherFlammes(holder, listData)
    }

    private fun afficherFlammes(
        holder: TachesViewHolder,
        listData: ListData
    ) {
        holder.flammeVert.colorFilter = grayScaleFilter
        holder.flammeOrange.colorFilter = grayScaleFilter
        holder.flammeRouge.colorFilter = grayScaleFilter
        when (listData.importance) {
            "ELEVEE" -> {
                setFlammeActive(holder, flammeNonActif, flammeNonActif, flammeActif)
            }

            "MOYENNE" -> {
                setFlammeActive(holder, flammeNonActif, flammeActif, flammeNonActif)
            }

            "FAIBLE" -> {
                setFlammeActive(holder, flammeActif, flammeNonActif, flammeNonActif)
            }
        }
    }

    private fun setFlammeActive(holder: TachesViewHolder, alphaVert: Float, alphaOrange: Float, alphaRouge: Float) {
        holder.flammeVert.alpha = alphaVert
        holder.flammeOrange.alpha = alphaOrange
        holder.flammeRouge.alpha = alphaRouge
        when {
            alphaVert == flammeActif -> holder.flammeVert.colorFilter = normalColorFilter
            alphaOrange == flammeActif -> holder.flammeOrange.colorFilter = normalColorFilter
            alphaRouge == flammeActif -> holder.flammeRouge.colorFilter = normalColorFilter
        }
    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }
}
