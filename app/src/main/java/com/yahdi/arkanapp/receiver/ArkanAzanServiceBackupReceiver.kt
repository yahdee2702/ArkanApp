package com.yahdi.arkanapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yahdi.arkanapp.services.ArkanAzanService

class ArkanAzanServiceBackupReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        intent.action?.let {
            if (it == Intent.ACTION_BOOT_COMPLETED) {
                val mIntent = Intent(context, ArkanAzanService::class.java)

                context.startForegroundService(mIntent)
            }
        }
    }
}