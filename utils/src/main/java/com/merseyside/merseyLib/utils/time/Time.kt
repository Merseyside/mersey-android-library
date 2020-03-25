package com.merseyside.merseyLib.utils.time

import android.content.Context
import com.merseyside.merseyLib.utils.LocaleManager
import java.text.SimpleDateFormat
import java.util.*

fun getSystemTimeMillis(): Long {
    return System.currentTimeMillis()
}

fun getTimestamp(): TimeUnit {
    return Millis(getSystemTimeMillis())
}

fun getHoursMinutes(timestamp: Long, context: Context? = null): String {
    return getFormattedDate(timestamp, "hh:mm", context)
}

fun getHoursMinutes(timestamp: TimeUnit, context: Context? = null): String {
    return getHoursMinutes(timestamp.toMillisLong(), context)
}

fun getDate(timestamp: Long, context: Context?): String {
    return getFormattedDate(timestamp, "dd-MM-YYYY", context)
}

fun getDate(timestamp: TimeUnit, context: Context?): String {
    return getDate(timestamp.toMillisLong(), context)
}

fun getDateWithTime(timestamp: Long, context: Context?): String {
    return getFormattedDate(timestamp, "dd-MM-YYYY hh:mm", context)
}

fun getDateWithTime(timestamp: TimeUnit, context: Context?): String {
    return getDateWithTime(timestamp.toMillisLong(), context)
}

fun getFormattedDate(timestamp: TimeUnit, pattern: String, context: Context? = null): String {
    return getFormattedDate(timestamp.toMillisLong(), pattern, context)
}

fun getFormattedDate(timestamp: Long, pattern: String, context: Context? = null): String {
    var locale: Locale? = null

    if (context != null) {
        locale = LocaleManager(context).getCurrentLocale()
    }

    return try {

        val sdf = if (locale != null) {
            SimpleDateFormat(pattern, locale)
        } else {
            SimpleDateFormat(pattern)
        }
        val netDate = Date(timestamp)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

