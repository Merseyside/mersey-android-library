@file:JvmName("LibUtils")
package com.merseyside.utils

import android.content.Context
import android.graphics.Color
import android.os.Environment
import androidx.annotation.ColorInt
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.merseyLib.time.Time
import com.merseyside.merseyLib.time.units.TimeUnit
import com.merseyside.merseyLib.time.units.minus
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

suspend fun <Result> measureAndLogTime(tag: String = "Measuring", block: suspend () -> Result): Result {
    val startTime = Time.nowGMT
    val result = block()
    val endTime = Time.nowGMT
    val delta = endTime - startTime
    delta.log(tag, "elapsed time =")
    return result
}

inline fun <reified Clazz> Clazz.getClassName(): String {
    return this!!::class.simpleName ?: throw IllegalArgumentException("No class name")
}