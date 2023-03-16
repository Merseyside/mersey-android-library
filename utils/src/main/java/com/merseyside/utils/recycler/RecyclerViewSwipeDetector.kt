package com.merseyside.utils.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.utils.view.gesture.SafeGestureDetector
import kotlin.math.abs

class RecyclerViewSwipeDetector(context: Context, attrsSet: AttributeSet, defStyle: Int) :
    RecyclerView(context, attrsSet, defStyle) {

    constructor(context: Context, attrsSet: AttributeSet) : this(context, attrsSet, 0)

    private var onSwipeListener: OnSwipeListener? = null

    fun setOnSwipeListener(onSwipeListener: OnSwipeListener) {
        this.onSwipeListener = onSwipeListener
    }

    private val gestureDetector = SafeGestureDetector(context, GestureListener())

    override fun onTouchEvent(e: MotionEvent): Boolean {
        return if (gestureDetector.onTouchEvent(e)) {
            true
        } else super.onTouchEvent(e)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return true
        }

        override fun onContextClick(e: MotionEvent): Boolean {
            return super.onContextClick(e)
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (abs(diffX) > abs(diffY) && abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeListener?.onSwipeLeft()
                    } else {
                        onSwipeListener?.onSwipeRight()
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return false
        }
    }

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }

    interface OnSwipeListener {
        fun onSwipeLeft()

        fun onSwipeRight()
    }
}