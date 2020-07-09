package com.merseyside.kmpMerseyLib.utils.time

import java.text.SimpleDateFormat
import java.util.*

actual fun getCurrentTimeMillis(timeZone: String): Long {
    return when (timeZone) {
        TimeZone.SYSTEM.name -> {
            val offset: Int = java.util.TimeZone.getDefault().rawOffset +
                    java.util.TimeZone.getDefault().dstSavings
            return System.currentTimeMillis() + offset
        }

        else -> {
            Calendar.getInstance(java.util.TimeZone.getTimeZone(timeZone)).timeInMillis
        }
    }
}

actual fun getFormattedDate(timestamp: Long, pattern: String): String {
    return try {

        val sdf = SimpleDateFormat(pattern, Locale.US)

        val netDate = Date(timestamp)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}