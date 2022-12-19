package com.merseyside.utils.ext

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorInt

@ColorInt
fun View.getColorByCurrentState(colorStateList: ColorStateList, viewStates: IntArray): Int {
    val currentState = viewStates.find { state ->
        drawableState.find { it == state } != null
    }

    return if (currentState == null) colorStateList.defaultColor
    else colorStateList.getColorForState(intArrayOf(currentState), colorStateList.defaultColor)
}
