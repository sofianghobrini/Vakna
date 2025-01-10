package com.app.vakna.vue

import android.os.Bundle
import com.app.vakna.R
import com.app.vakna.controller.ControllerModifierTache
import com.app.vakna.databinding.ActivityModifierBinding

class ModifierActivity : HideKeyboardActivity() {

    private lateinit var binding: ActivityModifierBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityModifierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BackgroundSetter.applyConstraintLayoutBackground(this, R.id.container)

        ControllerModifierTache(binding, intent)
    }
}