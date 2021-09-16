package com.merseyside.utils.serialization

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

inline fun <reified T : Any> T.serialize(
    json: Json = JsonConfigurator.json
): String {
    return json.encodeToString(this)
}

inline fun <reified T : Any> T.serializeLong(
    json: Json = JsonConfigurator.json
): Long {
    return json.encodeToString(this).toLong()
}

inline fun <reified T : Any> T.serializeInt(
    json: Json = JsonConfigurator.json
): Int {
    return json.encodeToString(this).toInt()
}

inline fun <reified T : Any> T.serializeFloat(
    json: Json = JsonConfigurator.json
): Float {
    return json.encodeToString(this).toFloat()
}

@OptIn(InternalSerializationApi::class)
fun <T : Any> T.serialize(
    type: KClass<T>,
    json: Json = JsonConfigurator.json): String {
    return serialize(type.serializer(), json)
}

fun <T : Any> T.serialize(
    serializationStrategy: SerializationStrategy<T>,
    json: Json = JsonConfigurator.json
): String {
    return json.encodeToString(serializationStrategy, this)
}

fun <T> String.deserialize(
    deserializationStrategy: DeserializationStrategy<T>,
    json: Json = JsonConfigurator.json
): T {
    return json.decodeFromString(deserializationStrategy, this)
}

inline fun <reified T : Any> String.deserialize(
    json: Json = JsonConfigurator.json
): T {
    return json.decodeFromString(this)
}

@OptIn(InternalSerializationApi::class)
fun <T : Any> String.deserialize(
    type: KClass<T>,
    json: Json = JsonConfigurator.json
): T {
    return deserialize(type.serializer(), json)
}

inline fun <reified T : Any> Any.deserialize(json: Json = JsonConfigurator.json): T {
    return this.toString().deserialize(json)
}

fun <T> Any.deserialize(
    deserializationStrategy: DeserializationStrategy<T>,
    json: Json = JsonConfigurator.json
): T {
    return this.toString().deserialize(deserializationStrategy, json)
}


