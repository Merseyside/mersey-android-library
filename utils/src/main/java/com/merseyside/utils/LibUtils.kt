@file:JvmName("LibUtils")
package com.merseyside.utils

import android.content.Context
import android.graphics.Color
import android.os.Environment
import androidx.annotation.ColorInt
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

fun convertPixelsToDp(context: Context, px: Int): Float {
    val density = context.resources.displayMetrics.density
    return px / density
}

fun convertDpToPixel(context: Context, dp: Float): Float {
    val density = context.resources.displayMetrics.density
    return (dp * density)
}

fun isExternalStorageReadable(): Boolean {
    val state = Environment.getExternalStorageState()
    return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
}

@ColorInt
fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
    val alpha = (Color.alpha(color) * factor).roundToInt()
    val red: Int = Color.red(color)
    val green: Int = Color.green(color)
    val blue: Int = Color.blue(color)
    return Color.argb(alpha, red, green, blue)
}

fun <T: Number> getMinMax(first: T, second: T): Pair<T, T> {
    val min = min(first.toInt(), second.toInt())
    val max = max(first.toInt(), second.toInt())

    return min as T to max as T
}