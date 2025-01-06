package com.app.vakna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerDetailsRefuge
import com.app.vakna.databinding.ActivityDetailsRefugesBinding

class DetailsRefugeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsRefugesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsRefugesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ControllerDetailsRefuge(binding, intent)
    }
}