package com.app.vakna

import android.content.pm.PackageManager
import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerCreerCompagnon
import com.app.vakna.databinding.ActivityCreerCompagnonBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CreerCompagnonActivity: HideKeyboardActivity() {

    private lateinit var binding: ActivityCreerCompagnonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreerCompagnonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ControllerCreerCompagnon(binding)
    }
}