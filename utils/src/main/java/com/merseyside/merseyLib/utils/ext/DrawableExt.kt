package com.merseyside.merseyLib.utils.ext

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.*
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

@SuppressLint("NewApi")
fun Drawable.setColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if (this is VectorDrawable || this is RippleDrawable) {
            colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
            return
        }
    }

    when (this) {

        is GradientDrawable -> {
            setColor(color)
        }
        is ColorDrawable -> {
            this.color = color
        }
        is ShapeDrawable -> {
            paint.color = color
        }
    }
}

fun Drawable.setColor(context: Context, @ColorRes color: Int) {
    setColor(ContextCompat.getColor(context, color))
}

@ColorInt
fun Drawable.getColor(): Int {
    return (this as ColorDrawable).color
}