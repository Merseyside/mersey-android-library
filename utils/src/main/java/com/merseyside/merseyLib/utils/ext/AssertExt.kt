package com.merseyside.merseyLib.utils.ext

fun Int.isZero(): Boolean {
    return this == 0
}

fun Double.isZero(): Boolean {
    return this == 0.0
}

fun Float.isZero(): Boolean {
    return this == 0f
}

fun Short.isZero(): Boolean {
    return this.toInt() == 0
}

fun Int.isNotZero(): Boolean {
    return this != 0
}

fun Double.isNotZero(): Boolean {
    return this != 0.0
}

fun Float.isNotZero(): Boolean {
    return this != 0f
}

fun Short.isNotZero(): Boolean {
    return this.toInt() != 0
}