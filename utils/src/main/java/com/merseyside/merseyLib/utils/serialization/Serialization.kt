package com.merseyside.merseyLib.utils.serialization

import android.os.Bundle
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

@UnstableDefault
val json: Json by lazy {
    Json {
        isLenient = true
        allowStructuredMapKeys = true
        ignoreUnknownKeys = true
    }
}

@OptIn(ImplicitReflectionSerializer::class)
inline fun <reified T : Any> T.serialize(): String {
    return json.stringify(this)
}

@OptIn(ImplicitReflectionSerializer::class)
inline fun <reified T : Any> String.deserialize(): T {
    return json.parse(this)
}

fun <T : Any> T.serialize(serializationStrategy: SerializationStrategy<T>): String {
    return json.stringify(serializationStrategy, this)
}

fun <T> String.deserialize(deserializationStrategy: DeserializationStrategy<T>): T {
    return json.parse(deserializationStrategy, this)
}

inline fun <reified T : Any> Any.deserialize(): T {
    return this.toString().deserialize()
}

fun <T> Any.deserialize(deserializationStrategy: DeserializationStrategy<T>): T {
    return this.toString().deserialize(deserializationStrategy)
}

inline fun <reified T : Any> Bundle.putSerialize(key: String, value: T) {
    this.putString(key, value.serialize())
}

inline fun <reified T : Any> Bundle.getSerialize(key: String): T? {
    return this.getString(key)?.deserialize()
}

inline fun <reified T : Any> Bundle.putSerialize(key: String, value: T, serializationStrategy: SerializationStrategy<T>) {
    this.putString(key, value.serialize(serializationStrategy))
}

inline fun <reified T : Any> Bundle.getSerialize(key: String, deserializationStrategy: DeserializationStrategy<T>): T? {
    return this.getString(key)?.deserialize(deserializationStrategy)
}


