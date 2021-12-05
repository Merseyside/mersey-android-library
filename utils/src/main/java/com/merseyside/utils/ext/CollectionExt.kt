package com.merseyside.utils.ext

import com.merseyside.merseyLib.time.Millis
import com.merseyside.merseyLib.time.TimeUnit
import com.merseyside.merseyLib.time.plus

fun List<TimeUnit>.sum(): TimeUnit {
    var sum = Millis()
    forEach { sum += it }

    return sum
}
