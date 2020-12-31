package com.merseyside.utils.ext

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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

@OptIn(ExperimentalContracts::class)
fun Float?.isNotNullAndZero(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndZero != null)
    }

    return this != null && this.isNotZero()
}


@OptIn(ExperimentalContracts::class)
fun Int?.isNotNullAndZero(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndZero != null)
    }

    return this != null && this.isNotZero()
}

@OptIn(ExperimentalContracts::class)
fun Double?.isNotNullAndZero(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndZero != null)
    }

    return this != null && this.isNotZero()
}

@OptIn(ExperimentalContracts::class)
fun Short?.isNotNullAndZero(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndZero != null)
    }

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