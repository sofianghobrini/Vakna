package com.app.vakna.vue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vakna.R
import com.app.vakna.controller.ControllerArchives
import com.app.vakna.controller.ControllerDetailsObjet
import com.app.vakna.databinding.ActivityArchivesBinding
import com.app.vakna.databinding.ActivityDetailsObjetBinding

class ArchivesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArchivesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArchivesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BackgroundSetter.applyConstraintLayoutBackground(this, R.id.archivesLayout)

        ControllerArchives(binding)
    }
}