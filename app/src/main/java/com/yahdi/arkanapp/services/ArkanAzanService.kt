package com.yahdi.arkanapp.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.text.format.DateUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.receiver.AzanReceiver
import com.yahdi.arkanapp.ui.MainActivity
import com.yahdi.arkanapp.utils.GPSTracker
import com.yahdi.arkanapp.utils.Prayer
import com.yahdi.arkanapp.utils.Utils.formatBasedOnSystemFormat
import java.util.*

class ArkanAzanService : Service() {
    companion object {
        const val FOREGROUND_ID = 1203

        fun isAvailable(context: Context): Boolean {
            val activityManager = context.getSystemService(ActivityManager::class.java)

            @Suppress("DEPRECATION")
            for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                if (ArkanAzanService::class.java.name.equals(service.service.className)) {
                    return true
                }
            }

            return false
        }
    }

    private var _gpsTracker: GPSTracker? = null
    private val gpsTracker get() = _gpsTracker!!

    private fun sendAzanSignal(location: Location) {
        val azanIntent = Intent(this, AzanReceiver::class.java).apply {
            putExtra(AzanReceiver.LOCATION_DATA, location)
            putExtra(AzanReceiver.IS_FINISHED_STATUS_DATA, false)
        }

        sendBroadcast(azanIntent)
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        _gpsTracker = GPSTracker(this)

        gpsTracker.setOnLocationChangedListener {
            sendAzanSignal(gpsTracker.location)
        }

        val mIntent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = "arkanlocsrvc"
        val channelName = "ArkanApp"

        val notificationChannel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

        getSystemService(NotificationManager::class.java).createNotificationChannel(
            notificationChannel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentIntent(pIntent)
            .setContentTitle("Arkan App")
            .setContentText("Prayer Time and Qur\'an")
            .setSmallIcon(R.drawable.ic_logo)
            .build()

        startForeground(FOREGROUND_ID, notification)
        sendAzanSignal(gpsTracker.location)
        return START_NOT_STICKY
    }


}