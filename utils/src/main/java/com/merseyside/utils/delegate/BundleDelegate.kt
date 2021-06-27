package com.merseyside.utils.delegate

import android.os.Bundle
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun Bundle.string(
    defaultValue: String? = null,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadOnlyProperty<Any, String?> =
    ReadOnlyProperty { _, property -> getString(key(property)) ?: defaultValue }

fun Bundle.float(
    defaultValue: Float? = null,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadOnlyProperty<Any, Float?> =
    ReadOnlyProperty { _, property ->
        if (containsKey(key(property))) getFloat(key(property))
        else defaultValue
    }

fun Bundle.int(
    defaultValue: Int? = null,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadOnlyProperty<Any, Int?> =
    ReadOnlyProperty { _, property ->
        if (containsKey(key(property))) getInt(key(property))
        else defaultValue
    }

fun Bundle.double(
    defaultValue: Double? = null,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadOnlyProperty<Any, Double?> =
    ReadOnlyProperty { _, property ->
        if (containsKey(key(property))) getDouble(key(property))
        else defaultValue
    }

fun Bundle.bool(
    defaultValue: Boolean? = null,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadOnlyProperty<Any, Boolean?> =
    ReadOnlyProperty { _, property ->
        if (containsKey(key(property))) getBoolean(key(property))
        else defaultValue
    }

fun <T> Bundle.deserializable(
    defaultValue: T? = null,
    key: (KProperty<*>) -> String = KProperty<*>::name,
    deserialize: (String) -> T
): ReadOnlyProperty<Any, T?> =
    ReadOnlyProperty { _, property -> getString(key(property))?.let(deserialize) ?: defaultValue }