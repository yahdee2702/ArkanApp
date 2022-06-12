package com.yahdi.arkanapp.utils

import com.azan.Azan
import com.azan.astrologicalCalc.SimpleDate
import com.yahdi.arkanapp.R
import java.util.*

class TimeState(private val prayer: Prayer) {
    val state get() = getStateFrom(Calendar.getInstance())
    enum class State {
        DAY,
        NOON,
        NIGHT,
        MIDNIGHT,
    }

    companion object {
        fun getImageIdFromState(state: State): Int {
            return when (state) {
                State.DAY -> R.drawable.img_time_state_1
                State.NOON -> R.drawable.img_time_state_2
                State.NIGHT -> R.drawable.img_time_state_3
                State.MIDNIGHT -> R.drawable.img_time_state_4
            }
        }
    }

    fun getStateFrom(date: Calendar): State {
        val currentTime = date.time.time

        return if (prayer.maghrib.time + (35 * 60000) < currentTime)
            State.MIDNIGHT
        else if (prayer.assr.time + (30 * 60000) < currentTime)
            State.NIGHT
        else if (prayer.shuruq.time + (40 * 60000) < currentTime)
            State.NOON
        else if (prayer.shuruq.time - (32 * 60000) < currentTime)
            State.DAY
        else
            State.MIDNIGHT
    }

    fun getImageId(): Int {
        return getImageIdFromState(state)
    }
}