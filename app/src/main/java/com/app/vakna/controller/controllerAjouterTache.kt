package com.app.vakna.controller
import android.widget.EditText
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import com.app.vakna.R

@Controller
class controllerCreationTache(private val view: View){
    fun recupererNomTache(){
        val nomTacheEditText = view.findViewById<EditText>(R.id.editTextNomTache)
        val nomTache = nomTacheEditText.text.toString()
    }
    fun recupererTypeTache(){
        val type = view.findViewById<Spinner>(R.id.spinnerCategory)
        val typeDeLaTache = spinnerCategory.selectedItem.toString()
    }
    fun recupererFrequenceTache(){
        val radioGroupPriority = view.findViewById<RadioGroup>(R.id.radioGroupPriority)
        val selectedRadioButtonId = radioGroupPriority.checkedRadioButtonId

        when(selectedRadioButtonId){

        }

        /*if (selectedRadioButtonId != -1) {
            val selectedRadioButton = view.findViewById<RadioButton>(selectedRadioButtonId)
            val selectedPriority = selectedRadioButton.text.toString()
        }*/
    }
}

annotation class Controller
