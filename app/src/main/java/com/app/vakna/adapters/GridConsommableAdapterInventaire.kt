package com.app.vakna.adapters

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.PopupWindow
import android.widget.TextView
import com.app.vakna.DetailsObjetActivity
import com.app.vakna.MainActivity
import com.app.vakna.R
import com.app.vakna.controller.ControllerCompagnon
import com.app.vakna.databinding.FragmentCompagnonBinding
import com.app.vakna.modele.GestionnaireDeCompagnons
import com.app.vakna.modele.Inventaire
import com.app.vakna.modele.dao.CompagnonDAO
import com.bumptech.glide.Glide

class GridConsommableAdapterInventaire(
    private val binding: FragmentCompagnonBinding,
    private val items: ArrayList<GridConsommableData>
) : GridConsommableAdapter(binding.root.context, items) {

    private val context = binding.root.context
    private val inventaire = Inventaire(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_inventaire, parent, false)

        val item = items[position]

        val imageView = view.findViewById<ImageView>(R.id.itemImage)
        val nomTextView = view.findViewById<TextView>(R.id.itemNom)
        val niveauTextView = view.findViewById<TextView>(R.id.itemNiveau)
        val qteTextView = view.findViewById<TextView>(R.id.itemQuantite)

        Glide.with(context)
            .load(item.image)
            .into(imageView)
        nomTextView.text = item.nom
        niveauTextView.text = item.niveau.toString()
        qteTextView.text = "${item.qte}x"

        view.setOnClickListener {
            if(item.qte!! <= 0) {
                showAcheterPlusPopUp(item, view)
            } else {
                showUtilisationPopUp(item, view)
            }
        }

        return view
    }

    private fun showAcheterPlusPopUp(item: GridConsommableData, view: View) {
        val popupMagasinView =
            LayoutInflater.from(context).inflate(R.layout.popup_acheter_plus, null)
        val popupMagasinWindow = PopupWindow(
            popupMagasinView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val popupTextMagasinView: TextView =
            popupMagasinView.findViewById(R.id.popupMagasinTitre)
        popupTextMagasinView.text = item.nom
        val popupTextQuestion: TextView = popupMagasinView.findViewById(R.id.popupMagasinQuestion)
        popupTextQuestion.text = context.getString(R.string.popup_titre_quantite_vide, item.nom)

        popupMagasinView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupWidth = popupMagasinView.measuredWidth

        val location = IntArray(2)
        view.getLocationOnScreen(location)

        val offsetX = location[0] + (view.width / 2) - (popupWidth / 2)
        val offsetY = location[1] - popupMagasinView.measuredHeight

        popupMagasinWindow.isOutsideTouchable = true
        popupMagasinWindow.isFocusable = true

        popupMagasinWindow.showAtLocation(view, Gravity.NO_GRAVITY, offsetX, offsetY)

        val boutonMagasin: Button = popupMagasinView.findViewById(R.id.boutonMagasin)
        boutonMagasin.text = context.getString(R.string.bouton_acheter)
        boutonMagasin.setOnClickListener {
            if (context is MainActivity) {
                val intent = Intent(context, DetailsObjetActivity::class.java).apply {
                    putExtra("sourceFragment", "CompagnonFragment")
                    putExtra("NOM_OBJET", item.nom)
                }
                context.startActivity(intent)
            }
            popupMagasinWindow.dismiss()
        }

        popupMagasinView.setOnClickListener {
            popupMagasinWindow.dismiss()
        }
    }

    private fun showUtilisationPopUp(item: GridConsommableData, view: View) {
        val popupUtilisationView =
            LayoutInflater.from(context).inflate(R.layout.popup_nombre_utilisations, null)
        val popupUtilisationWindow = PopupWindow(
            popupUtilisationView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val popupTextUtilisationView: TextView =
            popupUtilisationView.findViewById(R.id.popupUtilisationTitre)
        popupTextUtilisationView.text = item.nom
        val popupTextQuestion: TextView = popupUtilisationView.findViewById(R.id.popupQuestion)
        popupTextQuestion.text = context.getString(R.string.popup_quantite_utiliser, item.nom)

        val nombreUtilisations =
            popupUtilisationView.findViewById<NumberPicker>(R.id.nombreUtilisations)
        nombreUtilisations.minValue = 1
        nombreUtilisations.maxValue = item.qte!!

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
        buttonUtiliser.text = context.getString(R.string.bouton_utiliser)
        buttonUtiliser.setOnClickListener {
            val qteUtilisations = nombreUtilisations.value
            inventaire.utiliserObjet(item.nom, qteUtilisations)

            val type = inventaire.getObjetParNom(item.nom)?.getType()
            val updatedItems = type?.let { it1 -> inventaire.getObjetsParType(it1) }
            items.clear()

            ControllerCompagnon.setupGridView(updatedItems!!, type, binding)
            val gestionnaire = GestionnaireDeCompagnons(context)
            val compagnons = gestionnaire.obtenirCompagnons()
            var compagnon = gestionnaire.obtenirActif()
            if (compagnon == null) {
                compagnon = compagnons.first()
                gestionnaire.setActif(compagnon.id)
            }
            ControllerCompagnon.updateHumeurCompagnon(binding, compagnon)
            popupUtilisationWindow.dismiss()
        }

        popupUtilisationView.setOnClickListener {
            popupUtilisationWindow.dismiss()
        }
    }
}