package com.merseyside.kmpMerseyLib.utils.time

import java.text.SimpleDateFormat
import java.util.*

actual fun getCurrentTimeMillis(timeZone: String): Long {
    val value = when (timeZone) {
        TimeZone.SYSTEM.name -> {
            null
        }

        TimeZone.GMT.name -> {
            java.util.TimeZone.getTimeZone("GMT")
        }

        else -> {
            java.util.TimeZone.getTimeZone(timeZone)
        }
    }

    return if (value != null) {
        Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT")).timeInMillis
    } else {
        val offset: Int = java.util.TimeZone.getDefault().rawOffset +
                java.util.TimeZone.getDefault().dstSavings
        return (System.currentTimeMillis() + offset)
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