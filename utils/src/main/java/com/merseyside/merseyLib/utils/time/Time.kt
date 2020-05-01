package com.merseyside.merseyLib.utils.time

import android.annotation.SuppressLint
import android.content.Context
import com.merseyside.merseyLib.utils.LocaleManager
import java.text.SimpleDateFormat
import java.util.*

enum class TimeZone { SYSTEM, GMT }

fun getCurrentTimeMillis(timeZone: TimeZone = TimeZone.GMT): Long {
    return getCurrentTimeMillis(timeZone.name)
}

fun getCurrentTimeMillis(timeZone: String): Long {
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

/**
 * If set return type to Millis
 */
fun getCurrentTimeUnit(timeZone: TimeZone = TimeZone.GMT): TimeUnit {
    return getCurrentTimeUnit(timeZone.name)
}

fun getCurrentTimeUnit(timeZone: String): TimeUnit {
    return Millis(getCurrentTimeMillis(timeZone))
}

fun getHoursMinutes(timestamp: Long, context: Context? = null): String {
    return getFormattedDate(timestamp, "hh:mm", context)
}

fun getHoursMinutes(timestamp: TimeUnit, context: Context? = null): String {
    return getHoursMinutes(timestamp.toMillisLong(), context)
}

fun getDate(timestamp: Long, context: Context?): String {
    return getFormattedDate(timestamp, "dd.MM.YYYY", context)
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

@SuppressLint("SimpleDateFormat")
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

