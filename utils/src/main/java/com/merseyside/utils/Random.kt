package com.merseyside.utils

import android.graphics.Color
import androidx.annotation.ColorInt
import java.util.*

@ColorInt
fun randomColor(alpha: Int = 255): Int {
    val rnd = Random()
    return Color.argb(alpha, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
}