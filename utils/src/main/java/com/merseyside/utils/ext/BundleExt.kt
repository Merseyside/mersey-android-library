package com.merseyside.utils.ext

import android.os.Bundle
import android.os.Parcelable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun <T> Bundle.put(key: String, value: T) {
    when (value) {
        is Boolean -> putBoolean(key, value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Short -> putShort(key, value)
        is Long -> putLong(key, value)
        is Byte -> putByte(key, value)
        is ByteArray -> putByteArray(key, value)
        is Char -> putChar(key, value)
        is CharArray -> putCharArray(key, value)
        is CharSequence -> putCharSequence(key, value)
        is Float -> putFloat(key, value)
        is Bundle -> putBundle(key, value)
        is Parcelable -> putParcelable(key, value)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}

@OptIn(ExperimentalContracts::class)
fun Bundle?.isNotNullAndEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullAndEmpty != null)
    }

    return this != null && !this.isEmpty
}