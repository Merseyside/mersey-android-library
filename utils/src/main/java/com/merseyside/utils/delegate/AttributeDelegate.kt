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

/**
 * AttributeSet recycled in View's constructor. That's why we can use it only in init block.
 * And that's why first using of delegated property not inside of init block without explicit setting of resName is impossible.
 */

/* Boolean */

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

/* String */

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

/* Int */

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

/* Float */

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

/* Resources */

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

/* Dimension */

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

/* Color */

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

/* ColorStateList */
fun AttributeHelper.colorStateList(
    defaultValue: ColorStateList,
    resName: String? = null
): ReadWriteProperty<Any, ColorStateList> = object : ReadWriteProperty<Any, ColorStateList> {
    var value: ColorStateList? = null

    init {
        if (resName != null) value = getColorStateList(resName, defaultValue)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): ColorStateList {
        return value ?: getColorStateList(property.name, defaultValue)
            .also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: ColorStateList) {
        this.value = value
    }
}

fun AttributeHelper.colorStateList(
    resName: String? = null
): ReadWriteProperty<Any, ColorStateList> = object : ReadWriteProperty<Any, ColorStateList> {
    var value: ColorStateList? = null

    init {
        if (resName != null) value = getColorStateList(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): ColorStateList {
        return value ?: getColorStateList(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: ColorStateList) {
        this.value = value
    }
}

fun AttributeHelper.colorStateListOrNull(
    resName: String? = null
): ReadWriteProperty<Any, ColorStateList?> = object : ReadWriteProperty<Any, ColorStateList?> {
    var value: ColorStateList? = null

    init {
        if (resName != null) value = getColorStateListOrNull(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): ColorStateList? {
        return value ?: getColorStateListOrNull(property.name)?.also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: ColorStateList?) {
        this.value = value
    }
}

/* Drawable */

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

/* Text array */

fun AttributeHelper.textArray(
    resName: String? = null
): ReadWriteProperty<Any, List<String>> = object : ReadWriteProperty<Any, List<String>> {
    var value: List<String>? = null

    init {
        if (resName != null) value = getTextArrayOrNull(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): List<String> {
        return value ?: getTextArray(property.name).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: List<String>) {
        this.value = value
    }
}

fun AttributeHelper.textArrayOrNull(
    resName: String? = null
): ReadWriteProperty<Any, List<String>?> = object : ReadWriteProperty<Any, List<String>?> {
    var value: List<String>? = null

    init {
        if (resName != null) value = getTextArrayOrNull(resName)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): List<String>? {
        return value ?: getTextArrayOrNull(property.name)?.also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: List<String>?) {
        this.value = value
    }
}

/* Enum */

fun <T> AttributeHelper.enum(
    defaultValue: T? = null,
    resName: String? = null,
    provider: (Int) -> T
): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {
    var value: T? = null

    init {
        if (resName != null) value = getEnum(resName, defaultValue, provider)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value ?: getEnum(property.name, defaultValue, provider).also { value = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }
}