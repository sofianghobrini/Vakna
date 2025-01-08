package com.app.vakna.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.app.vakna.R
import com.bumptech.glide.Glide

open class GridAdapter(
    private val context: Context,
    private val items: ArrayList<GridData>
): BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_magasin, parent, false)

        val item = items[position]

        val nomTextView = view.findViewById<TextView>(R.id.itemNom)
        val niveauTextView = view.findViewById<TextView>(R.id.itemNiveau)
        val coutTextView = view.findViewById<TextView>(R.id.itemCout)

        nomTextView.text = item.nom
        coutTextView.text = item.cout.toString()
        niveauTextView.visibility = View.GONE

        val layoutParams = nomTextView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginEnd = 0
        layoutParams.marginStart = 0
        nomTextView.gravity = Gravity.CENTER

        return view
    }
}