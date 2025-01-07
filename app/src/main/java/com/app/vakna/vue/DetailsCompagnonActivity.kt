package com.app.vakna.vue

import android.os.Bundle
import com.app.vakna.controller.ControllerDetailsCompagnon
import com.app.vakna.databinding.ActivityDetailsCompagnonBinding

class DetailsCompagnonActivity : HideKeyboardActivity() {

    private lateinit var binding: ActivityDetailsCompagnonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsCompagnonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ControllerDetailsCompagnon(binding, intent)
    }
}