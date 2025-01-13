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
import com.app.vakna.modele.dao.TypeObjet
import com.bumptech.glide.Glide

open class GridConsommableAdapter (
    private val context: Context,
    private val items: ArrayList<GridConsommableData>
) : GridConsommableDefautAdapter(context, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        super.getView(position, convertView, parent)
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_magasin, parent, false)

        val item = items[position]


        val nomTextView = view.findViewById<TextView>(R.id.itemNom)
        val niveauTextView = view.findViewById<TextView>(R.id.itemNiveau)
        val imageView = view.findViewById<ImageView>(R.id.itemImage)
        val coutTextView = view.findViewById<TextView>(R.id.itemCout)
        val effet = view.findViewById<ImageView>(R.id.itemEffet)
        val boutonAchat = view.findViewById<ImageButton>(R.id.boutonVueDetaille)

        nomTextView.text = item.nom
        niveauTextView.text = item.niveau.toString()

        when(item.type) {
            TypeObjet.JOUET -> effet.setImageResource(R.drawable.humeur_0)
            TypeObjet.NOURRITURE -> effet.setImageResource(R.drawable.faim_0)
        }

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