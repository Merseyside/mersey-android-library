package com.merseyside.utils.attributes

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes
import com.merseyside.utils.attributes.AttributeHelper.Companion.NO_VALUE
import com.merseyside.utils.attributes.AttributeHelper.Companion.NO_VALUE_FLOAT
import com.merseyside.utils.attributes.AttributeHelper.Companion.NO_VALUE_STRING
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AttributeHelperDelegate<T>(
    @StyleableRes private val resId: Int,
    private val onChange: (oldValue: T?, newValue: T) -> Unit
) : ReadWriteProperty<Any, T> {

    var value: T? = null
    set(value) {
        if (value != null) onChange(field, value)
        field = value
    }
    
    fun init() {
        value = provideValue(resId)
    } 

    abstract fun provideValue(resId: Int): T

    final override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value ?: provideValue(resId).also { value = it }
    }

    final override fun setValue(thisRef: Any, property: KProperty<*>, newValue: T) {
        value = newValue
    }
}

/* Boolean */

fun AttributeHelper.bool(
    @StyleableRes resId: Int,
    defaultValue: Boolean = false,
    onChange: (oldValue: Boolean?, newValue: Boolean) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Boolean> = object : AttributeHelperDelegate<Boolean>(resId, onChange) {
    
    init {
        init()
    }
    
    override fun provideValue(id: Int): Boolean {
        return getBool(id, defaultValue)
    }
}

/* String */

fun AttributeHelper.string(
    @StyleableRes resId: Int,
    defaultValue: String = NO_VALUE_STRING,
    onChange: (oldValue: String?, newValue: String) -> Unit = { _, _ -> }
): AttributeHelperDelegate<String> = object : AttributeHelperDelegate<String>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): String {
        return getString(id, defaultValue)
    }
}

fun AttributeHelper.stringOrNull(
    @StyleableRes resId: Int,
    onChange: (oldValue: String?, newValue: String?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<String?> = object : AttributeHelperDelegate<String?>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): String? {
        return getStringOrNull(id)
    }
}

/* Int */

fun AttributeHelper.int(
    @StyleableRes resId: Int,
    defaultValue: Int = NO_VALUE,
    onChange: (oldValue: Int?, newValue: Int) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int> = object : AttributeHelperDelegate<Int>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): Int {
        return getInt(id, defaultValue)
    }
}

fun AttributeHelper.intOrNull(
    @StyleableRes resId: Int,
    onChange: (oldValue: Int?, newValue: Int?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int?> = object : AttributeHelperDelegate<Int?>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): Int? {
        return getIntOrNull(id)
    }
}

/* Float */

fun AttributeHelper.float(
    @StyleableRes resId: Int,
    defaultValue: Float = NO_VALUE_FLOAT,
    onChange: (oldValue: Float?, newValue: Float) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Float> = object : AttributeHelperDelegate<Float>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): Float {
        return getFloat(id, defaultValue)
    }
}

fun AttributeHelper.floatOrNull(
    @StyleableRes resId: Int,
    onChange: (oldValue: Float?, newValue: Float?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Float?> = object : AttributeHelperDelegate<Float?>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): Float? {
        return getFloatOrNull(id)
    }
}

/* Resources */

fun AttributeHelper.resource(
    @StyleableRes resId: Int,
    defaultValue: Int = NO_VALUE,
    onChange: (oldValue: Int?, newValue: Int) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int> = object : AttributeHelperDelegate<Int>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): Int {
        return getResourceId(id, defaultValue)
    }
}

fun AttributeHelper.resourceOrNull(
    @StyleableRes resId: Int,
    onChange: (oldValue: Int?, newValue: Int?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int?> = object : AttributeHelperDelegate<Int?>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): Int? {
        return getResourceIdOrNull(id)
    }
}

/* Dimension */

fun AttributeHelper.dimension(
    @StyleableRes resId: Int,
    defaultValue: Float = NO_VALUE_FLOAT,
    onChange: (oldValue: Float?, newValue: Float) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Float> = object : AttributeHelperDelegate<Float>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): Float {
        return getDimension(id, defaultValue)
    }
}

fun AttributeHelper.dimensionOrNull(
    @StyleableRes resId: Int,
    onChange: (oldValue: Float?, newValue: Float?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Float?> = object : AttributeHelperDelegate<Float?>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): Float? {
        return getDimensionOrNull(id)
    }
}

fun AttributeHelper.dimensionPixelSize(
    @StyleableRes resId: Int,
    defaultValue: Int = NO_VALUE,
    onChange: (oldValue: Int?, newValue: Int) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int> = object : AttributeHelperDelegate<Int>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): Int {
        return getDimensionPixelSize(id, defaultValue)
    }
}

