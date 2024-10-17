/*
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import com.app.vakna.R
import com.app.vakna.modele.Frequence
import com.app.vakna.modele.GestionnaireDeTaches
import com.app.vakna.modele.Importance
import com.app.vakna.modele.Tache
import com.app.vakna.modele.TypeTache
import java.time.LocalDate


@Controller
class controllerAjouterTache(private val view: View) {

    // Méthode privée pour récupérer le nom de la tâche
    private fun recupererNomTache(): String {
        val nomTacheEditText = view.findViewById<EditText>(R.id.inputNomTache)
        return nomTacheEditText.text.toString()
    }

    // Méthode privée pour récupérer le type de la tâche
    private fun recupererTypeTache(): TypeTache {
        val type = view.findViewById<Spinner>(R.id.selectTypeTache)
        val typeDeLaTache = type.selectedItem.toString()
        return when (typeDeLaTache) {
            "Urgent" -> TypeTache.AUTRE
            "Sport" -> TypeTache.PERSONNELLE
            "Travail" -> TypeTache.PROFESSIONNELLE
            else -> TypeTache.AUTRE
        }
    }

    // Méthode privée pour récupérer la fréquence de la tâche
    private fun recupererFrequenceTache(): Frequence {
        val radioGroupFrequence = view.findViewById<RadioGroup>(R.id.radioFrequenceTache)
        val selectedRadioButtonId = radioGroupFrequence.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.radioQuotidien -> Frequence.QUOTIDIENNE
            R.id.radioHebdomadaire -> Frequence.HEBDOMADAIRE
            R.id.radioMensuel -> Frequence.MENSUELLE
            else -> Frequence.ANNUELLE
        }
    }

    // Méthode privée pour récupérer l'importance de la tâche
    private fun recupererImportanceTache(): Importance?  {
        val radioGroupFrequence = view.findViewById<RadioGroup>(R.id.radioImportanceTache)
        val selectedRadioButtonId = radioGroupFrequence.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.radioFaible -> Importance.FAIBLE
            R.id.radioMoyen -> Importance.MOYENNE
            R.id.radioElevee -> Importance.ELEVEE
            else -> null
        }
    }

    // Méthode publique pour envoyer les informations de la tâche
    fun ConfirmTache(){
        var IdconfirmButton = view.findViewById<Button>(R.id.boutonCreerTache)
        IdconfirmButton.setOnClickListener {
            var nomTache = recupererNomTache()
            var typeTache = recupererTypeTache()
            var importanceTache = recupererImportanceTache()
            var frequenceTache = recupererFrequenceTache()
            val derniereValidation = LocalDate.now()
            val tache = Tache(nomTache, frequenceTache, importanceTache, typeTache, derniereValidation,false)
            var gestionnaireDeTaches = GestionnaireDeTaches()
            gestionnaireDeTaches.ajouterTache(tache)
        }


    }
}

// Définition de l'annotation Controller
annotation class Controller
*/
 