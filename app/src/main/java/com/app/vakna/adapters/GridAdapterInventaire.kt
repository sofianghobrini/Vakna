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
        val qteTextView = view.findViewById<TextView>(R.id.itemQuantite)

        imageView.setImageResource(item.image)
        nomTextView.text = item.nom
        niveauTextView.text = item.niveau.toString()
        qteTextView.text = "${item.qte}x"

        view.setOnClickListener {
            val popupView = LayoutInflater.from(context).inflate(R.layout.popup_objet, null)
            val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            // Set the content of the popup
            val popupTextView: TextView = popupView.findViewById(R.id.popupTextView)
            popupTextView.text = item.nom

            // Get the location of the clicked view on the screen
            val location = IntArray(2)
            view.getLocationOnScreen(location)

            // Adjust popup position to appear just above the clicked item
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true

            // Show the popup window above the item
            val offsetY = -view.height  // Moves the popup up by the height of the clicked view
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + offsetY)

            // Optional: dismiss the popup when clicked
            popupView.setOnClickListener {
                popupWindow.dismiss()
            }
        }

        return view
    }

}