fun AttributeHelper.dimensionPixelSizeOrNull(
    @StyleableRes resId: Int,
    onChange: (oldValue: Int?, newValue: Int?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int?> = object : AttributeHelperDelegate<Int?>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): Int? {
        return getDimensionPixelSizeOrNull(id)
    }
}

/* Color */

fun AttributeHelper.color(
    @StyleableRes resId: Int,
    @ColorInt defaultValue: Int = NO_VALUE,
    onChange: (oldValue: Int?, newValue: Int) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int> = object : AttributeHelperDelegate<Int>(resId, onChange) {
    init {
        init()
    }

    @ColorInt
    override fun provideValue(id: Int): Int {
        return getColor(id, defaultValue)
    }
}

fun AttributeHelper.color(
    @StyleableRes resId: Int,
    onChange: (oldValue: Int?, newValue: Int) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int> = object : AttributeHelperDelegate<Int>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): Int {
        return getColor(id)
    }
}

fun AttributeHelper.colorOrNull(
    @StyleableRes resId: Int,
    onChange: (oldValue: Int?, newValue: Int?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Int?> = object : AttributeHelperDelegate<Int?>(resId, onChange) {
    override fun provideValue(id: Int): Int? {
        return getColorOrNull(id)
    }
}

/* ColorStateList */
fun AttributeHelper.colorStateList(
    @StyleableRes resId: Int,
    defaultValue: ColorStateList,
    onChange: (oldValue: ColorStateList?, newValue: ColorStateList) -> Unit = { _, _ -> }
): AttributeHelperDelegate<ColorStateList> =
    object : AttributeHelperDelegate<ColorStateList>(resId, onChange) {
        init {
            init()
        }

        override fun provideValue(id: Int): ColorStateList {
            return getColorStateList(id, defaultValue)
        }
    }

fun AttributeHelper.colorStateList(
    @StyleableRes resId: Int,
    onChange: (oldValue: ColorStateList?, newValue: ColorStateList) -> Unit = { _, _ -> }
): AttributeHelperDelegate<ColorStateList> =
    object : AttributeHelperDelegate<ColorStateList>(resId, onChange) {
        init {
            init()
        }

        override fun provideValue(id: Int): ColorStateList {
            return getColorStateList(id)
        }
    }

fun AttributeHelper.colorStateListOrNull(
    @StyleableRes resId: Int,
    onChange: (oldValue: ColorStateList?, newValue: ColorStateList?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<ColorStateList?> =
    object : AttributeHelperDelegate<ColorStateList?>(resId, onChange) {
        init {
            init()
        }

        override fun provideValue(id: Int): ColorStateList? {
            return getColorStateListOrNull(id)
        }
    }

/* Drawable */

fun AttributeHelper.drawable(
    @StyleableRes resId: Int,
    defaultValue: Drawable,
    onChange: (oldValue: Drawable?, newValue: Drawable) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Drawable> =
    object : AttributeHelperDelegate<Drawable>(resId, onChange) {
        init {
            init()
        }

        override fun provideValue(id: Int): Drawable {
            return getDrawable(id, defaultValue)
        }
    }

fun AttributeHelper.drawableOrNull(
    @StyleableRes resId: Int,
    onChange: (oldValue: Drawable?, newValue: Drawable?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<Drawable?> =
    object : AttributeHelperDelegate<Drawable?>(resId, onChange) {
        init {
            init()
        }

        override fun provideValue(id: Int): Drawable? {
            return getDrawableOrNull(id)
        }
    }

/* Text array */

fun AttributeHelper.textArray(
    @StyleableRes resId: Int,
    onChange: (oldValue: List<String>?, newValue: List<String>) -> Unit = { _, _ -> }
): AttributeHelperDelegate<List<String>> =
    object : AttributeHelperDelegate<List<String>>(resId, onChange) {
        init {
            init()
        }

        override fun provideValue(id: Int): List<String> {
            return getTextArray(id)
        }
    }

fun AttributeHelper.textArrayOrNull(
    @StyleableRes resId: Int,
    onChange: (oldValue: List<String>?, newValue: List<String>?) -> Unit = { _, _ -> }
): AttributeHelperDelegate<List<String>?> =
    object : AttributeHelperDelegate<List<String>?>(resId, onChange) {
        init {
            init()
        }

        override fun provideValue(id: Int): List<String>? {
            return getTextArrayOrNull(id)
        }
    }

/* Enum */

fun <T> AttributeHelper.enum(
    @StyleableRes resId: Int,
    defaultValue: T? = null,
    onChange: (oldValue: T?, newValue: T) -> Unit = { _, _ -> },
    provider: (Int) -> T
): AttributeHelperDelegate<T> = object : AttributeHelperDelegate<T>(resId, onChange) {
    init {
        init()
    }

    override fun provideValue(id: Int): T {
        return getEnum(id, defaultValue, provider)
    }
}