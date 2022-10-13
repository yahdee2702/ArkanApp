package com.yahdi.arkanapp.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.utils.Prayer
import com.yahdi.arkanapp.utils.Utils.formatBasedOnSystemFormat
import java.util.*

class AzanReceiver: BroadcastReceiver() {
    companion object {
        const val LOCATION_DATA = "location_dta"
        const val IS_FINISHED_STATUS_DATA = "ihatemakingthesestringsbutok"
        const val AZAN_CODE = 102010
        const val AZAN_NOTIF = 20191
    }
    private fun showAzanNotification(context: Context, location: Location) {
        context.apply {
            val prayer = Prayer(location, Calendar.getInstance())
            val currentPrayer = prayer.getCurrentPrayer()
            val nextPrayer = prayer.getNextPrayer()
            val currentPrayerString = getString(Prayer.getPrayerStringId(currentPrayer))
            val nextPrayerString = getString(Prayer.getPrayerStringId(nextPrayer))
            val nextPrayerTime = prayer.getNextPrayerTime()

            val notificationManagerCompat = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val vibrationPattern = longArrayOf(1000,1000,1000,1000,1000)

            val channelId = "azanChannel_1"
            val channelName = "AzanChannel"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            val notification = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("It's time for $currentPrayerString!")
                .setContentText("Next prayer is $nextPrayerString at ${nextPrayerTime.formatBasedOnSystemFormat(this)}")
                .setSound(ringtone)
                .setColor(ContextCompat.getColor(this, android.R.color.transparent))
                .setVibrate(vibrationPattern)
                .setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
            notificationManagerCompat.notify(AZAN_NOTIF, notification.build())
        }
    }

    private fun setAzanAlarm(context: Context, location: Location) {
        context.apply {
            val today = Calendar.getInstance()
            val prayer = Prayer(location, today)
            val nextPrayer = prayer.getNextPrayerTime()
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AzanReceiver::class.java)
            intent.putExtra(IS_FINISHED_STATUS_DATA, true)
            intent.putExtra(LOCATION_DATA, location)
            val timeLeft = nextPrayer.time - today.time.time

            val pendingIntent = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    PendingIntent.getBroadcast(this, AZAN_CODE, intent, PendingIntent.FLAG_MUTABLE)
                }
                else -> {
                    PendingIntent.getBroadcast(this, AZAN_CODE, intent, PendingIntent.FLAG_IMMUTABLE)
                }
            }

            Log.d("Azan Alarm", "Setting Azan alarm for ${prayer.getNextPrayer().name} in %s".format(
                DateUtils.formatElapsedTime(timeLeft/1000)))
            alarmManager.cancel(pendingIntent)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextPrayer.time + 650, pendingIntent)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent?.let {
            val location = intent.getParcelableExtra<Location>(LOCATION_DATA) ?: return
            val status = intent.getBooleanExtra(IS_FINISHED_STATUS_DATA, false)

            if (status) {
                showAzanNotification(context, location)
            }
            setAzanAlarm(context, location)
        }
    }
}