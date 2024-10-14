import java.time.LocalDate

class Tache(
    nom: String,
    frequence: Frequence,
    importance: Importance,
    type: TypeTache,
    date: LocalDate,
    estTerminee: Boolean = false,
    private val gestionnaire: GestionnaireDeTaches
) {
    private var _nom: String = nom
    var nom: String
        get() = _nom
        set(value) {
            if (value.isBlank()) {
                throw IllegalArgumentException("Le nom ne peut pas être vide")
            }
            if (gestionnaire.obtenirTaches().any { it.nom == value }) {
                throw IllegalArgumentException("Le nom '$value' est déjà utilisé pour une autre tâche")
            }
            _nom = value
        }

    var frequence: Frequence = frequence
    var importance: Importance = importance
    var type: TypeTache = type
    var date: LocalDate = date
    var estTerminee: Boolean = estTerminee
}

class GestionnaireDeTaches {
    private val listeDeTaches = mutableListOf<Tache>()

    fun ajouterTache(tache: Tache) {
        listeDeTaches.add(tache)
    }

    fun modifierTache(nom: String, nouvelleTache: Tache) {
        val tache = listeDeTaches.find { it.nom == nom }
        if (tache != null) {
            tache.nom = nouvelleTache.nom
            tache.frequence = nouvelleTache.frequence
            tache.importance = nouvelleTache.importance
            tache.type = nouvelleTache.type
            tache.date = nouvelleTache.date
            tache.estTerminee = nouvelleTache.estTerminee
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun supprimerTache(nom: String) {
        val tache = listeDeTaches.find { it.nom == nom }
        if (tache != null) {
            listeDeTaches.remove(tache)
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }

    fun finirTache(nom: String) {
        val tache = listeDeTaches.find { it.nom == nom }
        if (tache != null) {
            tache.estTerminee = true
        } else {
            throw IllegalArgumentException("Tâche avec le nom $nom introuvable")
        }
    }


    // Obtenir la liste des tâches
    fun obtenirTaches(): List<Tache> {
        return listeDeTaches
    }
}

// Enums pour la tâche
enum class Frequence { QUOTIDIENNE, HEBDOMADAIRE, MENSUELLE, ANNUELLE }
enum class Importance { FAIBLE, MOYENNE, ELEVEE }
enum class TypeTache { PERSONNELLE, PROFESSIONNELLE, AUTRE }
