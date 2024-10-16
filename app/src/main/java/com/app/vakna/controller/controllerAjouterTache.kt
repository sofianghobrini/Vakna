import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import com.app.vakna.R

@Controller
class controllerTache(private val view: View){
    private fun recupererNomTache():String?{
        val nomTacheEditText = view.findViewById<EditText>(R.id.inputNomTache)
        val nomTache = nomTacheEditText.text.toString()
        return nomTache
    }
    private fun recupererTypeTache():String?{
        val type = view.findViewById<Spinner>(R.id.selectTypeTache)
        val typeDeLaTache = type.selectedItem.toString()
        return when(typeDeLaTache){
            "Urgent"->{
                "Urgent"
            }
            "Sport"->{
                "Sport"
            }
            "Travail"->{
                "Travail"
            }
            else->"Non défini"

        }
    }
    private fun recupererFrequenceTache(): String? {
        val radioGroupFrequence = view.findViewById<RadioGroup>(R.id.radioFrequenceTache)
        val selectedRadioButtonId = radioGroupFrequence.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.radioQuotidien -> {
                "Quotidien"
            }
            R.id.radioHebdomadaire -> {
                "Hebdomadaire"
            }
            R.id.radioMensuel -> {
                "Mensuel"
            }
            else -> {
                null // Aucun bouton sélectionné
            }
        }
    }
    private fun recupererImportanceTache():String{
        val IdImportance  = view.findViewById<CheckBox>(R.id.checkboxImportance)
        return if(IdImportance.isChecked){
            "Important"
        }
        else{
            "Pas important"
        }
    }
    fun envoyerInformationTache() : List<String?>{
        val nomTache = recupererNomTache()
        val typeTache = recupererTypeTache()
        val importanceTache = recupererImportanceTache()
        val frequenceTache = recupererFrequenceTache()
        val donneesEnListe = listOf(nomTache, typeTache, frequenceTache, importanceTache)

        return donneesEnListe
    }
}

annotation class Controller