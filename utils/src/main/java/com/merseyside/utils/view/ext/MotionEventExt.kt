package com.merseyside.utils.view.ext

import android.graphics.Point
import android.view.MotionEvent

fun MotionEvent.getCoordPoint(): Point {
    return Point(x.toInt(), y.toInt())
}

fun MotionEvent.getRawCoordPoint(): Point {
    return Point(rawX.toInt(), rawY.toInt())
}