package com.merseyside.utils.delegate

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.merseyside.utils.ext.getSerialize
import com.merseyside.utils.ext.put
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class FragmentArgumentProperty<T, V>(
    val helper: FragmentArgumentHelper,
    val key: (KProperty<*>) -> String = KProperty<*>::name,
    var requireExistence: Boolean = helper.requireExistence
) : ReadWriteProperty<T, V> {

    override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
        helper.put(key(property), value)
    }

    fun require(isRequired: Boolean) {
        requireExistence = isRequired
    }

    inline fun <R> requireExistence(
        key: String,
        block: (key: String, args: Bundle) -> R
    ): R {
        return if (helper.args == null || !helper.contains(key))
            throw IllegalArgumentException("Non nullable value with key $key required!")

        else block(key, helper.requireArgs)
    }
}

class FragmentArgumentHelper(
    private val fragment: Fragment,
    val requireExistence: Boolean = false
) {

    val args: Bundle?
        get() = fragment.arguments

    val requireArgs: Bundle
        get() = fragment.requireArguments()

    fun <T> put(
        key: String,
        value: T
    ) {
        getOrCreateArguments().put(key, value)
    }

    fun contains(key: String): Boolean {
        return fragment.arguments?.containsKey(key) ?: false
    }

    private fun getOrCreateArguments(): Bundle {
        return if (fragment.arguments == null) {
            if (requireExistence) throw IllegalArgumentException("Args are null!")
            else Bundle().also { fragment.arguments = it }
        } else fragment.requireArguments()
    }
}

fun FragmentArgumentHelper.string(
    defaultValue: String,
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, String> =
    object : FragmentArgumentProperty<Any, String>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): String {
            return args?.getString(key(property)) ?: defaultValue
        }
    }

fun FragmentArgumentHelper.stringOrNull(
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, String?> =
    object : FragmentArgumentProperty<Any, String?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): String? {
            return args?.getString(key(property))
        }
    }

fun FragmentArgumentHelper.string(
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, String> =
    object : FragmentArgumentProperty<Any, String>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): String {
            return requireExistence(key(property)) { key, args ->
                args.getString(key)!!
            }
        }
    }

fun FragmentArgumentHelper.bool(
    defaultValue: Boolean,
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Boolean> =
    object : FragmentArgumentProperty<Any, Boolean>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return args?.getBoolean(key(property)) ?: defaultValue
        }
    }

fun FragmentArgumentHelper.int(
    defaultValue: Int,
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Int> =
    object : FragmentArgumentProperty<Any, Int>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return args?.getInt(key(property)) ?: defaultValue
        }
    }

fun FragmentArgumentHelper.intOrNull(
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Int?> =
    object : FragmentArgumentProperty<Any, Int?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int? {
            return args?.getInt(key(property))
        }
    }

fun FragmentArgumentHelper.int(
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Int> =
    object : FragmentArgumentProperty<Any, Int>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return requireExistence(key(property)) { key, args ->
                args.getInt(key)
            }
        }
    }

fun FragmentArgumentHelper.float(
    defaultValue: Float,
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Float> =
    object : FragmentArgumentProperty<Any, Float>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return args?.getFloat(key(property)) ?: defaultValue
        }
    }

fun FragmentArgumentHelper.floatOrNull(
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Float?> =
    object : FragmentArgumentProperty<Any, Float?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float? {
            return args?.getFloat(key(property))
        }
    }

fun FragmentArgumentHelper.float(
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Float> =
    object : FragmentArgumentProperty<Any, Float>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return requireExistence(key(property)) { key, args ->
                args.getFloat(key)
            }
        }
    }

fun FragmentArgumentHelper.double(
    defaultValue: Double,
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Double> =
    object : FragmentArgumentProperty<Any, Double>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Double {
            return args?.getDouble(key(property)) ?: defaultValue
        }
    }

fun FragmentArgumentHelper.doubleOrNull(
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Double?> =
    object : FragmentArgumentProperty<Any, Double?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Double? {
            return args?.getDouble(key(property))
        }
    }

fun FragmentArgumentHelper.double(
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Double?> =
    object : FragmentArgumentProperty<Any, Double?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Double? {
            return requireExistence(key(property)) { key, args ->
                args.getDouble(key)
            }
        }
    }

fun FragmentArgumentHelper.long(
    defaultValue: Long,
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Long> =
    object : FragmentArgumentProperty<Any, Long>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return args?.getLong(key(property)) ?: defaultValue
        }
    }

fun FragmentArgumentHelper.longOrNull(
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Long?> =
    object : FragmentArgumentProperty<Any, Long?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Long? {
            return args?.getLong(key(property))
        }
    }

fun FragmentArgumentHelper.long(
    key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, Long> =
    object : FragmentArgumentProperty<Any, Long>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return requireExistence(key(property)) { key, args ->
                args.getLong(key)
            }
        }
    }

inline fun <reified T> FragmentArgumentHelper.deserializable(
    noinline key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, T> = object : FragmentArgumentProperty<Any, T>(
    this,
    key,
    requireExistence = true
) {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return requireExistence(key(property)) { key, args ->
            args.getSerialize(key)!!
        }
    }
}

inline fun <reified T> FragmentArgumentHelper.deserializableOrNull(
    noinline key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, T?> = object : FragmentArgumentProperty<Any, T?>(this, key) {
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return args?.getSerialize(key(property))
    }
}

inline fun <reified T> FragmentArgumentHelper.deserializable(
    defaultValue: T,
    noinline key: (KProperty<*>) -> String = KProperty<*>::name
): FragmentArgumentProperty<Any, T> = object : FragmentArgumentProperty<Any, T>(this, key) {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return requireArgs.getSerialize(key(property)) ?: defaultValue
    }
}