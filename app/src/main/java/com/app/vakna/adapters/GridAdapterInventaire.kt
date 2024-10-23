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
import com.app.vakna.modele.TypeObjet

class GridAdapterInventaire(
    private val context: Context,
    private val items: ArrayList<GridData>
) : GridAdapter(context, items) {

    private val inventaire = Inventaire(context)

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

            // Show the popup at the calculated location
            popupUtilisationWindow.showAtLocation(view, Gravity.NO_GRAVITY, offsetX, offsetY)

            val buttonChoisir: Button = popupUtilisationView.findViewById(R.id.boutonChoisirNombre)
            buttonChoisir.setOnClickListener {
                val qteUtilisations = nombreUtilisaitons.value
                showUtiliserPopUp(item, qteUtilisations, view)

                popupUtilisationWindow.dismiss()
            }

            // Dismiss the popup when clicked
            popupUtilisationView.setOnClickListener {
                popupUtilisationWindow.dismiss()
            }
        }

        return view
    }

    private fun showUtiliserPopUp(
        item: GridData,
        util: Int,
        view: View
    ) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_objet, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Set the title and information in the popup
        val popupTextView: TextView = popupView.findViewById(R.id.popupTitre)
        popupTextView.text = item.nom

        val action = when (inventaire.getObjetParNom(item.nom)?.getType()) {
            TypeObjet.JOUET -> "l'humeur"
            TypeObjet.NOURRITURE -> "la faim"
            null -> " "
        }

        val qteApresUtilisation = if (item.qte!! <= 1) "plus" else "que " + (item.qte.minus(util))
        val niveauTotal = item.niveau * util
        val popupTextInfo: TextView = popupView.findViewById(R.id.popupInfo)
        popupTextInfo.text = "Voulez vous utiliser $util ${item.nom}?" +
                "\nCela remontera $action de $niveauTotal." +
                "\nIl ne vous en restera $qteApresUtilisation."

        // Measure the popup width
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupWidth = popupView.measuredWidth

        // Get location of the clicked view
        val location = IntArray(2)
        view.getLocationOnScreen(location)

        // Calculate offsets for centering the popup above the clicked item
        val offsetX = location[0] + (view.width / 2) - (popupWidth / 2)
        val offsetY = location[1] - popupView.measuredHeight // Position above the item

        // Set properties for the popup window
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        // Show the popup at the calculated location
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, offsetX, offsetY)

        val buttonUtiliser: Button = popupView.findViewById(R.id.boutonUtiliser)
        buttonUtiliser.setOnClickListener {

            inventaire.utiliserObjet(item.nom, util)

            popupWindow.dismiss()
        }

        // Dismiss the popup when clicked
        popupView.setOnClickListener {
            popupWindow.dismiss()
        }
    }

}