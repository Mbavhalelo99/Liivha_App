package com.example.liivha

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the logo ImageView
        val logoImageView: ImageView = findViewById(R.id.logoimageView)

        // Load and start the animation
        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.splashanimation)
        logoImageView.startAnimation(logoAnimation)

        // Delay for 3 seconds before moving to the MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, GetStarted::class.java))
            finish()
        }, 3000) // 3-second delay
    }
}