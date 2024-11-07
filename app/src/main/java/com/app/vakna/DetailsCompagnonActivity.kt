package com.app.vakna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.controller.ControllerDetailsCompagnon
import com.app.vakna.controller.ControllerDetailsObjet
import com.app.vakna.databinding.ActivityDetailsCompagnonBinding
import com.app.vakna.databinding.ActivityDetailsObjetBinding

class DetailsCompagnonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsCompagnonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsCompagnonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ControllerDetailsCompagnon(binding, intent)
    }
}