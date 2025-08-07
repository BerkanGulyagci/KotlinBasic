package com.berkang.heroapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.berkang.heroapp.databinding.ActivityDefinitionBinding
import com.berkang.heroapp.databinding.ActivityMainBinding

class DefinitionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDefinitionBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDefinitionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent = intent
        //val selectedHero=intent.getSerializableExtra("selectedHero", SuperHero::class.java)
        val selectedHero = intent.getSerializableExtra("selectedHero") as SuperHero
        binding.nameTextView.text= selectedHero.name
        binding.imageView.setImageResource(selectedHero.img)
        binding.jobTextView.text= selectedHero.job
    }
}