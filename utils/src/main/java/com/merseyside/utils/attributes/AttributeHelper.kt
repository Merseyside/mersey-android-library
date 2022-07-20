package com.merseyside.utils.attributes

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import androidx.core.content.res.getColorOrThrow
import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.utils.ext.capitalize
import java.lang.reflect.Field

class AttributeHelper(
    val context: Context,
    attributeSet: AttributeSet?,
    @StyleableRes attrs: IntArray,
    private val declareStyleableName: String,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
    private val styleableNamePrefix: String = "",
    private val packageName: String = context.packageName
) {

    private val ta = context.obtainStyledAttributes(attributeSet, attrs, defStyleAttr, defStyleRes)

    fun getBool(name: String, defValue: Boolean): Boolean {
        return requireDefValueIfEmpty(name, defValue) { id ->
            ta.getBoolean(id, defValue)
        }
    }

    fun getInt(name: String, defValue: Int = NO_VALUE): Int {
        return requireDefValueIfEmpty(name, defValue) { id ->
            ta.getInt(id, defValue)
        }
    }

    fun getIntOrNull(name: String): Int? {
        return convertNoValueToNull(name, NO_VALUE) { id ->
            ta.getInt(id, NO_VALUE)
        }
    }

    fun getFloat(name: String, defValue: Float = NO_VALUE_FLOAT): Float {
        return requireDefValueIfEmpty(name, defValue) { id ->
            ta.getFloat(id, defValue)
        }
    }

    fun getFloatOrNull(name: String): Float? {
        return convertNoValueToNull(name, NO_VALUE) { id ->
            ta.getFloat(id, NO_VALUE_FLOAT)
        }
    }

    fun getString(name: String, defValue: String = NO_VALUE_STRING): String {
        return requireDefValueIfEmpty(name, defValue) { id ->
            ta.getString(id) ?: defValue
        }
    }

    fun getStringOrNull(name: String): String? {
        return convertNoValueToNull(name, NO_VALUE_STRING) { id ->
            ta.getString(id) ?: NO_VALUE_STRING
        }
    }

    fun getDimension(name: String, defValue: Float = NO_VALUE_FLOAT): Float {
        return requireDefValueIfEmpty(name, defValue) { id ->
            ta.getDimension(id, defValue)
        }
    }

    fun getDimensionOrNull(name: String): Float? {
        return convertNoValueToNull(name, NO_VALUE_FLOAT) { id ->
            ta.getDimension(id, NO_VALUE_FLOAT)
        }
    }

    fun getDimensionPixelSize(name: String, defValue: Int = NO_VALUE): Int {
        return requireDefValueIfEmpty(name, defValue) { id ->
            ta.getDimensionPixelSize(id, defValue)
        }
    }

    fun getDimensionPixelSizeOrNull(name: String): Int? {
        return convertNoValueToNull(name, NO_VALUE) { id ->
            ta.getDimensionPixelSize(id, NO_VALUE)
        }
    }

    fun getResourceId(name: String, defValue: Int = NO_VALUE): Int {
        return requireDefValueIfEmpty(name, defValue) { id ->
            ta.getResourceId(id, defValue)
        }
    }

    fun getResourceIdOrNull(name: String): Int? {
        return convertNoValueToNull(name, NO_VALUE) { id ->
            ta.getResourceId(id, NO_VALUE)
        }
    }

    @ColorInt
    fun getColor(name: String, @ColorInt defValue: Int): Int {
        return requireDefValueIfEmpty(name, defValue) { id ->
            ta.getColor(id, defValue)
        }
    }

    @ColorInt
    fun getColor(name: String): Int {
        return ta.getColorOrThrow(getIdentifier(name))
    }

    @ColorInt
    fun getColorOrNull(name: String): Int? {
        return convertNoValueToNull(name, NO_VALUE) { id ->
            ta.getColor(id, NO_VALUE)
        }
    }

    fun getDrawable(name: String, defValue: Drawable): Drawable {
        return getDrawableOrNull(name) ?: defValue
    }

    fun getDrawableOrNull(name: String): Drawable? {
        val id = getIdentifierOrNull(name)
        return id?.let { ta.getDrawable(id) }
    }

    fun recycle() {
        ta.recycle()
    }

    @Throws(IllegalArgumentException::class)
    private fun getIdentifier(name: String, dsn: String = declareStyleableName): Int {

        fun tryAndroidNamespace(): Int {
            val fullName = StringBuilder().apply {
                append(dsn).append("_")
                append("android").append("_")
                append(name)
            }.toString()

            return getStyleableId(fullName)
        }

        val index = getStyleableId(buildFullName(name, dsn))
        return if (index < 0) {
            val id = tryAndroidNamespace()
            if (id < 0) {
                throw IllegalArgumentException(
                    "Resource with name $name not found in $defPackage." +
                            " Had look for ${buildFullName(name, dsn)}"
                )
            } else id
        } else index
    }


    private fun buildFullName(
        name: String,
        dsn: String = declareStyleableName,
        prefix: String = styleableNamePrefix
    ): String {
        return StringBuilder().apply {
            append(dsn)

            val nameWithPrefix = if (prefix.isNotEmpty()) {
                "${prefix}${name.capitalize()}"
            } else name

            append("_").append(nameWithPrefix)
        }.toString()
    }

    private fun <T> convertNoValueToNull(name: String, noValue: Any, block: (Int) -> T): T? {
        return try {
            requireDefValueIfEmpty(name, noValue) { id -> block(id) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalArgumentException::class)
    private fun <T> requireDefValueIfEmpty(
        name: String,
        defValue: Any,
        block: ((Int) -> T)? = null
    ): T {
        val id = getIdentifierOrNull(name)

        val value = if (id == null) {
            defValue
        } else if (block != null) {
            block(id)
        } else defValue

        return if (value == NO_VALUE || value == NO_VALUE_FLOAT
            || value == NO_VALUE_STRING
        ) {
            throw IllegalArgumentException("Default value not passed and attribute is null!")
        } else value as T
    }

    private fun getStyleableId(name: String): Int {
        for (f in fields) {
            if (f.name == name) {
                return f.get(null) as Int
            }
        }

        return -1
    }

    private fun getIdentifierOrNull(name: String, dsn: String = declareStyleableName): Int? {
        return try {
            getIdentifier(name, dsn)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private val fields: Array<Field> by lazy { getStyleableClass(packageName).fields }

    companion object {
        private const val defPackage = "styleable"

        internal const val NO_VALUE = Int.MIN_VALUE
        internal const val NO_VALUE_FLOAT = Float.MIN_VALUE
        internal const val NO_VALUE_STRING = "attribute_helper_no_value"

        private fun getStyleableClass(packageName: String): Class<*> {
            var mutPackage = packageName
            var index: Int
            do {
                try {
                    return Class.forName("$mutPackage.R\$styleable")
                } catch (ignored: ClassNotFoundException) {
                    Logger.logErr("Tried to get R class with $mutPackage package but failed!")
                    index = mutPackage.indexOfLast { it == '.' }

                    if (index != -1) mutPackage = mutPackage.substring(0, index)
                }
            } while (index != -1)

            throw ClassNotFoundException("Can not find R class with passed $packageName package name")
        }
    }

}