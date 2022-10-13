package com.yahdi.arkanapp.ui

import android.app.Application
import android.content.Intent
import android.util.Log
import com.yahdi.arkanapp.services.ArkanAzanService
import com.yahdi.arkanapp.utils.GPSTracker
class ArkanApplication: Application() {

    private var _gpsTracker: GPSTracker? = null
    val gpsTracker get() = _gpsTracker!!

    override fun onCreate() {
        super.onCreate()
        _gpsTracker = GPSTracker(this)
        if (!ArkanAzanService.isAvailable(this)) {
            applicationContext.startForegroundService(Intent(this, ArkanAzanService::class.java))
        }
    }
}

