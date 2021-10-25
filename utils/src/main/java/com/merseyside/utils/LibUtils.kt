@file:JvmName("LibUtils")
package com.merseyside.utils

import android.content.Context
import android.graphics.Color
import android.os.Environment
import androidx.annotation.ColorInt
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

fun randomBool(positiveProbability: Float = 0.5F): Boolean {
    return when {
        positiveProbability >= 1f -> true
        positiveProbability <= 0f -> false

        else -> {
            val rand = Random()
            rand.nextFloat() <= positiveProbability
        }
    }
}

fun convertPixelsToDp(context: Context, px: Int): Float {
    val density = context.resources.displayMetrics.density
    return px / density
}

fun convertDpToPixel(context: Context, dp: Float): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density).toInt()
}

fun generateRandomString(length: Int): String {
    if (length > 0) {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..length)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    return ""
}

fun isExternalStorageReadable(): Boolean {
    val state = Environment.getExternalStorageState()
    return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
}

fun shrinkNumber(number: Number): String {
    val long = number.toLong()

    return when {
        long < 1000 -> {
            long.toString()
        }
        long < 1_000_000 -> {
            "${long / 1000}K+"
        }
        else -> {
            "${long / 1_000_000}M+"
        }
    }
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