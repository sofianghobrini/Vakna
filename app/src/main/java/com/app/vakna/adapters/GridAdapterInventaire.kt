package com.app.vakna.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.app.vakna.R

class GridAdapterInventaire(
    private val context: Context,
    private val items: ArrayList<GridData>
) : GridAdapter(context, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_inventaire, parent, false)

        val item = items[position]

        // Bind data to the views
        val imageView = view.findViewById<ImageView>(R.id.itemImage)
        val nomTextView = view.findViewById<TextView>(R.id.itemNom)
        val niveauTextView = view.findViewById<TextView>(R.id.itemNiveau)
        val coutTextView = view.findViewById<TextView>(R.id.itemCout)
        val qteTextView = view.findViewById<TextView>(R.id.itemQuantite)

        imageView.setImageResource(item.image)
        nomTextView.text = item.nom
        niveauTextView.text = item.niveau.toString()
        coutTextView.text = item.cout.toString()
        qteTextView.text = "${item.qte}x"

        view.setOnClickListener {
            val popupView = LayoutInflater.from(context).inflate(R.layout.popup_objet, null)
            val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            val popupTextView: TextView = popupView.findViewById(R.id.popupTextView)
            popupTextView.text = item.nom

            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0) // Show it centered on the item

            // Optional: dismiss the popup when clicked
            popupView.setOnClickListener {
                popupWindow.dismiss()
            }
        }

        return view
    }

}