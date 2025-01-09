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
import com.app.vakna.vue.DetailsRefugeActivity
import com.app.vakna.vue.MainActivity
import com.app.vakna.R
import com.bumptech.glide.Glide

class GridRefugesAdapter (
    private val context: Context,
    private val items: ArrayList<GridData>
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_magasin, parent, false)

        val item = items[position]

        val imageView = view.findViewById<ImageView>(R.id.itemImage)
        val nomTextView = view.findViewById<TextView>(R.id.itemNom)
        val niveauTextView = view.findViewById<TextView>(R.id.itemNiveau)
        val coutTextView = view.findViewById<TextView>(R.id.itemCout)
        val boutonAchat = view.findViewById<ImageButton>(R.id.boutonVueDetaille)

        Glide.with(context)
            .load(item.image)
            .into(imageView)
        nomTextView.text = item.nom
        coutTextView.text = item.cout.toString()

        niveauTextView.visibility = View.GONE

        val layoutParams = nomTextView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginEnd = 0
        layoutParams.marginStart = 0
        nomTextView.gravity = Gravity.CENTER

        view.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, DetailsRefugeActivity::class.java).apply {
                    putExtra("NOM_REFUGE", item.nom)
                }
                context.startActivity(intent)
            }
        }

        boutonAchat.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, DetailsRefugeActivity::class.java).apply {
                    putExtra("NOM_REFUGE", item.nom)
                }
                context.startActivity(intent)
            }
        }

        return view
    }
}