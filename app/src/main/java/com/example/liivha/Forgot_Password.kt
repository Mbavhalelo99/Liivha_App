package com.example.liivha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Forgot_Password : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the Recover button
        val recoverButton: Button = findViewById(R.id.recoverButton)

        // Set click listener for the Recover button
        recoverButton.setOnClickListener {
            // After clicking recover, navigate back to the Get Started screen
            val intent = Intent(this, GetStarted::class.java)
            startActivity(intent)
            finish() // Close the ForgotPasswordActivity so it doesn't remain in the back stack
        }
    }
}