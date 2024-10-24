package com.app.vakna.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.PopupWindow
import android.widget.TextView
import com.app.vakna.R
import com.app.vakna.modele.Inventaire

class GridAdapterInventaire(
    private val context: Context,
    private val items: ArrayList<GridData>
) : GridAdapter(context, items) {

    private val inventaire = Inventaire(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_inventaire, parent, false)

        val item = items[position]

        val imageView = view.findViewById<ImageView>(R.id.itemImage)
        val nomTextView = view.findViewById<TextView>(R.id.itemNom)
        val niveauTextView = view.findViewById<TextView>(R.id.itemNiveau)
        val qteTextView = view.findViewById<TextView>(R.id.itemQuantite)

        imageView.setImageResource(item.image)
        nomTextView.text = item.nom
        niveauTextView.text = item.niveau.toString()
        qteTextView.text = "${item.qte}x"

        view.setOnClickListener {
            val popupUtilisationView = LayoutInflater.from(context).inflate(R.layout.popup_nombre_utilisations, null)
            val popupUtilisationWindow = PopupWindow(popupUtilisationView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            val popupTextUtilisationView: TextView = popupUtilisationView.findViewById(R.id.popupUtilisationTitre)
            popupTextUtilisationView.text = item.nom
            val popupTextQuestion: TextView = popupUtilisationView.findViewById(R.id.popupQuestion)
            popupTextQuestion.text = "Combien voulez-vous utiliser de ${item.nom}."

            val nombreUtilisaitons = popupUtilisationView.findViewById<NumberPicker>(R.id.nombreUtilisations)
            nombreUtilisaitons.minValue = 1
            nombreUtilisaitons.maxValue = item.qte!!

            popupUtilisationView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val popupWidth = popupUtilisationView.measuredWidth

            val location = IntArray(2)
            view.getLocationOnScreen(location)

            val offsetX = location[0] + (view.width / 2) - (popupWidth / 2)
            val offsetY = location[1] - popupUtilisationView.measuredHeight

            popupUtilisationWindow.isOutsideTouchable = true
            popupUtilisationWindow.isFocusable = true

            popupUtilisationWindow.showAtLocation(view, Gravity.NO_GRAVITY, offsetX, offsetY)

            val buttonUtiliser: Button = popupUtilisationView.findViewById(R.id.boutonUtiliser)
            buttonUtiliser.setOnClickListener {
                val qteUtilisations = nombreUtilisaitons.value
                inventaire.utiliserObjet(item.nom, qteUtilisations)

                val type = inventaire.getObjetParNom(item.nom)?.getType()
                val updatedItems = type?.let { it1 -> inventaire.getObjetsParType(it1) }
                items.clear()
                updatedItems?.let { it1 -> Inventaire.setToGridDataArray(it1) }
                    ?.let { it2 -> items.addAll(it2) }
                notifyDataSetChanged()

                popupUtilisationWindow.dismiss()
            }

            popupUtilisationView.setOnClickListener {
                popupUtilisationWindow.dismiss()
            }
        }

        return view
    }
}