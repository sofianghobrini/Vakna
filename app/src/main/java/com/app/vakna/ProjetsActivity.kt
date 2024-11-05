package com.app.vakna

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.app.vakna.controller.ControllerAjouterTache
import com.app.vakna.controller.ControllerGerer
import com.app.vakna.databinding.ActivityAjouterBinding
import com.app.vakna.databinding.ActivityGererBinding
import com.app.vakna.modele.dao.TacheDAO

class ProjetsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGererBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityGererBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val root: View = binding.root

        ControllerGerer(binding)
    }

}