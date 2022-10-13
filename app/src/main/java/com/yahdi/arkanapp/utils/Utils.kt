package com.yahdi.arkanapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.yahdi.arkanapp.data.response.AyahResponse
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateFormat as DateFormat2

object Utils {
    fun getDefaultGson(): GsonBuilder = GsonBuilder()
        .enableComplexMapKeySerialization()
        .serializeNulls()
        .setDateFormat(DateFormat.LONG)
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .setPrettyPrinting()
        .setVersion(1.0)

    fun hasBasmallah(ayahData: AyahResponse): Boolean {
        return ayahData.id != 1 && ayahData.id != 1336 && ayahData.idInSurah == 1
    }

    fun removeBasmallah(ayahData: AyahResponse) :String {
         return if (hasBasmallah(ayahData)) ayahData.content.substring(38) else ayahData.content
    }

    fun getGMTDifference(calendar: Calendar): Double {
        val localTime: String = SimpleDateFormat("Z", Locale.getDefault()).format(calendar.time)
        val method = localTime.toCharArray()[0]
        val numbers = localTime.substring(1).toDoubleOrNull() ?: 0.0
        val gmt = numbers / 100.0

        return if (method == '+') gmt else -gmt
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

    fun Date.formatBasedOnSystemFormat(context: Context): String {
        return if (DateFormat2.is24HourFormat(context)) {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            formatter.format(this)
        } else {
            val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
            formatter.format(this)
        }
    }

    fun Calendar.toGregorianCalendar(): GregorianCalendar{
        val calendar = GregorianCalendar()
        calendar.timeInMillis = this.timeInMillis
        return calendar
    }

    fun canAccessLocation(mContext: Context): Boolean = ActivityCompat.checkSelfPermission(
            mContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            mContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
}