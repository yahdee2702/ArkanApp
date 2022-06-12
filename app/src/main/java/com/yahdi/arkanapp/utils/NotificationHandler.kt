package com.yahdi.arkanapp.utils

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.media.RingtoneManager
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.utils.Utils.formatBasedOnSystemFormat
import java.text.SimpleDateFormat
import java.util.*

class NotificationHandler(): BroadcastReceiver() {
    companion object {
        const val LOCATION = "location"
        const val AZAN_CODE = 102010
        const val AZAN_NOTIF = 20191
    }

    private fun showAzanNotification(context: Context, location: Location) {
        val prayer = Prayer(location, Calendar.getInstance())
        val currentPrayer = prayer.getCurrentPrayer()
        val nextPrayer = prayer.getNextPrayer()
        val currentPrayerString = context.getString(Prayer.getPrayerStringId(currentPrayer))
        val nextPrayerString = context.getString(Prayer.getPrayerStringId(nextPrayer))
        val nextPrayerTime = prayer.getNextPrayerTime()

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val vibrationPattern = longArrayOf(1000,1000,1000,1000,1000)

        val channelId = "azanChannel_1"
        val channelName = "AzanChannel"
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_logo2)
            .setContentTitle("It's time for $currentPrayerString!")
            .setContentText("Next prayer is $nextPrayerString at ${nextPrayerTime.formatBasedOnSystemFormat(context)}")
            .setSound(ringtone)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(vibrationPattern)
            .setChannelId(channelId)

        notificationManagerCompat.createNotificationChannel(channel)
        notificationManagerCompat.notify(AZAN_NOTIF, notification.build())
    }

    override fun onReceive(context: Context, intent: Intent) {
        val location = intent.getParcelableExtra<Location>(LOCATION) as Location

        showAzanNotification(context, location)
        setAzanAlarm(context, location)
    }

    fun setAzanAlarm(context: Context, location: Location) {
        val today = Calendar.getInstance()
        val prayer = Prayer(location, today)
        val nextPrayer = prayer.getNextPrayerTime()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationHandler::class.java)
        intent.putExtra(LOCATION, location)
        val timeLeft = nextPrayer.time - today.time.time

        val pendingIntent = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                PendingIntent.getBroadcast(context, AZAN_CODE , intent, PendingIntent.FLAG_MUTABLE)
            }
            else -> {
                PendingIntent.getBroadcast(context, AZAN_CODE, intent, PendingIntent.FLAG_IMMUTABLE)
            }
        }

        Log.d("Azan Alarm", "Setting Azan alarm for ${prayer.getNextPrayer().name} in %s".format(DateUtils.formatElapsedTime(timeLeft/1000)))
        alarmManager.cancel(pendingIntent)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextPrayer.time, pendingIntent)
    }
}