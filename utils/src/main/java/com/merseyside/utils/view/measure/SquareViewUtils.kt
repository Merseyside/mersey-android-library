package com.merseyside.utils.view.measure

import android.view.View

fun getSquaredViewSizes(widthMeasureSpec: Int, heightMeasureSpec: Int): Pair<Int, Int> {
    if (canViewBeSquared(widthMeasureSpec, heightMeasureSpec)) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        return if (widthSize == heightSize) widthSize to heightSize
        else if (isSizeAvailable(heightMode, heightSize, widthSize)) {
            widthSize to getAvailableSize(heightMode, heightSize, widthSize)
        } else getAvailableSize(widthMode, widthSize, heightSize) to heightSize
    } else throw IllegalArgumentException("View can not be squared!")
}

fun canViewBeSquared(widthMeasureSpec: Int, heightMeasureSpec: Int): Boolean {
    val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
    val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
    val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
    val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

    return if (widthMode == View.MeasureSpec.UNSPECIFIED && heightMode == View.MeasureSpec.UNSPECIFIED)
        throw IllegalArgumentException("Both sizes are UNSPECIFIED. Set at least one implicitly")
    else if (widthSize == heightSize) true
    else if (widthMode == View.MeasureSpec.EXACTLY && heightMode == View.MeasureSpec.EXACTLY) false
    else {
        isSizeAvailable(heightMode, heightSize, widthSize) || isSizeAvailable(widthMode, widthSize, heightSize)
    }
}

private fun getAvailableSize(mode: Int, size: Int, desiredSize: Int): Int {
    if (isSizeAvailable(mode, size, desiredSize)) {
        return desiredSize
    } else throw IllegalArgumentException()
}

private fun isSizeAvailable(mode: Int, size: Int, desiredSize: Int): Boolean {
    return if (desiredSize == size) true
    else if (mode == View.MeasureSpec.EXACTLY) false
    else if (mode == View.MeasureSpec.AT_MOST) size >= desiredSize
    else true
}