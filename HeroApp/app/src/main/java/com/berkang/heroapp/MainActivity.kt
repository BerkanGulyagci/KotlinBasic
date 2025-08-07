package com.berkang.heroapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.berkang.heroapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var SuperHeroList : ArrayList<SuperHero>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

val hulk = SuperHero("Hulk", "Scientist", R.drawable.hulk)
val ironMan = SuperHero("Iron Man ", "Genius", R.drawable.ironman)
val spiderMan = SuperHero("Spider Man", "High School Student", R.drawable.spiderman)
val thor=SuperHero("Thor","God",R.drawable.thor)

SuperHeroList= arrayListOf(hulk,ironMan,spiderMan,thor)

val adapter = SuperHeroAdapter(SuperHeroList)
  binding.HeroRecycler.layoutManager=GridLayoutManager(this,2)
        binding.HeroRecycler.adapter = adapter

    }
}