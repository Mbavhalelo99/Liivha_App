package com.example.liivha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PopularPlaces2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_places2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Back button (bckBtn)
        val bckBtn = findViewById<ImageView>(R.id.bckBtn)
        bckBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish() // Optionally, to prevent going back to this activity
        }

        // Directions button
        val directionsBtn = findViewById<Button>(R.id.button)
        directionsBtn.setOnClickListener {
            val intent = Intent(this, Location1::class.java)
            startActivity(intent)
        }
    }
}