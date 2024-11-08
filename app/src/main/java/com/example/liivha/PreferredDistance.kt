package com.example.liivha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class PreferredDistance : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var distanceEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_distance)

        db = FirebaseFirestore.getInstance()
        distanceEditText = findViewById(R.id.distanceEditText)

        // Save button listener to save preferred distance
        findViewById<Button>(R.id.saveButton).setOnClickListener { savePreferredDistance() }

        val bckBtn = findViewById<ImageView>(R.id.bckBtnS)
        bckBtn.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun savePreferredDistance() {
        val distance = distanceEditText.text.toString().toLongOrNull()
        if (distance != null) {
            val data = hashMapOf("preferredDistance" to distance)
            db.collection("settings").document("userSettings").set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Preferred distance saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        "Failed to save distance: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(this, "Please enter a valid distance", Toast.LENGTH_SHORT).show()
        }
    }
}
