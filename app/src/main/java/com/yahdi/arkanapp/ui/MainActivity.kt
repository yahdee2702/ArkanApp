package com.yahdi.arkanapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.azan.Azan
import com.azan.Method
import com.azan.astrologicalCalc.Location
import com.azan.astrologicalCalc.SimpleDate
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.databinding.ActivityMainBinding
import com.yahdi.arkanapp.utils.GPSTracker
import com.yahdi.arkanapp.utils.Utils
import java.text.SimpleDateFormat
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var _tracker: GPSTracker? = null
    private val tracker get() = _tracker as GPSTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        _tracker = GPSTracker(this)
        setContentView(binding.root)

        binding.cvQuran.setOnClickListener {
            startActivity(
                Intent(this, QuranActivity::class.java),
            )
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
                    checkForLocation()
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
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1000
            )
            return
        }
        checkForLocation()
    }

    private fun checkForLocation() {
        tracker.location.let {
            val calendar = GregorianCalendar()
            val today = SimpleDate(calendar)

            val mLocation = Location(it.latitude, it.longitude, Utils.getGMTDifference(calendar), 0)
            val azan = Azan(mLocation, Method.KARACHI_SHAF)

            azan.getPrayerTimes(today).let { time ->
                binding.apply {
                    tvFajrTime.text = Utils.formatPrayerTime(time.fajr())
                    tvSunriseTime.text = Utils.formatPrayerTime(time.shuruq())
                    tvDhuhrTime.text = Utils.formatPrayerTime(time.thuhr())
                    tvAsrTime.text = Utils.formatPrayerTime(time.assr())
                    tvMaghribTime.text = Utils.formatPrayerTime(time.maghrib())
                    tvIshaaTime.text = Utils.formatPrayerTime(time.ishaa())
                }
            }
        }
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
}