package com.app.vakna.adapters

// Adapter qui gère les boutons d'archivage et de modification des tâches
class ListAdapterBoutons(
    dataArrayList: ArrayList<ListData>,
    private val onArchiveClick: (String) -> Unit,
    private val onModifierClick: (String) -> Unit
) : ListAdapter(dataArrayList) {

    override fun onBindViewHolder(holder: TachesViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val listData = dataArrayList[position]

        // Gestion du clic sur le bouton d'archivage
        holder.boutonArchiverTache?.setOnClickListener {
            onArchiveClick(listData.name)
        }

        // Gestion du clic sur le bouton de modification
        holder.boutonModifierTache?.setOnClickListener {
            onModifierClick(listData.name)
        }
    }
}
