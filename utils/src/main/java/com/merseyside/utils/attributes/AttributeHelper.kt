package com.merseyside.utils.attributes

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getColorStateListOrThrow

class AttributeHelper(
    val context: Context,
    attributeSet: AttributeSet?,
    @StyleableRes attrs: IntArray,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) {

    private val ta = context.obtainStyledAttributes(attributeSet, attrs, defStyleAttr, defStyleRes)

    fun getBool(id: Int, defValue: Boolean): Boolean {
        return requireDefValueIfEmpty(id, defValue) { id ->
            ta.getBoolean(id, defValue)
        }
    }

    fun getInt(id: Int, defValue: Int = NO_VALUE): Int {
        return requireDefValueIfEmpty(id, defValue) { id ->
            ta.getInt(id, defValue)
        }
    }

    fun getIntOrNull(id: Int): Int? {
        return convertNoValueToNull(id, NO_VALUE) { id ->
            ta.getInt(id, NO_VALUE)
        }
    }

    fun getFloat(id: Int, defValue: Float = NO_VALUE_FLOAT): Float {
        return requireDefValueIfEmpty(id, defValue) { id ->
            ta.getFloat(id, defValue)
        }
    }

    fun getFloatOrNull(id: Int): Float? {
        return convertNoValueToNull(id, NO_VALUE) { id ->
            ta.getFloat(id, NO_VALUE_FLOAT)
        }
    }

    fun getString(id: Int, defValue: String = NO_VALUE_STRING): String {
        return requireDefValueIfEmpty(id, defValue) { id ->
            ta.getString(id) ?: defValue
        }
    }

    fun getStringOrNull(id: Int): String? {
        return convertNoValueToNull(id, NO_VALUE_STRING) { id ->
            ta.getString(id) ?: NO_VALUE_STRING
        }
    }

    fun getDimension(id: Int, defValue: Float = NO_VALUE_FLOAT): Float {
        return requireDefValueIfEmpty(id, defValue) { id ->
            ta.getDimension(id, defValue)
        }
    }

    fun getDimensionOrNull(id: Int): Float? {
        return convertNoValueToNull(id, NO_VALUE_FLOAT) { id ->
            ta.getDimension(id, NO_VALUE_FLOAT)
        }
    }

    fun getDimensionPixelSize(id: Int, defValue: Int = NO_VALUE): Int {
        return requireDefValueIfEmpty(id, defValue) { id ->
            ta.getDimensionPixelSize(id, defValue)
        }
    }

    fun getDimensionPixelSizeOrNull(id: Int): Int? {
        return convertNoValueToNull(id, NO_VALUE) { id ->
            ta.getDimensionPixelSize(id, NO_VALUE)
        }
    }

    fun getResourceId(id: Int, defValue: Int = NO_VALUE): Int {
        return requireDefValueIfEmpty(id, defValue) { id ->
            ta.getResourceId(id, defValue)
        }
    }

    fun getResourceIdOrNull(id: Int): Int? {
        return convertNoValueToNull(id, NO_VALUE) { id ->
            ta.getResourceId(id, NO_VALUE)
        }
    }

    @ColorInt
    fun getColor(id: Int, @ColorInt defValue: Int): Int {
        return requireDefValueIfEmpty(id, defValue) { id ->
            ta.getColor(id, defValue)
        }
    }

    @ColorInt
    fun getColor(id: Int): Int {
        return ta.getColorOrThrow(id)
    }

    @ColorInt
    fun getColorOrNull(id: Int): Int? {
        return convertNoValueToNull(id, NO_VALUE) { id ->
            ta.getColor(id, NO_VALUE)
        }
    }


    fun getColorStateList(id: Int, defValue: ColorStateList): ColorStateList {
        return requireDefValueIfEmpty(id, defValue) { id ->
            ta.getColorStateList(id)
        }
    }

    fun getColorStateList(id: Int): ColorStateList {
        return ta.getColorStateListOrThrow(id)
    }

    fun getColorStateListOrNull(id: Int): ColorStateList? {
        return try {
            getColorStateList(id)
        } catch (e: IllegalArgumentException) {
            null
        }
    }


    fun getDrawable(id: Int, defValue: Drawable): Drawable {
        return getDrawableOrNull(id) ?: defValue
    }

    fun getDrawableOrNull(id: Int): Drawable? {
        return id.let { ta.getDrawable(id) }
    }

    fun getTextArray(id: Int): List<String> {
        return id.let { ta.getTextArray(id) }?.map { it.toString() }
            ?: throw IllegalArgumentException("Not null identifier expected.")
    }

    fun getTextArrayOrNull(id: Int): List<String>? {
        return id.let { ta.getTextArray(id) }?.map { it.toString() }
    }

    fun <T> getEnum(id: Int, defValue: T? = null, provider: (Int) -> T): T {
        return requireDefValueIfEmpty(id, defValue) {
            val value = getIntOrNull(id)
            value?.let { provider(value) }
        }
    }


    fun recycle() {
        ta.recycle()
    }

    private fun <T> convertNoValueToNull(id: Int, noValue: Any, block: (Int) -> T): T? {
        return try {
            requireDefValueIfEmpty(id, noValue) { id -> block(id) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalArgumentException::class)
    private fun <T> requireDefValueIfEmpty(
        id: Int,
        defValue: Any?,
        block: ((Int) -> T?)? = null
    ): T {
        val value = if (block != null) {
            block(id) ?: defValue
        } else defValue

        return if (value == null || value == NO_VALUE ||
            value == NO_VALUE_FLOAT || value == NO_VALUE_STRING
        ) {
            throw IllegalArgumentException("Default value not passed and attribute is null!")
        } else value as T
    }

    companion object {
        internal const val NO_VALUE = Int.MIN_VALUE
        internal const val NO_VALUE_FLOAT = Float.MIN_VALUE
        internal const val NO_VALUE_STRING = "attribute_helper_no_value"
    }
}