package com.merseyside.kmpMerseyLib.utils.time

enum class TimeZone { SYSTEM, GMT }

fun getCurrentTimeMillis(timeZone: TimeZone = TimeZone.GMT): Long {
    return getCurrentTimeMillis(timeZone.name)
}

expect fun getCurrentTimeMillis(timeZone: String): Long

/**
 * If set return type to Millis
 */
fun getCurrentTimeUnit(timeZone: TimeZone = TimeZone.GMT): TimeUnit {
    return getCurrentTimeUnit(timeZone.name)
}

fun getCurrentTimeUnit(timeZone: String): TimeUnit {
    return Millis(getCurrentTimeMillis(timeZone))
}

fun getHoursMinutes(timestamp: Long): String {
    return getFormattedDate(timestamp, "hh:mm")
}

fun getHoursMinutes(timestamp: TimeUnit): String {
    return getHoursMinutes(timestamp.toMillisLong())
}

fun getDate(timestamp: Long): String {
    return getFormattedDate(timestamp, "dd.MM.YYYY")
}

fun getDate(timestamp: TimeUnit): String {
    return getDate(timestamp.toMillisLong())
}

fun getDateWithTime(timestamp: Long): String {
    return getFormattedDate(timestamp, "dd-MM-YYYY hh:mm")
}

fun getDateWithTime(timestamp: TimeUnit): String {
    return getDateWithTime(timestamp.toMillisLong())
}

fun getFormattedDate(timestamp: TimeUnit, pattern: String): String {
    return getFormattedDate(timestamp.toMillisLong(), pattern)
}

expect fun getFormattedDate(timestamp: Long, pattern: String): String

