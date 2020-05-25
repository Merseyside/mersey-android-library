package com.merseyside.kmpMerseyLib.utils

import com.merseyside.kmpMerseyLib.utils.serialization.serialize
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer


class Bundle(val map: MutableMap<String, String> = HashMap()) {

    fun contains(key: String): Boolean {
        return map.contains(key)
    }

    fun put(key: String, value: String) {
        map[key] = value
    }

    fun put(key: String, value: Any) {
        map[key] = value.toString()
    }

    fun getString(key: String): String? {
        return map[key]
    }

    fun getBool(key: String): Boolean? {
        return map[key]?.let {
            if (it == "false" || it == "true") {
                it.toBoolean()
            } else {
                null
            }
        }
    }

    fun getInt(key: String): Int? {
        return map[key]?.toIntOrNull()
    }

    fun getLong(key: String): Long? {
        return map[key]?.toLongOrNull()
    }

    fun getFloat(key: String): Float? {
        return map[key]?.toFloatOrNull()
    }

    override fun toString(): String {
        return map.serialize(MapSerializer(String.serializer(), String.serializer()))
    }
}