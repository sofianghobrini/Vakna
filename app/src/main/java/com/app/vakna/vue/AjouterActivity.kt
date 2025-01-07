package com.app.vakna.vue

import android.os.Bundle
import com.app.vakna.controller.ControllerAjouterTache
import com.app.vakna.databinding.ActivityAjouterBinding

class AjouterActivity : HideKeyboardActivity() {

    private lateinit var binding: ActivityAjouterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAjouterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ControllerAjouterTache(binding)
    }
}