// Fichier modeleTache.kt

class Tache(
    nom: String,
    frequence: Frequence,
    importance: Importance,
    type: TypeTache
) {
    var nom: String = nom
        get() = field
        set(value) {
            if (value.isBlank()) throw IllegalArgumentException("Le nom ne peut pas être vide")
            field = value
        }

    var frequence: Frequence = frequence
    var importance: Importance = importance
    var type: TypeTache = type
}

class GestionnaireDeTaches {
    private val listeDeTaches = mutableListOf<Tache>()

    fun ajouterTache(tache: Tache) {
        listeDeTaches.add(tache)
    }
    fun modifierTache(nom: String, nouvelleTache: Tache): Boolean {
        val tache = listeDeTaches.find { it.nom == nom }
        return if (tache != null) {
            tache.nom = nouvelleTache.nom
            tache.frequence = nouvelleTache.frequence
            tache.importance = nouvelleTache.importance
            tache.type = nouvelleTache.type
            true
        } else {
            false
        }
    }
    fun supprimerTache(nom: String): Boolean {
        val tache = listeDeTaches.find { it.nom == nom }
        return if (tache != null) {
            listeDeTaches.remove(tache)
            true
        } else {
            false
        }
    }
    fun obtenirTaches(): List<Tache> {
        return listeDeTaches
    }
}

// Enums pour la tâche
enum class Frequence { QUOTIDIENNE, HEBDOMADAIRE, MENSUELLE, ANNUELLE }
enum class Importance { FAIBLE, MOYENNE, ELEVEE }
enum class TypeTache { PROJET, SPORT, ETUDE, HABITUDE, AUTRE }
