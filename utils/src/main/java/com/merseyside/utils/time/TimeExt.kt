package com.merseyside.utils.time

import com.merseyside.utils.Logger
import com.merseyside.utils.ext.toTimeUnit
import java.text.ParseException

fun <T: Number> T.toTimeUnit(): TimeUnit {
    return Millis(this.toLong())
}

fun <T: Number> T.toMillis(): Millis {
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
fun <T: CharSequence> T.toTimeUnit(): TimeUnit {
    return this.toString().toLong().toTimeUnit()
}

@Throws(NumberFormatException::class)
fun <T: CharSequence> T.toMillis(): Millis {
    return this.toString().toLong().toMillis()
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

fun TimeUnit.toDouble(): Double {
    return value.toDouble()
}

fun TimeUnit.toFloat(): Float {
    return value.toFloat()
}

fun TimeUnit.toInt(): Int {
    return value.toInt()
}

fun TimeUnit.toFormattedDate(pattern: String): FormattedDate {
    return getFormattedDate(this, pattern)
}

fun TimeUnit.toHoursMinutes(): FormattedDate {
    return getHoursMinutes(this)
}

fun FormattedDate.toTimeUnit(vararg pattern: String): TimeUnit {
    val patternsList: List<String> = if (pattern.isNotEmpty()) pattern.toList() else TimeConfiguration.formatPatterns

    patternsList.forEach {
        try {
            value.toTimeUnit(it).let { timestamp -> return timestamp.toMillis() }
        } catch (e: ParseException) {
            Logger.logErr(tag = "TimeUnit", msg = "$it is wrong pattern to format time")
        }
    }

    throw Exception("Can not format response time with suggested patterns!")
}