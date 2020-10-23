package com.merseyside.archy.data.cache.serializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Json Serializer/Deserializer.
 */
@Singleton
class Serializer @Inject constructor() {

    private val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    fun serialize(obj: Any, clazz: Class<*>): String {
        return gson.toJson(obj, clazz)
    }

    fun <T> deserialize(string: String, clazz: Class<T>): T {
        return gson.fromJson(string, clazz)
    }

    fun <T> serialize(obj: Any): String {
        return gson.toJson(obj, object: TypeToken<T>(){}.type)
    }

    fun <T> deserialize(string: String): T {
        return gson.fromJson(string, object: TypeToken<T>(){}.type)
    }

    fun <T> deserialize(string: String, type: TypeToken<T>): T {
        return gson.fromJson(string, type.type)
    }

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

    inline fun <reified T> Gson.toJsonAlt(json: T) = this.toJson(json, object: TypeToken<T>() {}.type)
}
