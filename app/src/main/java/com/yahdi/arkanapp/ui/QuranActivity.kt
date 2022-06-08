package com.yahdi.arkanapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.databinding.ActivityQuranBinding

class QuranActivity : AppCompatActivity() {
    private var _binding: ActivityQuranBinding? = null
    private val binding get() = _binding as ActivityQuranBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuranBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.quranToolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.quran_navigation_fragment)
        val navController = navHostFragment?.findNavController()!!

        setupActionBarWithNavController(navController)
    }

    fun setTitle(title: String) {
        binding.quranToolbar.title = title
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.quran_navigation_fragment)
        val navController = navHostFragment?.findNavController()!!
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}