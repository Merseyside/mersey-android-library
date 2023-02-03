package com.merseyside.utils.ext

import android.content.res.ColorStateList
import androidx.annotation.ColorInt

@ColorInt
fun ColorStateList.getColorForState(state: Int): Int {
    return getColorForState(intArrayOf(state), defaultColor)
}