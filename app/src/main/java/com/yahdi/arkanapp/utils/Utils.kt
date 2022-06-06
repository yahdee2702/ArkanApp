package com.yahdi.arkanapp.utils

import com.azan.Time
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
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

    fun formatPrayerTime(time: Time): String {
        return time.hour.toString() + ":" + time.minute.toString()
    }
}