package com.yahdi.arkanapp.utils

import android.os.Handler
import android.os.Looper
import android.util.Log

class LooperListener {
    private var key = this::class.java.name
    private var _delay: Long = 1000L // 1 Seconds

    private var mRunnable: Runnable? = null
    private var tempListener: ((LooperListener) -> Unit)? = null
    private var handler = Handler(Looper.getMainLooper())

    fun setListener(listener: (LooperListener) -> Unit): LooperListener {
        tempListener = listener
        return this
    }

    fun setDelay(value: Long): LooperListener {
        _delay = value
        return this
    }

    fun start(startupDelayed: Boolean? = false) {
        if (tempListener == null) {return}
        handler.postDelayed(object: Runnable {
            override fun run() {
                tempListener?.invoke(this@LooperListener)
                mRunnable = this
                handler.postDelayed(mRunnable!!, _delay)
            }
        }, if (startupDelayed == null || !startupDelayed) 0 else _delay)
    }

    fun stop() {
        if (tempListener == null) { return }
        if (mRunnable == null) return
        handler.removeCallbacks(mRunnable!!)
    }

    fun remove() {
        if (tempListener == null) {
            Log.e(key, "Cannot remove when listener is already null")
            return
        }

        stop()
        tempListener = null
        mRunnable = null
    }
}