package com.merseyside.utils.ext

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.*
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.merseyside.utils.reflection.callMethodByName

fun Drawable.setColor(@ColorInt color: Int): Drawable {
    if (this is VectorDrawable || this is RippleDrawable) {
        colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    } else {

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

    return this
}

fun Drawable.setColor(context: Context, @ColorRes color: Int) {
    setColor(ContextCompat.getColor(context, color))
}

@ColorInt
fun Drawable.getColor(): Int? {
    return when(this) {
        is ColorDrawable -> color
        else  ->
            getColorFromColorFilter(this)
    }

}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun getColorFromColorFilter(drawable: Drawable): Int? {
    return drawable.colorFilter?.let {
        val colorFilter = (it as PorterDuffColorFilter)

        colorFilter.callMethodByName("getColor") as Int
    }
}