package com.merseyside.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.merseyside.merseyLib.kotlin.singletons.SingletonHolder
import com.merseyside.merseyLib.time.Millis
import com.merseyside.merseyLib.time.TimeUnit

@SuppressLint("MissingPermission")
class Vibration private constructor(context: Context) {

    private var vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun vibrate(timeUnit: TimeUnit = Millis(100)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    timeUnit.millis,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrate(createPattern(timeUnit))
        }
    }

    fun vibrate(pattern: Array<Long> = defaultPattern, repeat: Int = -1) {
        vibrator.vibrate(pattern.toLongArray(), repeat)
    }

    private fun createPattern(vararg timeUnit: TimeUnit): Array<Long> {
        return mutableListOf<Long>(0).apply {
            timeUnit.forEach { add(it.millis) }
        }.toTypedArray()
    }

    companion object : SingletonHolder<Vibration, Context>(::Vibration) {
        private val defaultPattern = arrayOf(0, 100L)
    }
}