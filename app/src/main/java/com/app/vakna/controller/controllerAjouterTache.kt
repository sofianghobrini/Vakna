import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import com.app.vakna.R

@Controller
class controllerAjouterTache(private val view: View) {

    // Méthode privée pour récupérer le nom de la tâche
    private fun recupererNomTache(): String {
        val nomTacheEditText = view.findViewById<EditText>(R.id.inputNomTache)
        return nomTacheEditText.text.toString()
    }

    // Méthode privée pour récupérer le type de la tâche
    private fun recupererTypeTache(): String {
        val type = view.findViewById<Spinner>(R.id.selectTypeTache)
        val typeDeLaTache = type.selectedItem.toString()
        return when (typeDeLaTache) {
            "Urgent" -> "Urgent"
            "Sport" -> "Sport"
            "Travail" -> "Travail"
            else -> "Non défini"
        }
    }

    // Méthode privée pour récupérer la fréquence de la tâche
    private fun recupererFrequenceTache(): String? {
        val radioGroupFrequence = view.findViewById<RadioGroup>(R.id.radioFrequenceTache)
        val selectedRadioButtonId = radioGroupFrequence.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.radioQuotidien -> "Quotidien"
            R.id.radioHebdomadaire -> "Hebdomadaire"
            R.id.radioMensuel -> "Mensuel"
            else -> null // Aucun bouton sélectionné
        }
    }

    // Méthode privée pour récupérer l'importance de la tâche
    private fun recupererImportanceTache(): String {
        val IdImportance = view.findViewById<CheckBox>(R.id.checkboxImportance)
        return if (IdImportance.isChecked) {
            "Important"
        } else {
            "Pas important"
        }
    }

    // Méthode publique pour envoyer les informations de la tâche
    fun envoyerInformationTache(): List<String?> {
        val nomTache = recupererNomTache()
        val typeTache = recupererTypeTache()
        val importanceTache = recupererImportanceTache()
        val frequenceTache = recupererFrequenceTache()

        // Créer une liste avec les données récupérées
        return listOf(nomTache, typeTache, frequenceTache, importanceTache)
    }
}

// Définition de l'annotation Controller
annotation class Controller