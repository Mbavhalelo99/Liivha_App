package com.example.liivha

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Settings : AppCompatActivity() {

    private lateinit var metricSwitch: Switch
    private lateinit var distanceSeekBar: SeekBar
    private lateinit var maxDistanceLabel: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("LiivhaSettings", MODE_PRIVATE)

        metricSwitch = findViewById(R.id.metricSwitch)
        distanceSeekBar = findViewById(R.id.distanceSeekBar)
        maxDistanceLabel = findViewById(R.id.maxDistanceLabel)

        // Load saved settings
        loadSettings()

        // Toggle metric/imperial system
        metricSwitch.setOnCheckedChangeListener { _, isChecked ->
            val unitSystem = if (isChecked) "Metric" else "Imperial"
            sharedPreferences.edit().putString("UnitSystem", unitSystem).apply()
        }

        // SeekBar listener to update maximum distance
        distanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val unit = if (metricSwitch.isChecked) "km" else "miles"
                maxDistanceLabel.text = "Maximum Distance ($progress $unit)"
                sharedPreferences.edit().putInt("MaxDistance", progress).apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Optional: Handle touch events when user starts moving the SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optional: Handle touch events when user stops moving the SeekBar
            }
        })
    }

    private fun loadSettings() {
        val unitSystem = sharedPreferences.getString("UnitSystem", "Metric")
        val maxDistance = sharedPreferences.getInt("MaxDistance", 50)

        metricSwitch.isChecked = unitSystem == "Metric"
        distanceSeekBar.progress = maxDistance
        val unit = if (metricSwitch.isChecked) "km" else "miles"
        maxDistanceLabel.text = "Maximum Distance ($maxDistance $unit)"
    }
}
