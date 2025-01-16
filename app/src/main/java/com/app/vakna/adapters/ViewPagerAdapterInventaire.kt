package com.app.vakna.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.recyclerview.widget.RecyclerView
import com.app.vakna.R
import com.app.vakna.controller.NavigationHandler
import com.app.vakna.databinding.FragmentCompagnonBinding
import com.app.vakna.modele.dao.TypeObjet
import com.app.vakna.modele.dao.objetobtenu.ObjetObtenu

class ViewPagerAdapterInventaire(
    private val binding: FragmentCompagnonBinding,
    private val context: Context,
    private val pages: List<List<GridConsommableData>>,
    private val items: List<ObjetObtenu>,
    private val type : List<TypeObjet>
) : RecyclerView.Adapter<ViewPagerAdapterInventaire.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.liste_inventaire, parent, false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val page = pages[position]
        val types = type[position]

        val gridView = holder.gridView


        if (items.isEmpty()) {

            gridView.numColumns = 1
            val message = when (types) {
                TypeObjet.JOUET -> context.getString(R.string.message_inventaire_jouet_vide)
                TypeObjet.NOURRITURE -> context.getString(R.string.message_inventaire_nourriture_vide)
            }

            val placeholder = listOf(
                InventaireVideData(
                    message = message,
                    buttonText = context.getString(R.string.titre_magasin),
                    buttonAction = {
                        NavigationHandler.navigationFragmentVersFragment(context, R.id.navigation_magasin, message)
                    }
                )
            )
            gridView.adapter = InventaireVideAdapter(binding, placeholder)
            gridView.adapter
        } else {
            gridView.numColumns = 2
            gridView.adapter = GridConsommableAdapterInventaire(binding, ArrayList(page))
            gridView.adapter
        }
    }

    override fun getItemCount(): Int = pages.size

    fun getTypeCount(): Int = type.size


    inner class PagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gridView: GridView = view.findViewById(R.id.grid_view_items_inventaire)
    }
}

