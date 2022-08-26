package com.merseyside.utils.ext

import android.widget.ImageView
import androidx.annotation.ColorInt

fun ImageView.setImageColor(@ColorInt color: Int) {
    setImageDrawable(drawable.setColor(color))
}