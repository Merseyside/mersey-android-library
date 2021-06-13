package com.merseyside.utils.delegate

import android.content.Context
import android.util.AttributeSet
import com.merseyside.utils.Logger
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AttributeHelper(
    private val context: Context,
    private val attrSet: AttributeSet,
    val defNamespace: Namespace = Namespace.DEFAULT
) {

    fun getBool(
        defValue: Boolean,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = attrSet.getAttributeBooleanValue(nameSpace.namespace, resName, defValue)

    fun getString(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = attrSet.getAttributeValue(nameSpace.namespace, resName)

    fun getInt(
        defValue: Int,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = attrSet.getAttributeIntValue(nameSpace.namespace, resName, defValue)

    fun getFloat(
        defValue: Float,
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = attrSet.getAttributeFloatValue(nameSpace.namespace, resName, defValue)

    fun getDimension(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ): Float {
        val id = getResourceId(nameSpace, resName)
        return context.resources.getDimension(id)
    }

    fun getDimensionPixelSize(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ): Int {
        val id = getResourceId(nameSpace, resName)
        return context.resources.getDimensionPixelSize(id)
    }

    fun getResourceId(
        nameSpace: Namespace = Namespace.DEFAULT,
        resName: String
    ) = attrSet.getAttributeResourceValue(nameSpace.namespace, resName, 0)
}

/**
 * AttributeSet recycled in View's constructor. That's why we can use it only in init block.
 * And that's why first using of delegated property not inside of init block without explicit setting of resName is impossible.
 */

fun AttributeHelper.boolean(
    defaultValue: Boolean = false,
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Boolean> = object: ReadOnlyProperty<Any, Boolean>,
    ReadWriteProperty<Any, Boolean> {
    var value: Boolean? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return value ?: getValue(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        this.value = value
    }

    private fun getValue(resName: String): Boolean {
        return try {
            getBool(defaultValue, namespace, resName).also { value = it }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Logger.logErr("Are you sure you call it in init block or passed resName explicitly")
            defaultValue
        }
    }
}

fun AttributeHelper.string(
    defaultValue: String = "",
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, String> = object: ReadOnlyProperty<Any, String>,
    ReadWriteProperty<Any, String> {
    var value: String? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return value ?: getValue(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        this.value = value
    }

    private fun getValue(resName: String): String {
        return try {
            getString(namespace, resName).also { value = it }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Logger.logErr("Are you sure you call it in init block or passed resName explicitly")
            defaultValue
        }
    }
}

fun AttributeHelper.int(
    defaultValue: Int = 0,
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Int> = object: ReadOnlyProperty<Any, Int>,
    ReadWriteProperty<Any, Int> {
    var value: Int? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return value ?: getValue(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        this.value = value
    }

    private fun getValue(resName: String): Int {
        return try {
            getInt(defaultValue, namespace, resName).also { value = it }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Logger.logErr("Are you sure you call it in init block or passed resName explicitly")
            defaultValue
        }
    }
}

fun AttributeHelper.int(
    defaultValue: Float = 0F,
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Float> = object: ReadOnlyProperty<Any, Float>,
    ReadWriteProperty<Any, Float> {
    var value: Float? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Float {
        return value ?: getValue(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
        this.value = value
    }

    private fun getValue(resName: String): Float {
        return try {
            getFloat(defaultValue, namespace, resName).also { value = it }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Logger.logErr("Are you sure you call it in init block or passed resName explicitly")
            defaultValue
        }
    }
}

fun AttributeHelper.resource(
    defaultValue: Int = 0,
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Int> = object: ReadOnlyProperty<Any, Int>,
    ReadWriteProperty<Any, Int> {
    var value: Int? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return value ?: getValue(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        this.value = value
    }

    private fun getValue(resName: String): Int {
        return try {
            getResourceId(namespace, resName).also { value = it }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Logger.logErr("Are you sure you call it in init block or passed resName explicitly")
            defaultValue
        }
    }
}


fun AttributeHelper.dimension(
    defaultValue: Int = 0,
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Int> = object: ReadOnlyProperty<Any, Int>,
    ReadWriteProperty<Any, Int> {
    var value: Int? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return value ?: getValue(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        this.value = value
    }

    private fun getValue(resName: String): Int {
        return try {
            getDimensionPixelSize(namespace, resName).also { value = it }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Logger.logErr("Are you sure you call it in init block or passed resName explicitly")
            defaultValue
        }
    }
}


enum class Namespace(
    val namespace: String
) {
    ANDROID("xmlns:android=\"http://schemas.android.com/apk/res/android"),
    DEFAULT("http://schemas.android.com/apk/res-auto")
}