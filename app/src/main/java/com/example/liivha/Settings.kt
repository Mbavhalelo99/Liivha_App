package com.example.liivha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Settings : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var metricsValue: TextView
    private lateinit var distanceValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize Firestore
        firestore = Firebase.firestore

        // Reference to TextViews
        metricsValue = findViewById(R.id.metricsValue)
        distanceValue = findViewById(R.id.distanceValue)


        findViewById<Button>(R.id.metricsButton).setOnClickListener {
            startActivity(Intent(this, Metrics::class.java))
        }

        findViewById<Button>(R.id.distanceButton).setOnClickListener {
            startActivity(Intent(this, PreferredDistance::class.java))
        }

        val bckBtn = findViewById<ImageView>(R.id.bckBtnS)
        bckBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        // Fetch data from Firestore
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        // Replace "userSettings" with your Firestore collection name and "userId" with the document ID
        firestore.collection("userSettings").document("userId")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Assuming fields "metrics" and "preferredDistance" are in your Firestore document
                    val metrics = document.getString("metrics")
                    val distance = document.getString("preferredDistance")

                    // Update TextViews
                    metricsValue.text = metrics ?: "KM"  // Default to "KM" if metrics is null
                    distanceValue.text =
                        distance ?: "40Km"  // Default to "40Km" if distance is null
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                metricsValue.text = "Error"
                distanceValue.text = "Error"
            }
    }
}



