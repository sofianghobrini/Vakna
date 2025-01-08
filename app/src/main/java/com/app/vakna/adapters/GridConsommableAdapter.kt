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
import com.app.vakna.vue.DetailsObjetActivity
import com.app.vakna.vue.MainActivity
import com.app.vakna.R
import com.bumptech.glide.Glide

open class GridConsommableAdapter (
    private val context: Context,
    private val items: ArrayList<GridConsommableData>
) : GridConsommableDefautAdapter(context, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        super.getView(position, convertView, parent)
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_magasin, parent, false)

        val item = items[position]

        val imageView = view.findViewById<ImageView>(R.id.itemImage)
        val coutTextView = view.findViewById<TextView>(R.id.itemCout)
        val boutonAchat = view.findViewById<ImageButton>(R.id.boutonVueDetaille)

        Glide.with(context)
            .load(item.image)
            .into(imageView)
        coutTextView.text = item.cout.toString()

        view.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, DetailsObjetActivity::class.java).apply {
                    putExtra("NOM_OBJET", item.nom)
                }
                context.startActivity(intent)
            }
        }

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