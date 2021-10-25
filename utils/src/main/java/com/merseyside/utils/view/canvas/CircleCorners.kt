package com.merseyside.utils.view.canvas

import android.graphics.RectF

object CircleCorners : CornerRadius(0, 0) {

    fun getCornerRadius(rect: RectF): CornerRadius {
        with(rect) {
            val size = (bottom - top) / 2
            return CornerRadius(size, size)
        }
    }
}