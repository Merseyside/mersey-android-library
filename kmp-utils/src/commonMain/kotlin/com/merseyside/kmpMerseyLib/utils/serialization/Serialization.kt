package com.merseyside.kmpMerseyLib.utils.serialization

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

@kotlinx.serialization.ImplicitReflectionSerializer
inline fun <reified T : Any> T.serialize(): String {
    return json.stringify(this)
}

@kotlinx.serialization.ImplicitReflectionSerializer
inline fun <reified T : Any> String.deserialize(): T {
    return json.parse(this)
}

fun <T : Any> T.serialize(serializationStrategy: SerializationStrategy<T>): String {
    return json.stringify(serializationStrategy, this)
}

fun <T> String.deserialize(deserializationStrategy: DeserializationStrategy<T>): T {
    return json.parse(deserializationStrategy, this)
}

@kotlinx.serialization.ImplicitReflectionSerializer
inline fun <reified T : Any> Any.deserialize(): T {
    return this.toString().deserialize()
}

fun <T> Any.deserialize(deserializationStrategy: DeserializationStrategy<T>): T {
    return this.toString().deserialize(deserializationStrategy)
}


