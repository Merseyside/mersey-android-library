package com.merseyside.utils.ext

import android.os.Bundle
import android.os.Parcelable
import com.merseyside.merseyLib.kotlin.serialization.JsonConfigurator
import com.merseyside.merseyLib.kotlin.serialization.deserialize
import com.merseyside.merseyLib.kotlin.serialization.serialize
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
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

inline fun <reified T : Any> Bundle.putSerialize(
    key: String,
    value: T,
    json: Json = JsonConfigurator.json
) {
    this.putString(key, value.serialize(json))
}

inline fun <reified T : Any> Bundle.getSerialize(
    key: String,
    json: Json = JsonConfigurator.json
): T? {
    return this.getString(key)?.deserialize(json)
}

inline fun <reified T : Any> Bundle.putSerialize(
    key: String,
    value: T,
    serializationStrategy: SerializationStrategy<T>,
    json: Json = JsonConfigurator.json
) {
    this.putString(key, value.serialize(serializationStrategy, json))
}

inline fun <reified T : Any> Bundle.getSerialize(
    key: String,
    deserializationStrategy: DeserializationStrategy<T>,
    json: Json = JsonConfigurator.json
): T? {
    return this.getString(key)?.deserialize(deserializationStrategy, json)
}