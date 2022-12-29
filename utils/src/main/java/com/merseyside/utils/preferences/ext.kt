package com.merseyside.utils.preferences

import com.merseyside.merseyLib.time.units.TimeUnit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun PreferenceManager.stringNullable(
    defaultValue: String? = null,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, String?> =
    object : ReadWriteProperty<Any, String?> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getNullableString(key(property), defaultValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: String?
        ) = put(key(property), value)
    }

fun PreferenceManager.string(
    defaultValue: String = "",
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, String> =
    object : ReadWriteProperty<Any, String> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getString(key(property), defaultValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: String
        ) = put(key(property), value)
    }

fun PreferenceManager.float(
    defaultValue: Float = 0F,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, Float> =
    object : ReadWriteProperty<Any, Float> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getFloat(key(property), defaultValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: Float
        ) = put(key(property), value)
    }

fun PreferenceManager.int(
    defaultValue: Int = 0,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, Int> =
    object : ReadWriteProperty<Any, Int> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getInt(key(property), defaultValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: Int
        ) = put(key(property), value)
    }

fun PreferenceManager.bool(
    defaultValue: Boolean = false,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, Boolean> =
    object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getBool(key(property), defaultValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: Boolean
        ) = put(key(property), value)
    }

fun PreferenceManager.timeUnit(
    defaultValue: TimeUnit = TimeUnit.empty(),
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, TimeUnit> =
    object : ReadWriteProperty<Any, TimeUnit> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getTimeUnit(key(property), defaultValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: TimeUnit
        ) = put(key(property), value)
    }

