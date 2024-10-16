/*import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import com.app.vakna.R

@Controller
class controllerTache(private val view: View){
    fun recupererNomTache(){
        val nomTacheEditText = view.findViewById<EditText>(R.id.editTextNomTache)
        val nomTache = nomTacheEditText.text.toString()
    }
    fun recupererTypeTache():String?{
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
    fun recupererFrequenceTache(): String? {
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
    fun recupererImportanceTache():String{
        val IdImportance  = view.findViewById<CheckBox>(R.id.checkboxImportance)
        if(IdImportance.isChecked){
            return "Important"
        }
        else{
            return "Pas important"
        }
    }
    fun EnvoyerInformationTache(){

    }
}

annotation class Controller
*/