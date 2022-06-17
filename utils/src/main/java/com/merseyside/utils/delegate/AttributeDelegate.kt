package com.merseyside.utils.delegate

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.attributes.AttributeHelper.Companion.NO_VALUE
import com.merseyside.utils.attributes.AttributeHelper.Companion.NO_VALUE_FLOAT
import com.merseyside.utils.attributes.AttributeHelper.Companion.NO_VALUE_STRING
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * AttributeSet recycled in View's constructor. That's why we can use it only in init block.
 * And that's why first using of delegated property not inside of init block without explicit setting of resName is impossible.
 */

fun AttributeHelper.bool(
    defaultValue: Boolean = false,
    resName: String? = null
): ReadWriteProperty<Any, Boolean> = object : ReadWriteProperty<Any, Boolean> {
    var value: Boolean? = null

    init {
        if (resName != null) value = getBool(resName, defaultValue)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return value ?: getBool(property.name, defaultValue).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        this.value = value
    }
}

fun AttributeHelper.string(
    defaultValue: String = NO_VALUE_STRING,
    resName: String? = null
): ReadWriteProperty<Any, String> = object : ReadWriteProperty<Any, String> {
    var value: String? = null

    init {
        if (resName != null) value = getString(resName, defaultValue)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return value ?: getString(property.name, defaultValue).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        this.value = value
    }
}

fun AttributeHelper.stringOrNull(
    resName: String? = null
): ReadWriteProperty<Any, String?> = object : ReadWriteProperty<Any, String?> {
    var value: String? = null

    init {
        if (resName != null) value = getStringOrNull(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return value ?: getStringOrNull(property.name)?.also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        this.value = value
    }
}

fun AttributeHelper.int(
    defaultValue: Int = NO_VALUE,
    resName: String? = null
): ReadWriteProperty<Any, Int> = object : ReadWriteProperty<Any, Int> {
    var value: Int? = null

    init {
        if (resName != null) value = getInt(resName, defaultValue)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return value ?: getInt(property.name, defaultValue).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        this.value = value
    }
}

fun AttributeHelper.intOrNull(
    resName: String? = null
): ReadWriteProperty<Any, Int?> = object : ReadWriteProperty<Any, Int?> {
    var value: Int? = null

    init {
        if (resName != null) value = getIntOrNull(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Int? {
        return value ?: getIntOrNull(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int?) {
        this.value = value
    }
}

fun AttributeHelper.float(
    defaultValue: Float = NO_VALUE_FLOAT,
    resName: String? = null
): ReadWriteProperty<Any, Float> = object : ReadWriteProperty<Any, Float> {
    var value: Float? = null

    init {
        if (resName != null) value = getFloat(resName, defaultValue)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Float {
        return value ?: getFloat(property.name, defaultValue).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
        this.value = value
    }
}

fun AttributeHelper.floatOrNull(
    resName: String? = null
): ReadWriteProperty<Any, Float?> = object : ReadWriteProperty<Any, Float?> {
    var value: Float? = null

    init {
        if (resName != null) value = getFloatOrNull(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Float? {
        return value ?: getFloatOrNull(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float?) {
        this.value = value
    }
}

fun AttributeHelper.resource(
    defaultValue: Int = NO_VALUE,
    resName: String? = null
): ReadWriteProperty<Any, Int> = object : ReadWriteProperty<Any, Int> {
    var value: Int? = null

    init {
        if (resName != null) value = getResourceId(resName, defaultValue)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return value ?: getResourceId(property.name, defaultValue).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        this.value = value
    }
}

fun AttributeHelper.resourceOrNull(
    resName: String? = null
): ReadWriteProperty<Any, Int?> = object : ReadWriteProperty<Any, Int?> {
    var value: Int? = null

    init {
        if (resName != null) value = getResourceIdOrNull(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Int? {
        return value ?: getResourceIdOrNull(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int?) {
        this.value = value
    }
}

fun AttributeHelper.dimension(
    defaultValue: Float = NO_VALUE_FLOAT,
    resName: String? = null
): ReadWriteProperty<Any, Float> = object : ReadWriteProperty<Any, Float> {
    var value: Float? = null

    init {
        if (resName != null) value = getDimension(resName, defaultValue)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Float {
        return value ?: getDimension(property.name, defaultValue).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
        this.value = value
    }
}

fun AttributeHelper.dimensionOrNull(
    resName: String? = null
): ReadWriteProperty<Any, Float?> = object : ReadWriteProperty<Any, Float?> {
    var value: Float? = null

    init {
        if (resName != null) value = getDimensionOrNull(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Float? {
        return value ?: getDimensionOrNull(property.name)?.also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float?) {
        this.value = value
    }
}

fun AttributeHelper.dimensionPixelSize(
    defaultValue: Int = NO_VALUE,
    resName: String? = null
): ReadWriteProperty<Any, Int> = object : ReadWriteProperty<Any, Int> {
    var value: Int? = null

    init {
        if (resName != null) value = getDimensionPixelSize(resName, defaultValue)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return value ?: getDimensionPixelSize(property.name, defaultValue).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        this.value = value
    }
}

fun AttributeHelper.dimensionPixelSizeOrNull(
    resName: String? = null
): ReadWriteProperty<Any, Int?> = object : ReadWriteProperty<Any, Int?> {
    var value: Int? = null

    init {
        if (resName != null) value = getDimensionPixelSizeOrNull(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Int? {
        return value ?: getDimensionPixelSizeOrNull(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int?) {
        this.value = value
    }
}

fun AttributeHelper.color(
    @ColorInt defaultValue: Int = NO_VALUE,
    resName: String? = null
): ReadWriteProperty<Any, Int> = object : ReadWriteProperty<Any, Int> {
    var value: Int? = null

    init {
        if (resName != null) value = getColor(resName, defaultValue)
    }

    @ColorInt
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return value ?: getColor(property.name, defaultValue)
            .also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        this.value = value
    }
}

fun AttributeHelper.color(
    resName: String? = null
): ReadWriteProperty<Any, Int> = object : ReadWriteProperty<Any, Int> {
    var value: Int? = null

    init {
        if (resName != null) value = getColor(resName)
    }

    @ColorInt
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return value ?: getColor(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        this.value = value
    }
}

fun AttributeHelper.colorOrNull(
    resName: String? = null
): ReadWriteProperty<Any, Int?> = object : ReadWriteProperty<Any, Int?> {
    var value: Int? = null

    init {
        if (resName != null) value = getColorOrNull(resName)
    }

    @ColorInt
    override fun getValue(thisRef: Any, property: KProperty<*>): Int? {
        return value ?: getColorOrNull(property.name)?.also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int?) {
        this.value = value
    }
}

fun AttributeHelper.drawable(
    defaultValue: Drawable,
    resName: String? = null
): ReadWriteProperty<Any, Drawable> = object : ReadWriteProperty<Any, Drawable> {
    var value: Drawable? = null

    init {
        if (resName != null) value = getDrawable(resName, defaultValue)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Drawable {
        return value ?: getDrawable(property.name, defaultValue).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Drawable) {
        this.value = value
    }
}

fun AttributeHelper.drawableOrNull(
    resName: String? = null
): ReadWriteProperty<Any, Drawable?> = object : ReadWriteProperty<Any, Drawable?> {
    var value: Drawable? = null

    init {
        if (resName != null) value = getDrawableOrNull(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Drawable? {
        return value ?: getDrawableOrNull(property.name)?.also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Drawable?) {
        this.value = value
    }
}