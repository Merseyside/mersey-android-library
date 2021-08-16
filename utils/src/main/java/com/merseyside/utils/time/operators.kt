package com.merseyside.utils.time

import com.merseyside.merseyLib.time.TimeUnit

operator fun <T : TimeUnit> T.plus(increment: Number): T {
    return this + newInstance(increment.toLong())
}

operator fun <T : TimeUnit> T.div(divider: Number): T {
    return this / newInstance(divider.toLong())
}

operator fun <T : TimeUnit> T.times(times: Number): T {
    return this * newInstance(times.toLong())
}

operator fun <T : TimeUnit> T.minus(unary: Number): T {
    return this - newInstance(unary.toLong())
}

operator fun <T : TimeUnit> T.plus(increment: TimeUnit): T {
    return newInstanceMillis(this.millis + increment.millis) as T
}

operator fun <T : TimeUnit> T.div(divider: TimeUnit): T {
    return newInstanceMillis(this.millis / divider.millis) as T
}

operator fun <T : TimeUnit> T.times(times: TimeUnit): T {
    return newInstanceMillis(this.millis * times.millis) as T
}

operator fun <T : TimeUnit> T.minus(unary: TimeUnit): T {
    return newInstanceMillis(this.millis - unary.millis) as T
}

operator fun <T : TimeUnit> T.inc(): T {
    return this + newInstance(1)
}

operator fun <T : TimeUnit> T.dec(): T {
    return this - newInstance(1)
}

fun <T : TimeUnit> T.isEqual(other: T): Boolean {
    return this.millis == other.millis
}

operator fun <T : TimeUnit> T.compareTo(value: Number): Int {
    return this.millis.compareTo(newInstanceMillis(value.toLong()).millis)
}

fun <T : TimeUnit> T.isNotEqual(other: T) = !isEqual(other)

fun <T : TimeUnit> T.round() = newInstance(value) as T

fun <T : TimeUnit> T.isRound() = newInstance(value).millis == millis