package com.yahdi.arkanapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.data.response.SurahResponse
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

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            Log.d("Changed", destination.displayName)

            binding.quranToolbar.title = when (destination.id) {
                R.id.surahFragment -> (arguments?.getParcelable<SurahResponse>("surahData"))?.name
                R.id.quranContentsFragment -> "Al-Qur'an"
                R.id.searchFragment -> "Search"
                R.id.ayahFragment -> {
                    val ayahData = arguments?.getParcelable<AyahResponse>("ayahData")
                    ayahData?.let { ayah ->
                        ayah.surah?.let { surah ->
                            "${surah.name} ${surah.id}:${ayah.idInSurah}"
                        }
                    }
                }
                else -> "Al-Qur'an"
            }
        }

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