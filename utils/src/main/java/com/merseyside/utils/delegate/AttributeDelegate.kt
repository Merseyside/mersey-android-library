package com.merseyside.utils.delegate

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import com.merseyside.utils.Logger
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.attributes.AttributeHelper.Companion.NO_VALUE
import com.merseyside.utils.attributes.Namespace
import com.merseyside.utils.ext.log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * AttributeSet recycled in View's constructor. That's why we can use it only in init block.
 * And that's why first using of delegated property not inside of init block without explicit setting of resName is impossible.
 */

fun AttributeHelper.bool(
    defaultValue: Boolean = false,
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Boolean> = object: ReadWriteProperty<Any, Boolean> {
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
            getBool(resName, defaultValue, namespace).also { value = it }
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
): ReadWriteProperty<Any, String> = object: ReadWriteProperty<Any, String> {
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
            getString(resName, namespace).also { value = it }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Logger.logErr("Are you sure you call it in init block or passed resName explicitly")
            defaultValue
        }
    }
}

fun AttributeHelper.stringOrNull(
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, String?> = object: ReadWriteProperty<Any, String?> {
    var value: String? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return value ?: getValue(property.name)?.also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        this.value = value
    }

    private fun getValue(resName: String): String? {
        return try {
            getString(resName, namespace).also { value = it }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Logger.logErr("Are you sure you call it in init block or passed resName explicitly")
            null
        }
    }
}

fun AttributeHelper.int(
    defaultValue: Int = 0,
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Int> = object: ReadWriteProperty<Any, Int> {
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
            getInt(resName, defaultValue, namespace).also { value = it }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Logger.logErr("Are you sure you call it in init block or passed resName explicitly")
            defaultValue
        }
    }
}

fun AttributeHelper.float(
    defaultValue: Float = 0F,
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Float> = object: ReadWriteProperty<Any, Float> {
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
            getFloat(resName, defaultValue, namespace).also { value = it }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Logger.logErr("Are you sure you call it in init block or passed resName explicitly")
            defaultValue
        }
    }
}

fun AttributeHelper.resource(
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Int> = object: ReadWriteProperty<Any, Int> {
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
        return getResourceId(resName, namespace)?.also { value = it } ?: NO_VALUE
    }
}

fun AttributeHelper.dimension(
    defaultValue: Float,
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Float> = object: ReadWriteProperty<Any, Float> {
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
        return getDimension(resName, defaultValue, namespace).also { value = it }
    }
}

fun AttributeHelper.dimensionOrNull(
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Float?> = object: ReadWriteProperty<Any, Float?> {
    var value: Float? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Float? {
        return value ?: getValue(property.name)?.also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float?) {
        this.value = value
    }

    private fun getValue(resName: String): Float? {
        return getDimensionOrNull(resName, namespace)?.also { value = it }
    }
}

fun AttributeHelper.color(
    @ColorInt defaultValue: Int = 0,
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Int> = object: ReadWriteProperty<Any, Int> {
    var value: Int? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    @ColorInt
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return value ?: getValue(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        this.value = value
    }

    private fun getValue(resName: String): Int {
        return getColor(resName, defaultValue, namespace).also { value = it }
    }
}

fun AttributeHelper.colorOrNull(
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Int?> = object: ReadWriteProperty<Any, Int?> {
    var value: Int? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    @ColorInt
    override fun getValue(thisRef: Any, property: KProperty<*>): Int? {
        return value ?: getValue(property.name)?.also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int?) {
        this.value = value
    }

    private fun getValue(resName: String): Int? {
        return getColorOrNull(resName, namespace).also { value = it }
    }
}

fun AttributeHelper.drawable(
    defaultValue: Drawable,
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Drawable> = object: ReadWriteProperty<Any, Drawable> {
    var value: Drawable? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Drawable {
        return value ?: getValue(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Drawable) {
        this.value = value
    }

    private fun getValue(resName: String): Drawable {
        return getDrawable(resName, defaultValue, namespace).also { value = it }
    }
}

fun AttributeHelper.drawableOrNull(
    namespace: Namespace = defNamespace,
    resName: String? = null
): ReadWriteProperty<Any, Drawable?> = object: ReadWriteProperty<Any, Drawable?> {
    var value: Drawable? = null

    init {
        if (resName != null) value = getValue(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Drawable? {
        return value ?: getValue(property.name)?.also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Drawable?) {
        this.value = value
    }

    private fun getValue(resName: String): Drawable? {
        return getDrawableOrNull(resName, namespace).also { value = it }
    }
}