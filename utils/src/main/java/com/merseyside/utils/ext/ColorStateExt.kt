package com.merseyside.utils.ext

import android.content.res.ColorStateList
import androidx.annotation.ColorInt

@ColorInt
fun ColorStateList.getColorForState(state: Int, @ColorInt defaultColor: Int = this.defaultColor): Int {
    return getColorForState(intArrayOf(state), defaultColor)
}

fun ColorStateList.hasState(state: Int): Boolean {
    return getColorForState(state, -1) != -1
}