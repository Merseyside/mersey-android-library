package com.merseyside.utils.colorStateList

import android.content.res.ColorStateList
import androidx.annotation.ColorInt

fun colorToSimpleStateList(@ColorInt color: Int): ColorStateList {
    val states = arrayOf(
        intArrayOf(android.R.attr.state_focused, android.R.attr.state_pressed),
        intArrayOf(android.R.attr.state_hovered),
        intArrayOf(android.R.attr.state_enabled),
        intArrayOf(android.R.attr.state_pressed)
    )

    val colors = intArrayOf(color, color, color, color)

    return ColorStateList(states, colors)
}