package com.app.vakna.adapters
class ListData(
    var name: String,
    var type: String,
    var importance: String,
    var icon: Int,
    var estTermine: Boolean? = null,
    var estArchivee: Boolean
) {
    override fun toString(): String {
        return "$name : $type $importance " + if(estTermine == null || !estTermine!!) "pas fini" else "fini"
    }
}