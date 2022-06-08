package com.yahdi.arkanapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
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

    private var timerRunnable: Runnable? = null
    private val timerHandler = Handler(Looper.getMainLooper())

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
        if (!tracker.isAvailable) return

        val location = tracker.location
        val calendar = GregorianCalendar()
        val today = SimpleDate(calendar)

        val mLocation = Location(location.latitude, location.longitude, Utils.getGMTDifference(calendar), 0)
        val azan = Azan(mLocation, Method.KARACHI_SHAF)

        azan.getPrayerTimes(today).let { time ->
            val prayerTimes = arrayListOf(
                time.fajr(),
                time.shuruq(),
                time.thuhr(),
                time.assr(),
                time.maghrib(),
                time.ishaa()
            ).map{ prayerTime ->
                Utils.changePrayerTimeToDate(prayerTime)
            }

            binding.apply {
                tvFajrTime.text = Utils.formatBasedOnSystemFormat(this@MainActivity, prayerTimes[0])
                tvSunriseTime.text = Utils.formatBasedOnSystemFormat(this@MainActivity, prayerTimes[1])
                tvDhuhrTime.text = Utils.formatBasedOnSystemFormat(this@MainActivity, prayerTimes[2])
                tvAsrTime.text = Utils.formatBasedOnSystemFormat(this@MainActivity, prayerTimes[3])
                tvMaghribTime.text = Utils.formatBasedOnSystemFormat(this@MainActivity, prayerTimes[4])
                tvIshaaTime.text = Utils.formatBasedOnSystemFormat(this@MainActivity, prayerTimes[5])
                tvLocation.text = getString(R.string.txt_location).format(tracker.getCityName(), tracker.getCountryName())
            }

            timerHandler.postDelayed(object: Runnable {
                override fun run(){
                    val calendarNow = Calendar.getInstance()
                    val nextPrayer = Utils.getNextPrayer(this@MainActivity, calendarNow.time, time)
                    val currentImgState = Utils.getImageTimeStateFromPrayer(calendarNow.time, time)

                    binding.ivImageTime.setImageResource(currentImgState)


                    for (prayerTime in prayerTimes) {
                        val timeLeft = prayerTime.time - calendarNow.time.time
                        val index = prayerTimes.indexOf(prayerTime)

                        if (timeLeft > 0) {
                            binding.tvTimeLeft.text = getString(R.string.txt_time_detail).format(DateUtils.formatElapsedTime(timeLeft/1000), nextPrayer)
                            break
                        }
                    }

                    timerRunnable = this
                    timerHandler.postDelayed(this, 1000)
                }
            }, 10)
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

    override fun onDestroy() {
        super.onDestroy()
        if (timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable!!)
        }
    }
}