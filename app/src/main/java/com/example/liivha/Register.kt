package com.example.liivha

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Realtime Database and Firebase Authentication
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val surnameEditText: EditText = findViewById(R.id.surnameEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val registerButton: Button = findViewById(R.id.registerButton)

        // Set click listener for the register button
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val surname = surnameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validation for username (lowercase)
            if (!username.matches(Regex("^[a-z]+$"))) {
                Toast.makeText(
                    this,
                    "Username must consist of lowercase letters only",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validation for password (8+ characters, includes a number and special character)
            val passwordPattern = Regex("^(?=.*[0-9])(?=.*[!@#\$%^&+=]).{8,}$")
            if (!password.matches(passwordPattern)) {
                Toast.makeText(
                    this,
                    "Password must be at least 8 characters long, include a number and special character",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Validate email format
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Check if all fields are filled
            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if username or email already exists in the database
            checkUsernameAndEmailExists(username, email, name, surname, password)
        }
    }

    private fun checkUsernameAndEmailExists(
        username: String,
        email: String,
        name: String,
        surname: String,
        password: String
    ) {
        database.child("users").get().addOnSuccessListener { dataSnapshot ->
            var userExists = false
            for (userSnapshot in dataSnapshot.children) {
                val existingUsername = userSnapshot.child("username").value.toString()
                val existingEmail = userSnapshot.child("email").value.toString()
                if (existingUsername == username || existingEmail == email) {
                    userExists = true
                    break
                }
            }

            if (userExists) {
                Toast.makeText(
                    this,
                    "Username or Email already exists. Please choose a different one.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Register user with Firebase Authentication
                registerUserWithAuthentication(email, password, username, name, surname)
            }
        }.addOnFailureListener {
            Toast.makeText(
                this,
                "Error checking database. Please try again later.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun registerUserWithAuthentication(
        email: String,
        password: String,
        username: String,
        name: String,
        surname: String
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseUser = auth.currentUser
                val userId = firebaseUser?.uid

                if (userId != null) {
                    val user = User(userId, name, surname, email, username)
                    database.child("users").child(userId).setValue(user).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish() // Close the current activity
                        } else {
                            Toast.makeText(
                                this,
                                "Failed to register. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Authentication failed. ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

// User data class to store user information in Firebase Realtime Database
data class User(
    val userId: String,
    val name: String,
    val surname: String,
    val email: String,
    val username: String
)

