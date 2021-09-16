package com.merseyside.utils.serialization

import com.merseyside.merseyLib.time.Time
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus

object JsonConfigurator {

    private var _json: Json? = null

    val json: Json
        get() {
            return _json ?: getDefaultJson().also { _json = it }
        }

    fun configure(block: JsonBuilder.() -> Unit) {
        _json = Json {
            block()
        }
    }

    fun addSerializersModule(module: SerializersModule) {
        _json = Json(from = json) {
            serializersModule += module
        }
    }

    fun clear() {
        _json = null
    }

    private fun getDefaultJson() = Json {
        isLenient = true
        allowStructuredMapKeys = true
        ignoreUnknownKeys = true

        serializersModule = Time.serializersModule
    }

}