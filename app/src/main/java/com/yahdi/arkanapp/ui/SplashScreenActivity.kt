package com.yahdi.arkanapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yahdi.arkanapp.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding as ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.apply {
            animate()
                .alpha(1.0f)
                .setDuration(500L)
                .setStartDelay(1000L)
                .start()
        }

        binding.btnStart.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish()
        }
    }
}