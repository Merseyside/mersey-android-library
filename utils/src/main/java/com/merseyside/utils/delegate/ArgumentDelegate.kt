@file:OptIn(InternalSerializationApi::class)
package com.merseyside.utils.delegate

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.NavArgsLazy
import androidx.navigation.fragment.navArgs
import com.merseyside.merseyLib.kotlin.serialization.serialize
import com.merseyside.utils.ext.getSerialize
import com.merseyside.utils.ext.put
import com.merseyside.utils.reflection.callMethodByName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.serializer
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class ArgumentHelper(internal val requireExistence: Boolean) {
    abstract val arguments: Bundle?

    val requireArgs: Bundle
        get() = arguments ?: throw NullPointerException()

    fun <T> put(key: String, value: T) {
        arguments?.put(key, value)
            ?: throw NullPointerException("Can not put value because arguments are null!")
    }

    inline fun <reified T> get(key: String, defaultValue: T): T {
        return (arguments?.get(key) as? T) ?: defaultValue
    }

    inline fun <reified T> getOrNull(key: String): T? {
        return arguments?.get(key) as? T
    }

    fun contains(key: String): Boolean {
        return arguments?.containsKey(key) ?: false
    }
}

abstract class ArgumentProperty<T, V>(
    val helper: ArgumentHelper,
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
        return if (helper.arguments == null || !helper.contains(key))
            throw IllegalArgumentException("Non nullable value with key $key required!")
        else block(key, helper.requireArgs)
    }

    inline fun <R> ifContains(
        key: String,
        block: (key: String, args: Bundle) -> R
    ): R? {
        return if (helper.arguments != null && helper.contains(key)) block(key, helper.requireArgs)
        else null
    }
}

class ActivityArgumentHelper(
    internal val activity: Activity,
    requireExistence: Boolean = false
) : ArgumentHelper(requireExistence) {

    override val arguments: Bundle?
        get() = activity.intent.extras

}

fun Activity.argumentHelper(requireExistence: Boolean = false): ReadOnlyProperty<Any, ActivityArgumentHelper> {
    return ReadOnlyProperty { _, _ ->
        ActivityArgumentHelper(this, requireExistence)
    }
}

fun Fragment.argumentHelper(requireExistence: Boolean = false): ReadOnlyProperty<Any, FragmentArgumentHelper> {
    return ReadOnlyProperty<Any, FragmentArgumentHelper> { _, _ ->
        FragmentArgumentHelper(this, requireExistence)
    }
}

class FragmentArgumentHelper(
    internal val fragment: Fragment,
    requireExistence: Boolean = false
) : ArgumentHelper(requireExistence) {

    init {
        if (fragment.arguments == null) {
            fragment.arguments = Bundle()
        }
    }

    override val arguments: Bundle?
        get() = fragment.arguments
}

class NavArgsHelper<Args : NavArgs>(
    private val lazyNavArgs: NavArgsLazy<Args>,
    requireExistence: Boolean = false
) : ArgumentHelper(requireExistence) {

    lateinit var args: Args
    var cached: Bundle? = null

    override val arguments: Bundle?
        get() {
            if (cached == null) {
                args = lazyNavArgs.value
                cached = args.callMethodByName("toBundle") as Bundle?
            }

            return cached
        }
}

inline fun <reified Args : NavArgs> Fragment.NavArgsHelper(
    requireExistence: Boolean = false
): NavArgsHelper<Args> {
    return NavArgsHelper(navArgs(), requireExistence)
}

fun ArgumentHelper.string(
    defaultValue: String,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, String> =
    object : ArgumentProperty<Any, String>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): String {
            return arguments?.getString(key(property)) ?: defaultValue
        }
    }

fun ArgumentHelper.stringOrNull(
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, String?> =
    object : ArgumentProperty<Any, String?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): String? {
            return ifContains(key(property)) { key, args -> args.getString(key) }
        }
    }

fun ArgumentHelper.string(
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, String> =
    object : ArgumentProperty<Any, String>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): String {
            return requireExistence(key(property)) { key, args ->
                args.getString(key)!!
            }
        }
    }

fun ArgumentHelper.bool(
    defaultValue: Boolean,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Boolean> =
    object : ArgumentProperty<Any, Boolean>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return arguments?.getBoolean(key(property)) ?: defaultValue
        }
    }

fun ArgumentHelper.int(
    defaultValue: Int,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Int> =
    object : ArgumentProperty<Any, Int>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return arguments?.getInt(key(property)) ?: defaultValue
        }
    }

fun ArgumentHelper.intOrNull(
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Int?> =
    object : ArgumentProperty<Any, Int?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int? {
            return ifContains(key(property)) { key, args -> args.getInt(key) }
        }
    }

fun ArgumentHelper.int(
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Int> =
    object : ArgumentProperty<Any, Int>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return requireExistence(key(property)) { key, args ->
                args.getInt(key)
            }
        }
    }

fun ArgumentHelper.float(
    defaultValue: Float,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Float> =
    object : ArgumentProperty<Any, Float>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return arguments?.getFloat(key(property)) ?: defaultValue
        }
    }

