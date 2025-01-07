package com.app.vakna.vue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerGerer
import com.app.vakna.databinding.ActivityGererBinding

class GererActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGererBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGererBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ControllerGerer(binding)
    }
}
