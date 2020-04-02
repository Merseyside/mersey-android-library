package com.merseyside.merseyLib.utils.time

import com.merseyside.merseyLib.utils.ext.log

fun <T: Number> T.toMillis(): Millis {
    return Millis(this.toLong()).log()
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