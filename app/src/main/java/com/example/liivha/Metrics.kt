package com.example.liivha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Metrics : AppCompatActivity() {

    private lateinit var metricsRadioGroup: RadioGroup
    private lateinit var saveButton: Button
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metrics)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Find views
        metricsRadioGroup = findViewById(R.id.metricsRadioGroup)
        saveButton = findViewById(R.id.saveButton)

        // Save button click listener
        saveButton.setOnClickListener {
            saveMetricChoice()
        }

        val bckBtn = findViewById<ImageView>(R.id.bckBtnS)
        bckBtn.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveMetricChoice() {
        // Get the selected RadioButton
        val selectedId = metricsRadioGroup.checkedRadioButtonId
        if (selectedId == -1) {
            // No radio button selected
            Toast.makeText(this, "Please select a metric.", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the text of the selected radio button
        val selectedMetric = findViewById<RadioButton>(selectedId).text.toString()

        // Save to Firestore
        val userMetrics = hashMapOf("metric" to selectedMetric)
        firestore.collection("settings").document("userSettings")
            .set(userMetrics)
            .addOnSuccessListener {
                Toast.makeText(this, "Metric saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save metric: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}

