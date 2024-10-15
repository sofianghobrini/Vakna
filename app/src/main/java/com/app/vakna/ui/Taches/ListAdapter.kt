package com.app.vakna.ui.Taches

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.app.vakna.R

class ListAdapter(context: TachesFragment, dataArrayList: ArrayList<ListData?>?) :
    ArrayAdapter<ListData?>(context.requireContext(), R.layout.liste_taches, dataArrayList!!) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        var view = view
        val listData = getItem(position)

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.liste_taches, parent, false)
        }

        val listTypeIcon = view!!.findViewById<ImageView>(R.id.listTypeImage)
        val listName = view.findViewById<TextView>(R.id.listName)
        val listType = view.findViewById<TextView>(R.id.listType)
        val listImportance = view.findViewById<TextView>(R.id.listImportance)


        listTypeIcon.setImageResource(listData!!.icon)
        listName.text = listData.name
        listType.text = listData.type
        listImportance.text = if(listData.importance) "Importante" else "Facultative"

        return view
    }
}