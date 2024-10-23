package com.app.vakna

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerGerer
import com.app.vakna.databinding.ActivityGererBinding

class GererActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGererBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityGererBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val root: View = binding.root

        ControllerGerer(binding)
    }
}
