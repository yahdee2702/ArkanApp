package com.yahdi.arkanapp.utils

import android.location.Location
import com.azan.Azan
import com.azan.AzanTimes
import com.azan.Method
import com.azan.PrayerTime
import com.yahdi.arkanapp.R
import com.azan.Time as AzanTime
import com.yahdi.arkanapp.utils.Utils.toGregorianCalendar
import java.util.*
import com.azan.astrologicalCalc.Location as AzanLocation

class Prayer(azanLocation: AzanLocation, private var time: GregorianCalendar) {
    private val defaultAzan: Azan
    private val prayerTimes: AzanTimes

    val fajr get() = prayerTimes.fajr().toDate()
    val shuruq get() = prayerTimes.shuruq().toDate()
    val dhuhur get() = prayerTimes.thuhr().toDate()
    val assr get() = prayerTimes.assr().toDate()
    val maghrib get() = prayerTimes.maghrib().toDate()
    val ishaa get() = prayerTimes.ishaa().toDate()
    val nextFajr get() = defaultAzan.getNextDayFajr(time).toNextDate()

    companion object {
        fun getAzanLocation(location: Location): AzanLocation {
            val calendar = GregorianCalendar()
            val gmtDifference = Utils.getGMTDifference(calendar)

            return AzanLocation(location.latitude, location.longitude, gmtDifference, 0)
        }

        fun getPrayerStringId(id: PrayerID): Int {
            return when (id) {
                PrayerID.ISHAA -> R.string.txt_ishaa_time
                PrayerID.MAGHRIB -> R.string.txt_maghrib_time
                PrayerID.ASSR -> R.string.txt_assr_time
                PrayerID.DHUHUR -> R.string.txt_dhuhr_time
                PrayerID.SUNRISE -> R.string.txt_sunrise_time
                PrayerID.FAJR -> R.string.txt_fajr_time
                PrayerID.NEXT_FAJR -> R.string.txt_fajr_time
            }
        }

        fun AzanTime.toDate(): Date {
            val calendar = android.icu.util.Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, this.hour)
            calendar.set(Calendar.MINUTE, this.minute)
            calendar.set(Calendar.SECOND, this.second)
            return calendar.time
        }

        fun AzanTime.toNextDate(): Date {
            val calendar = android.icu.util.Calendar.getInstance()
            calendar.add(Calendar.DATE, 1)
            calendar.set(Calendar.HOUR_OF_DAY, this.hour)
            calendar.set(Calendar.MINUTE, this.minute)
            calendar.set(Calendar.SECOND, this.second)
            return calendar.time
        }
    }

    enum class PrayerID {
        FAJR,
        SUNRISE,
        DHUHUR,
        ASSR,
        MAGHRIB,
        ISHAA,
        NEXT_FAJR,
    }

    init {
        defaultAzan = Azan(azanLocation, Method.FIXED_ISHAA)
        prayerTimes = defaultAzan.getPrayerTimes(time.toGregorianCalendar())
    }

    constructor(location: Location, time: Calendar): this(getAzanLocation(location), time.toGregorianCalendar())

    fun getNextPrayerTime(): Date {
        return getPrayerTimeFromId(getNextPrayer())
    }

    fun getCurrentPrayerTime(): Date {
        return getPrayerTimeFromId(getCurrentPrayer())
    }

    fun getNextPrayer(): PrayerID {
        val currentTime = time.time.time

        return if (fajr.time >= currentTime)
            PrayerID.FAJR
        else if (shuruq.time >= currentTime)
            PrayerID.SUNRISE
        else if (dhuhur.time >= currentTime)
            PrayerID.DHUHUR
        else if (assr.time >= currentTime)
            PrayerID.ASSR
        else if (maghrib.time >= currentTime)
            PrayerID.MAGHRIB
        else if (ishaa.time >= currentTime)
            PrayerID.ISHAA
        else
            PrayerID.NEXT_FAJR
    }

    fun getPrayerTimeFromId(id: PrayerID): Date {
        return when (id) {
            PrayerID.ISHAA -> ishaa
                PrayerID.MAGHRIB -> maghrib
            PrayerID.ASSR -> assr
            PrayerID.DHUHUR -> dhuhur
            PrayerID.SUNRISE -> shuruq
            PrayerID.FAJR -> fajr
            PrayerID.NEXT_FAJR -> nextFajr
        }
    }

    fun getCurrentPrayer(): PrayerID {
        val currentTime = time.time.time

        return if (nextFajr.time < currentTime)
            PrayerID.NEXT_FAJR
        else if (ishaa.time < currentTime)
            PrayerID.ISHAA
        else if (maghrib.time < currentTime)
            PrayerID.MAGHRIB
        else if (assr.time < currentTime)
            PrayerID.ASSR
        else if (dhuhur.time < currentTime)
            PrayerID.DHUHUR
        else if (shuruq.time < currentTime)
            PrayerID.SUNRISE
        else
            PrayerID.FAJR
    }
}