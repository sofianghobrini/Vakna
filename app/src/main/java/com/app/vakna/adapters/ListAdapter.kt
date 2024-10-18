package com.app.vakna.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.R
import com.google.android.material.switchmaterial.SwitchMaterial

open class ListAdapter(
    val dataArrayList: ArrayList<ListData>
) : RecyclerView.Adapter<ListAdapter.TachesViewHolder>() {

    class TachesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listTypeIcon: ImageView = itemView.findViewById(R.id.listTypeImage)
        val listName: TextView = itemView.findViewById(R.id.listName)
        val listType: TextView = itemView.findViewById(R.id.listType)
        val listImportance: TextView = itemView.findViewById(R.id.listImportance)
        val listTermine: SwitchMaterial? = itemView.findViewById(R.id.listSwitchTermine)
        val boutonArchiverTache: ImageButton? = itemView.findViewById<ImageButton>(R.id.boutonArchiverTache)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TachesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.liste_gerer_taches, parent, false)
        return TachesViewHolder(view)
    }

    override fun onBindViewHolder(holder: TachesViewHolder, position: Int) {
        val listData = dataArrayList[position]
        holder.listTypeIcon.setImageResource(listData.icon)
        holder.listName.text = listData.name
        holder.listType.text = listData.type
        holder.listImportance.text = listData.importance
    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }
}