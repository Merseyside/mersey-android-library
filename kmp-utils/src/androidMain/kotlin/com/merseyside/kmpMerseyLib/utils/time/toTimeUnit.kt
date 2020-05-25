package com.merseyside.kmpMerseyLib.utils.time

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.TimeZone

actual fun String.toTimeUnit(dateFormat: String): TimeUnit {
    return try {
        val date = SimpleDateFormat(dateFormat, Locale.US).apply {
            isLenient = false
            timeZone = TimeZone.getTimeZone("GMT")
        }.parse(this)

        if (date != null) {
            Millis(date.time)
        } else {
            throw KotlinNullPointerException("Date can not be parse within following format")
        }
    } catch (e: ParseException) {
        throw e
    }
}