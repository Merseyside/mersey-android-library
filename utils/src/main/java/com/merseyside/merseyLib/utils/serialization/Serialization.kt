package com.merseyside.merseyLib.utils.serialization

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

@OptIn(ImplicitReflectionSerializer::class, UnstableDefault::class)
inline fun <reified T : Any> T.serialize(): String {
    return json.stringify(this)
}

@OptIn(ImplicitReflectionSerializer::class, UnstableDefault::class)
inline fun <reified T : Any> String.deserialize(): T {
    return json.parse(this)
}

@OptIn(UnstableDefault::class)
fun <T : Any> T.serialize(serializationStrategy: SerializationStrategy<T>): String {
    return json.stringify(serializationStrategy, this)
}

@OptIn(UnstableDefault::class)
fun <T> String.deserialize(deserializationStrategy: DeserializationStrategy<T>): T {
    return json.parse(deserializationStrategy, this)
}

inline fun <reified T : Any> Any.deserialize(): T {
    return this.toString().deserialize()
}

fun <T> Any.deserialize(deserializationStrategy: DeserializationStrategy<T>): T {
    return this.toString().deserialize(deserializationStrategy)
}


