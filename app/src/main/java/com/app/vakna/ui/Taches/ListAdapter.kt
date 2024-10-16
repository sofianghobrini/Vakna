package com.app.vakna.ui.Taches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.R
import com.google.android.material.switchmaterial.SwitchMaterial

// Define your adapter class that extends RecyclerView.Adapter
class ListAdapter(
    private val dataArrayList: ArrayList<ListData>,
    private val progressBar: ProgressBar
) : RecyclerView.Adapter<ListAdapter.TachesViewHolder>() {

    private var completedTasks = 0
    // Define the ViewHolder class that holds the views for each item
    class TachesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listTypeIcon: ImageView = itemView.findViewById(R.id.listTypeImage)
        val listName: TextView = itemView.findViewById(R.id.listName)
        val listType: TextView = itemView.findViewById(R.id.listType)
        val listImportance: TextView = itemView.findViewById(R.id.listImportance)
        val listTermine: SwitchMaterial = itemView.findViewById(R.id.listSwitchTermine)
    }

    // This method inflates the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TachesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.liste_taches, parent, false)
        return TachesViewHolder(view)
    }

    // This method binds the data to each view in the ViewHolder
    override fun onBindViewHolder(holder: TachesViewHolder, position: Int) {
        val listData = dataArrayList[position]

        // Set the views based on the data
        holder.listTypeIcon.setImageResource(listData.icon)
        holder.listName.text = listData.name
        holder.listType.text = listData.type
        holder.listImportance.text = if (listData.importance) "Importante" else "Facultative"
        holder.listTermine.isChecked = listData.estTermine

        if (listData.estTermine) {
            completedTasks++
        }

        holder.listTermine.setOnCheckedChangeListener { _, isChecked ->
            listData.estTermine = isChecked
            if (isChecked) {
                completedTasks++
            } else {
                completedTasks--
            }
            updateProgressBar()
        }
    }

    fun updateProgressBar() {
        val progressPercentage = (completedTasks.toDouble() / dataArrayList.size) * 100
        progressBar.progress = progressPercentage.toInt()
    }

    // This method returns the total number of items in the data set
    override fun getItemCount(): Int {
        return dataArrayList.size
    }
}