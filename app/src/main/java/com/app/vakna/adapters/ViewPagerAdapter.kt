package com.app.vakna.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.R

class ViewPagerAdapter(
    private val context: Context,
    private val pages: List<List<GridData>>,
    private val adapterProvider: (Context, List<GridData>) -> GridAdapter
) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_objet, parent, false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val items = pages[position]
        val gridView = holder.gridView

        val adapter = adapterProvider(context, items)
        gridView.adapter = adapter
    }

    override fun getItemCount(): Int = pages.size

    inner class PagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gridView: GridView = view.findViewById(R.id.grid_view_items)
    }
}