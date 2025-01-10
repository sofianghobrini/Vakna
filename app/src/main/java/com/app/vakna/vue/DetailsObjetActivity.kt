package com.app.vakna.vue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.R
import com.app.vakna.controller.ControllerDetailsObjet
import com.app.vakna.databinding.ActivityDetailsObjetBinding

class DetailsObjetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsObjetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsObjetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BackgroundSetter.applyConstraintLayoutBackground(this, R.id.detailsObjetLayout)

        ControllerDetailsObjet(binding, intent)
    }
}