package com.merseyside.utils.ext

import com.merseyside.merseyLib.time.units.Millis
import com.merseyside.merseyLib.time.units.TimeUnit
import com.merseyside.merseyLib.time.units.plus

fun List<TimeUnit>.sum(): TimeUnit {
    var sum = Millis()
    forEach { sum += it }

    return sum
}
