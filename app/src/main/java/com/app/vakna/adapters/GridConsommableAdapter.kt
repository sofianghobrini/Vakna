package com.app.vakna.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.app.vakna.DetailsObjetActivity
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.bumptech.glide.Glide

open class GridConsommableAdapter (
    private val context: Context,
    private val items: ArrayList<GridConsommableData>
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_magasin, parent, false)

        val item = items[position]

        // Bind data to the views
        val imageView = view.findViewById<ImageView>(R.id.itemImage)
        val nomTextView = view.findViewById<TextView>(R.id.itemNom)
        val niveauTextView = view.findViewById<TextView>(R.id.itemNiveau)
        val coutTextView = view.findViewById<TextView>(R.id.itemCout)
        val boutonAchat = view.findViewById<ImageButton>(R.id.boutonVueDetaille)

        Glide.with(context)
            .load(item.image)
            .into(imageView)
        nomTextView.text = item.nom
        niveauTextView.text = item.niveau.toString()
        coutTextView.text = item.cout.toString()

        boutonAchat.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, DetailsObjetActivity::class.java).apply {
                    putExtra("NOM_OBJET", item.nom)
                }
                context.startActivity(intent)
            }
        }

        return view
    }
}