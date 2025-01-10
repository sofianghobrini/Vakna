package com.app.vakna.adapters

// Classe représentant une tâche dans la liste
class ListData(
    var name: String,
    var type: String,
    var importance: String,
    var icon: Int,
    var estTermine: Boolean = false,
    var estArchivee: Boolean = false
) {
    override fun toString(): String {
        return "$name : $type $importance " + if (estTermine) "fini" else "pas fini"
    }
}
