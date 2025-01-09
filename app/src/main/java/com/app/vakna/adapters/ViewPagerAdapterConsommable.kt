package com.app.vakna.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.R

class ViewPagerAdapterConsommable(
    private val context: Context,
    private val pages: List<List<GridConsommableData>> // List of items for each page
) : RecyclerView.Adapter<ViewPagerAdapterConsommable.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_objet, parent, false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val items = pages[position] // Items for this page
        val gridView = holder.gridView

        // Use your custom GridView adapter
        val adapter = GridConsommableAdapter(context, ArrayList(items))
        gridView.adapter = adapter
    }

    override fun getItemCount(): Int = pages.size

    inner class PagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gridView: GridView = view.findViewById(R.id.grid_view_items)
    }
}