fun ArgumentHelper.floatOrNull(
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Float?> =
    object : ArgumentProperty<Any, Float?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float? {
            return ifContains(key(property)) { key, args -> args.getFloat(key) }
        }
    }

fun ArgumentHelper.float(
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Float> =
    object : ArgumentProperty<Any, Float>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return requireExistence(key(property)) { key, args ->
                args.getFloat(key)
            }
        }
    }

fun ArgumentHelper.double(
    defaultValue: Double,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Double> =
    object : ArgumentProperty<Any, Double>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Double {
            return arguments?.getDouble(key(property)) ?: defaultValue
        }
    }

fun ArgumentHelper.doubleOrNull(
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Double?> =
    object : ArgumentProperty<Any, Double?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Double? {
            return ifContains(key(property)) { key, args -> args.getDouble(key) }
        }
    }

fun ArgumentHelper.double(
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Double?> =
    object : ArgumentProperty<Any, Double?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Double? {
            return requireExistence(key(property)) { key, args ->
                args.getDouble(key)
            }
        }
    }

fun ArgumentHelper.long(
    defaultValue: Long,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Long> =
    object : ArgumentProperty<Any, Long>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return arguments?.getLong(key(property), defaultValue) ?: defaultValue
        }
    }

fun ArgumentHelper.longOrNull(
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Long?> =
    object : ArgumentProperty<Any, Long?>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Long? {
            return ifContains(key(property)) { key, args -> args.getLong(key) }
        }
    }

fun ArgumentHelper.long(
    key: (KProperty<*>) -> String = KProperty<*>::name
): ArgumentProperty<Any, Long> =
    object : ArgumentProperty<Any, Long>(this, key) {
        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return requireExistence(key(property)) { key, args ->
                args.getLong(key)
            }
        }
    }

abstract class SerializableArgumentProperty<T, V>(
    helper: ArgumentHelper,
    key: (KProperty<*>) -> String = KProperty<*>::name,
    requireExistence: Boolean = helper.requireExistence,
    private val serializer: KSerializer<V>
): ArgumentProperty<T, V>(helper, key, requireExistence) {

    override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
        val serializedValue = value?.serialize(serializer)
        if (serializedValue != null) {
            helper.put(key(property), serializedValue)
        }
    }
}

@OptIn(InternalSerializationApi::class)
inline fun <reified T : Any> ArgumentHelper.serializable(
    noinline key: (KProperty<*>) -> String = KProperty<*>::name
): SerializableArgumentProperty<Any, T> = object : SerializableArgumentProperty<Any, T>(
    helper = this, key = key, requireExistence = true, serializer = T::class.serializer()
) {

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return requireExistence(key(property)) { key, args ->
            args.getSerialize(key)!!
        }
    }
}

inline fun <reified T : Any> ArgumentHelper.serializableOrNull(
    noinline key: (KProperty<*>) -> String = KProperty<*>::name
): SerializableArgumentProperty<Any, T?> = object : SerializableArgumentProperty<Any, T?>(
    helper = this, key = key, requireExistence = true, serializer = T::class.serializer().nullable
) {
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return ifContains(key(property)) { key, args -> args.getSerialize(key) }
    }
}

inline fun <reified T : Any> ArgumentHelper.serializable(
    defaultValue: T,
    noinline key: (KProperty<*>) -> String = KProperty<*>::name
): SerializableArgumentProperty<Any, T> = object : SerializableArgumentProperty<Any, T>(
    helper = this, key = key, requireExistence = true, serializer = T::class.serializer()
) {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return requireArgs.getSerialize(key(property)) ?: defaultValue
    }
}

inline fun <reified T : Any> ArgumentHelper.serializable(
    serializer: KSerializer<T>,
    noinline key: (KProperty<*>) -> String = KProperty<*>::name
): SerializableArgumentProperty<Any, T> = object : SerializableArgumentProperty<Any, T>(
    helper = this, key = key, requireExistence = true, serializer = T::class.serializer()
) {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return requireExistence(key(property)) { key, args ->
            requireNotNull(args.getSerialize(key, serializer))
        }
    }
}

inline fun <reified T : Any> ArgumentHelper.serializableOrNull(
    serializer: KSerializer<T>,
    noinline key: (KProperty<*>) -> String = KProperty<*>::name
): SerializableArgumentProperty<Any, T?> = object : SerializableArgumentProperty<Any, T?>(
    helper = this, key = key, requireExistence = true, serializer = T::class.serializer().nullable
) {
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return ifContains(key(property)) { key, args ->
            args.getSerialize(key, serializer)
        }
    }
}

inline fun <reified T : Any> ArgumentHelper.serializable(
    defaultValue: T,
    serializer: KSerializer<T>,
    noinline key: (KProperty<*>) -> String = KProperty<*>::name
): SerializableArgumentProperty<Any, T> = object : SerializableArgumentProperty<Any, T>(
    helper = this, key = key, requireExistence = true, serializer = T::class.serializer()
) {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return requireArgs.getSerialize(key(property), serializer) ?: defaultValue
    }
}