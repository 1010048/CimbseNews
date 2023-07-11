package com.example.cimbsenews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cimbsenews.R
import android.content.Intent
import android.os.Handler


class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        // Delay the start of the next activity
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}
