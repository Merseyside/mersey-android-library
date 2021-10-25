package com.merseyside.utils.gestures

import android.graphics.Point
import android.view.GestureDetector
import android.view.MotionEvent
import com.merseyside.utils.Vibration

abstract class ClickGestureListener: GestureDetector.SimpleOnGestureListener() {

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        onClick(Point(e.x.toInt(),  e.y.toInt()))
        return super.onSingleTapUp(e)

    }

    override fun onLongPress(e: MotionEvent) {
        super.onLongPress(e)
        if (onLongPress(Point(e.x.toInt(), e.y.toInt()))) {
            Vibration.getInstance().vibrate()
        }
    }

    abstract fun onLongPress(point: Point): Boolean
    abstract fun onClick(point: Point)
}