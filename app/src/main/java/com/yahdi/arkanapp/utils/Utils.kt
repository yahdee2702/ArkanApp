package com.yahdi.arkanapp.utils

import android.content.Context
import com.azan.AzanTimes
import android.text.format.DateFormat as DateFormat2
import com.azan.Time
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.response.AyahResponse
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getDefaultGson(): GsonBuilder = GsonBuilder()
        .enableComplexMapKeySerialization()
        .serializeNulls()
        .setDateFormat(DateFormat.LONG)
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .setPrettyPrinting()
        .setVersion(1.0)

    fun removeBasmallah(ayahData: AyahResponse) :String {
        return if (ayahData.id != 1 && ayahData.id != 1336 && ayahData.idInSurah == 1) ayahData.content.substring(36) else ayahData.content
    }

    fun getGMTDifference(calendar: Calendar): Double {
        val localTime: String = SimpleDateFormat("Z", Locale.getDefault()).format(calendar.time)
        val methods = localTime.toCharArray()
        val gmt = methods[2].toString().toDoubleOrNull() ?: 0.0
        return if (methods[0] == '+') gmt else -gmt
    }

    fun changePrayerTimeToDate(time: Time): Date {
        val calendar = android.icu.util.Calendar.getInstance()
        calendar.set(android.icu.util.Calendar.HOUR_OF_DAY, time.hour)
        calendar.set(android.icu.util.Calendar.MINUTE, time.minute)
        calendar.set(android.icu.util.Calendar.SECOND, time.second)
        return calendar.time
    }

//    fun formatPrayerTime(context: Context, time: Time): String {
//        return formatBasedOnSystemFormat(context, changePrayerTimeToDate(time))
//    }

    fun formatBasedOnSystemFormat(context: Context, date: Date): String {
        return if (DateFormat2.is24HourFormat(context)) {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            formatter.format(date)
        } else {
            val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
            formatter.format(date)
        }
    }

    fun getNextPrayer(context:Context, current: Date, prayerTimes: AzanTimes): String {
        val currentTime = current.time

        return if (changePrayerTimeToDate(prayerTimes.fajr()).time >= currentTime)
            context.getString(R.string.txt_fajr_time)
        else if (changePrayerTimeToDate(prayerTimes.shuruq()).time >= currentTime)
            context.getString(R.string.txt_sunrise_time)
        else if (changePrayerTimeToDate(prayerTimes.thuhr()).time >= currentTime)
            context.getString(R.string.txt_dhuhr_time)
        else if (changePrayerTimeToDate(prayerTimes.assr()).time >= currentTime)
            context.getString(R.string.txt_asr_time)
        else if (changePrayerTimeToDate(prayerTimes.maghrib()).time >= currentTime)
            context.getString(R.string.txt_maghrib_time)
        else
            context.getString(R.string.txt_ishaa_time)
    }

    fun getCurrentPrayer(context: Context, current: Date,  prayerTimes: AzanTimes):String {
        val currentTime = current.time

        return if (changePrayerTimeToDate(prayerTimes.ishaa()).time < currentTime)
            context.getString(R.string.txt_ishaa_time)
        else if (changePrayerTimeToDate(prayerTimes.maghrib()).time < currentTime)
            context.getString(R.string.txt_maghrib_time)
        else if (changePrayerTimeToDate(prayerTimes.assr()).time < currentTime)
            context.getString(R.string.txt_asr_time)
        else if (changePrayerTimeToDate(prayerTimes.thuhr()).time < currentTime)
            context.getString(R.string.txt_dhuhr_time)
        else if (changePrayerTimeToDate(prayerTimes.shuruq()).time < currentTime)
            context.getString(R.string.txt_sunrise_time)
        else
            context.getString(R.string.txt_fajr_time)
    }

    fun getImageTimeStateFromPrayer(current: Date, prayerTimes: AzanTimes): Int {
        val currentTime = current.time
        return if (changePrayerTimeToDate(prayerTimes.maghrib()).time + (35 * 60000) < currentTime)
            R.drawable.img_time_state_4
        else if (changePrayerTimeToDate(prayerTimes.assr()).time + (30 * 60000) < currentTime)
            R.drawable.img_time_state_3
        else if (changePrayerTimeToDate(prayerTimes.shuruq()).time + (75 * 60000) < currentTime)
            R.drawable.img_time_state_2
        else if (changePrayerTimeToDate(prayerTimes.shuruq()).time - (32 * 60000) < currentTime)
            R.drawable.img_time_state_1
        else
            R.drawable.img_time_state_4
    }
}