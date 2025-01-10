package com.app.vakna.vue

import android.os.Bundle
import android.util.Log
import com.app.vakna.R
import com.app.vakna.controller.ControllerCreerCompagnon
import com.app.vakna.databinding.ActivityCreerCompagnonBinding

class CreerCompagnonActivity: HideKeyboardActivity() {

    private lateinit var binding: ActivityCreerCompagnonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreerCompagnonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BackgroundSetter.applyConstraintLayoutBackground(this, R.id.creerCompagnonLayout)

        ControllerCreerCompagnon(binding)
    }
}