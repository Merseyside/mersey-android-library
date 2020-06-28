package com.merseyside.utils.time

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.TimeZone

fun <T: Number> T.toTimeUnit(): Millis {
    return Millis(this.toLong())
}

fun <T: Number> T.toSeconds(): Seconds {
    return Seconds(this.toLong())
}

fun <T: Number> T.toMinutes(): Minutes {
    return Minutes(this.toLong())
}

fun <T: Number> T.toHours(): Hours {
    return Hours(this.toLong())
}

fun <T: Number> T.toDays(): Days {
    return Days(this.toLong())
}

@Throws(NumberFormatException::class)
fun <T: CharSequence> T.toTimeUnit(): Millis {
    return this.toString().toLong().toTimeUnit()
}

@Throws(NumberFormatException::class)
fun <T: CharSequence> T.toSeconds(): Seconds {
    return this.toString().toLong().toSeconds()
}

@Throws(NumberFormatException::class)
fun <T: CharSequence> T.toMinutes(): Minutes {
    return this.toString().toLong().toMinutes()
}

@Throws(NumberFormatException::class)
fun <T: CharSequence> T.toHours(): Hours {
    return this.toString().toLong().toHours()
}

@Throws(NumberFormatException::class)
fun <T: CharSequence> T.toDays(): Days {
    return this.toString().toLong().toDays()
}

@Throws(ParseException::class, KotlinNullPointerException::class)
fun String.toTimeUnit(dateFormat: String, locale: Locale = Locale.US): TimeUnit {
    return try {
        val date = SimpleDateFormat(dateFormat, locale).apply {
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