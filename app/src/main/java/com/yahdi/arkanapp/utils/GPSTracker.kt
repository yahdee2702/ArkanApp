package com.yahdi.arkanapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.util.Log
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.lang.Exception
import java.util.*


class GPSTracker(val context: Context): LocationListener {
    companion object {
        private val TAG = GPSTracker::class.java.name
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10.0f
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()
        private const val GEOCODER_MAX_RESULTS = 1
    }

    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    private var isGPSTrackingEnabled = false
    private var alreadyRequested = false

    private var _location: Location? = null
    val location: Location get() {
        requestLocation()
        return _location as Location
    }

    private lateinit var locationManager: LocationManager

    private var _providerInfo: String? = null
    val providerInfo get() = _providerInfo as String

    init {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (isGPSEnabled) {
                isGPSTrackingEnabled = true
                Log.d(TAG, "Application use GPS Service")
                _providerInfo = LocationManager.GPS_PROVIDER
            } else if (isNetworkEnabled) {
                isGPSTrackingEnabled = true
                Log.d(TAG, "Application use Network State to get GPS coordinates")
                _providerInfo = LocationManager.NETWORK_PROVIDER
            }
        } catch (e: Exception) {
            Log.e(TAG, "Impossible to connect to LocationManager", e)
        }
    }

    private fun requestLocation() {
        if (alreadyRequested) return
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        locationManager.requestLocationUpdates(
            providerInfo,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            this@GPSTracker
        )
        _location = locationManager.getLastKnownLocation(providerInfo)
        alreadyRequested = true
    }

    fun stopUsingGPS() {
        locationManager.removeUpdates(this@GPSTracker)
    }

    private fun getGeocoderAddress(context: Context): List<Address>? {
        requestLocation()
        val geocoder = Geocoder(context, Locale.ENGLISH)
        try {
            return geocoder.getFromLocation(location.latitude, location.longitude, GEOCODER_MAX_RESULTS)
        } catch (e: IOException) {
            //e.printStackTrace();
            Log.e(TAG, "Impossible to connect to Geocoder", e)
        }
        return emptyList()
    }

    fun getPostalCode(context: Context): String? {
        val addresses: List<Address>? = getGeocoderAddress(context)
        return if (addresses != null && addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            address.postalCode
        } else {
            null
        }
    }

    fun getCountryName(context: Context): String? {
        val addresses: List<Address>? = getGeocoderAddress(context)
        return if (addresses != null && addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            address.countryName
        } else {
            null
        }
    }

    override fun onLocationChanged(p1: Location) {
        _location = p1
    }
}