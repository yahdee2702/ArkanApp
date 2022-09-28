package com.yahdi.arkanapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.ActivityNavigator
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.databinding.ActivityMainBinding
import com.yahdi.arkanapp.utils.*
import com.yahdi.arkanapp.utils.Utils.formatBasedOnSystemFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val prayerChanging = LooperListener()
        .setDelay(100000L)

    private val timeChanged = LooperListener()
        .setDelay(1000L)

    private lateinit var mApplication: ArkanApplication

    private val tracker: GPSTracker by lazy {
        mApplication.gpsTracker
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        mApplication = application as ArkanApplication
        setContentView(binding.root)

        binding.cvQuran.setOnClickListener {
            val destination = ActivityNavigator(this)
                .createDestination()
                .setIntent(Intent(this@MainActivity, QuranActivity::class.java))

            ActivityNavigator(this).navigate(destination, null, null, null)
        }

        applyTime()
        checkForPermission(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("Permission", "acquired")
                    setupAzan()
                } else {
                    Toast.makeText(
                        this, "You need to grant permission to access location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkForPermission(mContext: Context) {
        if (!Utils.canAccessLocation(mContext)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ),
                1000
            )
            return
        }

        setupAzan()
    }

    private fun setupAzan() {
        Log.d("Tracker", tracker.isAvailable.toString())
        if (!tracker.isAvailable) {
            LooperListener()
                .setDelay(2000)
                .setListener {
                    if (!Utils.canAccessLocation(this)) {
                        it.remove()
                        return@setListener
                    }
                    setupAzan()
                    it.remove()
                }.start()
            return
        }

        Log.d("Location", "available")

        prayerChanging
            .setListener {
                val today = Calendar.getInstance()
                val prayer = Prayer(tracker.location, today)
                binding.apply {
                    tvFajrTime.text = prayer.fajr.formatBasedOnSystemFormat(this@MainActivity)
                    tvSunriseTime.text = prayer.shuruq.formatBasedOnSystemFormat(this@MainActivity)
                    tvDhuhrTime.text = prayer.dhuhur.formatBasedOnSystemFormat(this@MainActivity)
                    tvAsrTime.text = prayer.assr.formatBasedOnSystemFormat(this@MainActivity)
                    tvMaghribTime.text = prayer.maghrib.formatBasedOnSystemFormat(this@MainActivity)
                    tvIshaaTime.text = prayer.ishaa.formatBasedOnSystemFormat(this@MainActivity)
                    tvLocation.text = getString(R.string.txt_location).format(
                        tracker.getCityName(),
                        tracker.getCountryName()
                    )
                }
            }

        timeChanged
            .setListener {
                val today = Calendar.getInstance()
                val prayer = Prayer(tracker.location, today)
                val timeState = TimeState(prayer)
                val nextPrayer = prayer.getNextPrayer()
                val timeLeft = prayer.getNextPrayerTime().time - today.time.time
                val nextPrayerString = Prayer.getPrayerStringId(nextPrayer)

                binding.tvTimeLeft.text = getString(R.string.txt_time_detail).format(
                    DateUtils.formatElapsedTime(timeLeft / 1000),
                    getString(nextPrayerString)
                )
                binding.ivImageTime.setImageResource(timeState.getImageId())
            }

        timeChanged.start()
        prayerChanging.start()
    }

    private fun applyTime() {
        val today = Calendar.getInstance()
        val normalFormatter = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        val hijrahDate = HijrahDate.now()
        val hijrahFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())

        binding.apply {
            tvCalenderDateTime.text = getString(R.string.txt_date).format(
                normalFormatter.format(today.time),
                hijrahFormatter.format(hijrahDate)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        timeChanged.start()
        prayerChanging.start()
    }

    override fun onPause() {
        super.onPause()
        timeChanged.stop()
        prayerChanging.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        timeChanged.remove()
        prayerChanging.remove()
    }
}