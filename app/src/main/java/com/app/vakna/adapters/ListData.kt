package com.app.vakna.adapters

// Classe représentant une tâche dans la liste
class ListData(
    var name: String,
    var type: String,
    var importance: String,
    var icon: Int,
    var estTermine: Boolean? = false, // Statut de la tâche terminée (par défaut false)
    var estArchivee: Boolean = false // Statut de la tâche archivée (par défaut false)
) {
    override fun toString(): String {
        return "$name : $type $importance " + if (estTermine == true) "fini" else "pas fini"
    }
}
