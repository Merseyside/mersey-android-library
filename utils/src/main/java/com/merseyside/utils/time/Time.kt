package com.merseyside.utils.time

import android.annotation.SuppressLint
import android.content.Context
import com.merseyside.utils.LocaleManager
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.*

val GMT = TimeZone.getTimeZone("GMT")
val SYSTEM = TimeZone.getTimeZone("GMT0")

fun getCurrentTimeMillis(timeZone: TimeZone = GMT): Long {
    return when (timeZone) {
        SYSTEM -> {
            val offset: Int = TimeZone.getDefault().rawOffset +
                    TimeZone.getDefault().dstSavings
            return System.currentTimeMillis() + offset
        }

        else -> {
            Calendar.getInstance(timeZone).timeInMillis
        }
    }
}

/**
 * If set return type to Millis
 */
fun getCurrentTimeUnit(timeZone: TimeZone = GMT): TimeUnit {
    return Millis(getCurrentTimeMillis(timeZone))
}

fun getHoursMinutes(timestamp: Long, context: Context? = null): String {
    return getFormattedDate(timestamp, "HH:mm", context)
}

fun getHoursMinutes(timestamp: TimeUnit, context: Context? = null): String {
    return getHoursMinutes(timestamp.toMillisLong(), context)
}

fun getDate(
    timestamp: Long,
    context: Context? = null
): String {
    return getFormattedDate(timestamp, "dd.MM.YYYY", context)
}

fun getDate(
    timestamp: TimeUnit,
    context: Context? = null
): String {
    return getDate(timestamp.toMillisLong(), context)
}

fun getDateWithTime(
    timestamp: Long,
    context: Context? = null
): String {
    return getFormattedDate(timestamp, "dd-MM-YYYY HH:mm", context)
}

fun getDateWithTime(
    timestamp: TimeUnit,
    context: Context? = null): String {
    return getDateWithTime(timestamp.toMillisLong(), context)
}

fun getFormattedDate(
    timestamp: TimeUnit,
    pattern: String,
    context: Context? = null
): String {
    return getFormattedDate(timestamp.toMillisLong(), pattern, context)
}

@SuppressLint("SimpleDateFormat")
fun getFormattedDate(
    timestamp: Long,
    pattern: String,
    context: Context? = null
): String {
    val locale = if (context != null) {
        LocaleManager(context).getCurrentLocale()
    } else {
        Locale.getDefault()
    }

    return try {

        val sdf =  SimpleDateFormat(pattern, locale)
        sdf.timeZone = GMT

        val netDate = Date(timestamp)

        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

suspend fun measureTime(block: suspend () -> Unit): TimeUnit {
    val startTime = getCurrentTimeUnit()
    block()
    return getCurrentTimeUnit() - startTime
}

