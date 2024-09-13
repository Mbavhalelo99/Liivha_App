package com.example.liivha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login<EditText> : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find views by ID
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val forgotPasswordTextView: TextView = findViewById(R.id.forgotPasswordTextView)
        val signUpTextView: TextView = findViewById(R.id.signUpTextView)

        // Set click listener for the login button
        loginButton.setOnClickListener {
            // Retrieve the input data from EditText fields
            val username = usernameEditText.toString()
            val password = passwordEditText.toString()

            // Add your authentication logic here (e.g., validate inputs, authenticate user)
            // Navigate to the home screen or show error messages
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        // Set click listener for the Forgot Password text
        forgotPasswordTextView.setOnClickListener {
            // Navigate to Forgot Password screen
            val intent = Intent(this, Forgot_Password::class.java)
            startActivity(intent)
        }

        // Set click listener for the Sign Up text
        signUpTextView.setOnClickListener {
            // Navigate to Sign Up screen
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        // Find the root layout of the activity
        //val main: ConstraintLayout = findViewById(R.id.main)

        // Set the OnSwipeTouchListener to detect the swipe gesture
        //main.setOnTouchListener(object : OnSwipeTouchListener(this) {
        //override fun onSwipeRight() {
        // When swipe right, navigate back to the "Getting Started" screen
        //val intent = Intent(this@Login, GetStarted::class.java)
        //startActivity(intent)
        //finish() // Close the current activity
        //Toast.makeText(this@Login, "Swiped Right!", Toast.LENGTH_SHORT).show()
        //}
        //})
    }
}