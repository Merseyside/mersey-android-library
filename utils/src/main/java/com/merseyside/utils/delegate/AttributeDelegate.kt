@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package com.merseyside.utils.delegate

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.attributes.AttributeHelper.Companion.NO_VALUE
import com.merseyside.utils.attributes.AttributeHelper.Companion.NO_VALUE_FLOAT
import com.merseyside.utils.attributes.AttributeHelper.Companion.NO_VALUE_STRING
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AttributeHelperDelegate<T>(
    private val resName: String?,
    private val onChange: (oldValue: T?, newValue: T) -> Unit
) : ReadWriteProperty<Any, T> {

    var value: T? = null
    set(value) {
        if (value != null) onChange(field, value)
        field = value
    }

    abstract fun provideValue(name: String): T

    final override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value ?: provideValue(getResName(resName, property)).also { value = it }
    }

    final override fun setValue(thisRef: Any, property: KProperty<*>, newValue: T) {
        value = newValue
    }

    private fun getResName(explicitName: String?, property: KProperty<*>): String {
        return explicitName ?: property.name
    }
}

/* Boolean */

fun AttributeHelper.bool(
    defaultValue: Boolean = false,
    resName: String? = null,
    onChange: (oldValue: Boolean?, newValue: Boolean) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Boolean> = object : AttributeHelperDelegate<Boolean>(resName, onChange) {
    override fun provideValue(name: String): Boolean {
        return getBool(name, defaultValue)
    }
}

/* String */

fun AttributeHelper.string(
    defaultValue: String = NO_VALUE_STRING,
    resName: String? = null,
    onChange: (oldValue: String?, newValue: String) -> Unit = { _, _ -> }
): AttributeHelperDelegate<String> = object : AttributeHelperDelegate<String>(resName, onChange) {
    override fun provideValue(name: String): String {
        return getString(name, defaultValue)
    }
}

fun AttributeHelper.stringOrNull(
    resName: String? = null,
    onChange: (oldValue: String?, newValue: String?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<String?> = object : AttributeHelperDelegate<String?>(resName, onChange) {
    override fun provideValue(name: String): String? {
        return getStringOrNull(name)
    }
}

/* Int */

fun AttributeHelper.int(
    defaultValue: Int = NO_VALUE,
    resName: String? = null,
    onChange: (oldValue: Int?, newValue: Int) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int> = object : AttributeHelperDelegate<Int>(resName, onChange) {
    override fun provideValue(name: String): Int {
        return getInt(name, defaultValue)
    }
}

fun AttributeHelper.intOrNull(
    resName: String? = null,
    onChange: (oldValue: Int?, newValue: Int?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int?> = object : AttributeHelperDelegate<Int?>(resName, onChange) {
    override fun provideValue(name: String): Int? {
        return getIntOrNull(name)
    }
}

/* Float */

fun AttributeHelper.float(
    defaultValue: Float = NO_VALUE_FLOAT,
    resName: String? = null,
    onChange: (oldValue: Float?, newValue: Float) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Float> = object : AttributeHelperDelegate<Float>(resName, onChange) {
    override fun provideValue(name: String): Float {
        return getFloat(name, defaultValue)
    }
}

fun AttributeHelper.floatOrNull(
    resName: String? = null,
    onChange: (oldValue: Float?, newValue: Float?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Float?> = object : AttributeHelperDelegate<Float?>(resName, onChange) {
    override fun provideValue(name: String): Float? {
        return getFloatOrNull(name)
    }
}

/* Resources */

fun AttributeHelper.resource(
    defaultValue: Int = NO_VALUE,
    resName: String? = null,
    onChange: (oldValue: Int?, newValue: Int) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int> = object : AttributeHelperDelegate<Int>(resName, onChange) {
    override fun provideValue(name: String): Int {
        return getResourceId(name, defaultValue)
    }
}

fun AttributeHelper.resourceOrNull(
    resName: String? = null,
    onChange: (oldValue: Int?, newValue: Int?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int?> = object : AttributeHelperDelegate<Int?>(resName, onChange) {
    override fun provideValue(name: String): Int? {
        return getResourceIdOrNull(name)
    }
}

/* Dimension */

fun AttributeHelper.dimension(
    defaultValue: Float = NO_VALUE_FLOAT,
    resName: String? = null,
    onChange: (oldValue: Float?, newValue: Float) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Float> = object : AttributeHelperDelegate<Float>(resName, onChange) {
    override fun provideValue(name: String): Float {
        return getDimension(name, defaultValue)
    }
}

fun AttributeHelper.dimensionOrNull(
    resName: String? = null,
    onChange: (oldValue: Float?, newValue: Float?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Float?> = object : AttributeHelperDelegate<Float?>(resName, onChange) {
    override fun provideValue(name: String): Float? {
        return getDimensionOrNull(name)
    }
}

fun AttributeHelper.dimensionPixelSize(
    defaultValue: Int = NO_VALUE,
    resName: String? = null,
    onChange: (oldValue: Int?, newValue: Int) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int> = object : AttributeHelperDelegate<Int>(resName, onChange) {
    override fun provideValue(name: String): Int {
        return getDimensionPixelSize(name, defaultValue)
    }
}

fun AttributeHelper.dimensionPixelSizeOrNull(
    resName: String? = null,
    onChange: (oldValue: Int?, newValue: Int?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int?> = object : AttributeHelperDelegate<Int?>(resName, onChange) {
    override fun provideValue(name: String): Int? {
        return getDimensionPixelSizeOrNull(name)
    }
}

/* Color */

fun AttributeHelper.color(
    @ColorInt defaultValue: Int = NO_VALUE,
    resName: String? = null,
    onChange: (oldValue: Int?, newValue: Int) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int> = object : AttributeHelperDelegate<Int>(resName, onChange) {
    @ColorInt
    override fun provideValue(name: String): Int {
        return getColor(name, defaultValue)
    }
}

fun AttributeHelper.color(
    resName: String? = null,
    onChange: (oldValue: Int?, newValue: Int) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int> = object : AttributeHelperDelegate<Int>(resName, onChange) {
    override fun provideValue(name: String): Int {
        return getColor(name)
    }
}

fun AttributeHelper.colorOrNull(
    resName: String? = null,
    onChange: (oldValue: Int?, newValue: Int?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int?> = object : AttributeHelperDelegate<Int?>(resName, onChange) {
    override fun provideValue(name: String): Int? {
        return getColorOrNull(name)
    }
}

/* ColorStateList */
fun AttributeHelper.colorStateList(
    defaultValue: ColorStateList,
    resName: String? = null,
    onChange: (oldValue: ColorStateList?, newValue: ColorStateList) -> Unit = { _, _ -> }
): AttributeHelperDelegate<ColorStateList> =
    object : AttributeHelperDelegate<ColorStateList>(resName, onChange) {
        override fun provideValue(name: String): ColorStateList {
            return getColorStateList(name, defaultValue)
        }
    }

fun AttributeHelper.colorStateList(
    resName: String? = null,
    onChange: (oldValue: ColorStateList?, newValue: ColorStateList) -> Unit = { _, _ -> }
): AttributeHelperDelegate<ColorStateList> =
    object : AttributeHelperDelegate<ColorStateList>(resName, onChange) {
        override fun provideValue(name: String): ColorStateList {
            return getColorStateList(name)
        }
    }

fun AttributeHelper.colorStateListOrNull(
    resName: String? = null,
    onChange: (oldValue: ColorStateList?, newValue: ColorStateList?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<ColorStateList?> =
    object : AttributeHelperDelegate<ColorStateList?>(resName, onChange) {
        override fun provideValue(name: String): ColorStateList? {
            return getColorStateListOrNull(name)
        }
    }

/* Drawable */

fun AttributeHelper.drawable(
    defaultValue: Drawable,
    resName: String? = null,
    onChange: (oldValue: Drawable?, newValue: Drawable) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Drawable> =
    object : AttributeHelperDelegate<Drawable>(resName, onChange) {
        override fun provideValue(name: String): Drawable {
            return getDrawable(name, defaultValue)
        }
    }

fun AttributeHelper.drawableOrNull(
    resName: String? = null,
    onChange: (oldValue: Drawable?, newValue: Drawable?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Drawable?> =
    object : AttributeHelperDelegate<Drawable?>(resName, onChange) {
        override fun provideValue(name: String): Drawable? {
            return getDrawableOrNull(name)
        }
    }

/* Text array */

fun AttributeHelper.textArray(
    resName: String? = null,
    onChange: (oldValue: List<String>?, newValue: List<String>) -> Unit = { _, _ -> }
): AttributeHelperDelegate<List<String>> =
    object : AttributeHelperDelegate<List<String>>(resName, onChange) {
        override fun provideValue(name: String): List<String> {
            return getTextArray(name)
        }
    }

fun AttributeHelper.textArrayOrNull(
    resName: String? = null,
    onChange: (oldValue: List<String>?, newValue: List<String>?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<List<String>?> =
    object : AttributeHelperDelegate<List<String>?>(resName, onChange) {
        override fun provideValue(name: String): List<String>? {
            return getTextArrayOrNull(name)
        }
    }

/* Enum */

fun <T> AttributeHelper.enum(
    defaultValue: T? = null,
    resName: String? = null,
    onChange: (oldValue: T?, newValue: T) -> Unit = { _, _ -> },
    provider: (Int) -> T
): AttributeHelperDelegate<T> = object : AttributeHelperDelegate<T>(resName, onChange) {
    override fun provideValue(name: String): T {
        return getEnum(name, defaultValue, provider)
    }
}