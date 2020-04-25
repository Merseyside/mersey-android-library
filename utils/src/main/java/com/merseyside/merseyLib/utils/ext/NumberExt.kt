package com.merseyside.merseyLib.utils.ext

import androidx.annotation.NonNull

fun Float.trimTrailingZero(): String {
    return this.toString().trimTrailingZero()
}

fun Number.toBoolean(): Boolean {
    return this != 0
}

fun Float.isZero(): Boolean {
    return this == 0f
}

fun Long.isZero(): Boolean {
    return this == 0L
}

fun Int.isZero(): Boolean {
    return this == 0
}

fun Double.isZero(): Boolean {
    return this == 0.0
}

fun Short.isZero(): Boolean {
    return this.toInt() == 0
}

@NonNull
fun Float?.isNotNullAndZero(): Boolean {
    return this != null && this.isNotZero()
}


fun Int?.isNotNullAndZero(): Boolean {
    return this != null && this.isNotZero()
}

fun Double?.isNotNullAndZero(): Boolean {
    return this != null && this.isNotZero()
}

fun Short?.isNotNullAndZero(): Boolean {
    return this != null && this.toInt().isNotZero()
}

fun Number.isEven(): Boolean {
    return this.toInt() % 2 == 0
}

fun Number.isOdd() = !this.isEven()

fun Int.isNotZero() = !this.isZero()
fun Double.isNotZero() = !this.isZero()
fun Long.isNotZero() = !this.isZero()
fun Float.isNotZero() = !this.isZero()
fun Short.isNotZero() = !this.isZero()