package com.app.vakna.adapters

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.app.vakna.vue.DetailsCompagnonActivity
import com.app.vakna.vue.MainActivity
import com.app.vakna.R
import com.bumptech.glide.Glide

open class GridCompagnonsAdapter(
    private val context: Context,
    private val items: ArrayList<GridData>
) : GridAdapter(context, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        super.getView(position, convertView, parent)
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_magasin, parent, false)

        val item = items[position]

        val nomTextView = view.findViewById<TextView>(R.id.itemNom)
        val coutTextView = view.findViewById<TextView>(R.id.itemCout)
        val imageView = view.findViewById<ImageView>(R.id.itemImage)
        val boutonAchat = view.findViewById<ImageButton>(R.id.boutonVueDetaille)
        val niveauTextView = view.findViewById<TextView>(R.id.itemNiveau)

        nomTextView.text = item.nom
        coutTextView.text = item.cout.toString()

        Glide.with(context)
            .asGif()
            .load(item.image)
            .into(imageView)

        val layoutParams = nomTextView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginEnd = 0
        layoutParams.marginStart = 0
        nomTextView.gravity = Gravity.CENTER
        niveauTextView.visibility = View.GONE

        view.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, DetailsCompagnonActivity::class.java).apply {
                    putExtra("ESPECE_COMPAGNON", item.nom)
                }
                context.startActivity(intent)
            }
        }

        boutonAchat.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, DetailsCompagnonActivity::class.java).apply {
                    putExtra("ESPECE_COMPAGNON", item.nom)
                }
                context.startActivity(intent)
            }
        }

        return view
    }
}