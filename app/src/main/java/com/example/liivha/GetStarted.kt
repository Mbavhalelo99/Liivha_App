package com.example.liivha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GetStarted : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        // Apply window insets to handle system bars like status and navigation bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the "Get Started" button
        val getStartedButton: Button = findViewById(R.id.getStartedbtn)

        // Set an onClickListener for the button to navigate to the login screen
        getStartedButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish() // Close the current activity
        }
    }
}
