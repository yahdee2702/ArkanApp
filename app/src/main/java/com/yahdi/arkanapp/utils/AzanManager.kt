package com.yahdi.arkanapp.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.azan.Azan
import com.azan.AzanTimes
import com.azan.Method
import com.azan.astrologicalCalc.Location
import com.azan.astrologicalCalc.SimpleDate
import java.text.SimpleDateFormat
import java.util.*

class AzanManager(val gpsTracker: GPSTracker) {
    private var mListener: AzanListener? = null

    private var timerDelay: Long = 1000
    private var timerRunnable: Runnable? = null
    private val timerHandler = Handler(Looper.getMainLooper())

    private val myRunnable: Runnable = object: Runnable {
        override fun run() {
            timerRunnable = this
            timerHandler.postDelayed(timerRunnable!!, timerDelay)
            if (mListener == null) return
            mListener!!.onUpdated()
        }
    }

    interface AzanListener {
        fun onSetup(azan: AzanTimes)
        fun onChanged()
        fun onUpdated()
    }

    private fun getGMTDifference(calendar: Calendar): Double {
        val localTime: String = SimpleDateFormat("Z", Locale.getDefault()).format(calendar.time)
        val method = localTime.toCharArray()[0]
        val numbers = localTime.substring(1).toDoubleOrNull() ?: 0.0
        val gmt = numbers / 100.0

        return if (method == '+') gmt else -gmt
    }

    private fun getLocation(): Location? {
        if (!gpsTracker.isAvailable) return null

        val calendar = GregorianCalendar()
        val gmtDifference = getGMTDifference(calendar)

        return Location(gpsTracker.location.latitude, gpsTracker.location.longitude, gmtDifference, 0)
    }

    fun setUpdateDelay(amount: Long): AzanManager {
        timerDelay = amount
        return this
    }

    fun start() {
        if (mListener == null) return
        if (timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable!!)
        }

        val azanLocation = getLocation() ?: return
        val today = SimpleDate(GregorianCalendar())
        val azanTimes = Azan(azanLocation, Method.KARACHI_SHAF).getPrayerTimes(today)

        mListener!!.onSetup(azanTimes)
        timerRunnable = myRunnable
        timerHandler.postDelayed(timerRunnable!!, 0)
    }

    fun stop() {
        if (timerRunnable != null) return
        timerHandler.removeCallbacks(timerRunnable!!)
    }

    fun setListener(listener: AzanListener): AzanManager {
        mListener = listener
        return this
    }

    fun removeListener(): AzanManager {
        mListener = null
        return this
    }
}