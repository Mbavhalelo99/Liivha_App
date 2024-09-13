package com.example.liivha

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Map button
        val mapBtn = findViewById<ImageView>(R.id.mapBtn)
        mapBtn.setOnClickListener {
            val intent = Intent(this, MapView::class.java)
            startActivity(intent)
        }

        // Settings button
        val settingsBtn = findViewById<ImageView>(R.id.settingsBtn)
        settingsBtn.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        // Popular Place 1 button
        val popLoc1Btn = findViewById<ImageView>(R.id.popLoc1Btn)
        popLoc1Btn.setOnClickListener {
            val intent = Intent(this, PopularPlaces::class.java)
            startActivity(intent)
        }

        // Popular Place 2 button
        val popLoc2Btn = findViewById<ImageView>(R.id.popLoc2Btn)
        popLoc2Btn.setOnClickListener {
            val intent = Intent(this, PopularPlaces::class.java)
            startActivity(intent)
        }
    }
}