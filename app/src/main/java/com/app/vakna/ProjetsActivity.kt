package com.app.vakna

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.app.vakna.controller.ControllerAjouterTache
import com.app.vakna.databinding.ActivityAjouterBinding
import com.app.vakna.modele.dao.TacheDAO

class ProjetsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAjouterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAjouterBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}