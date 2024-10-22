package com.app.vakna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerCreerCompagnon
import com.app.vakna.databinding.ActivityCreerCompagnonBinding

class CreerCompagnonActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreerCompagnonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreerCompagnonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ControllerCreerCompagnon(binding)
    }
}