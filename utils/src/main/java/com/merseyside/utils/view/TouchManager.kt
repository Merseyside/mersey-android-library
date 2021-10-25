package com.merseyside.utils.view

import android.graphics.Point
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.merseyside.utils.buffer.BufferQueue
import com.merseyside.utils.emptyMutableList
import com.merseyside.utils.gestures.ClickGestureListener
import com.merseyside.utils.gestures.SwipeGestureListener
import com.merseyside.utils.view.ext.inverse
import com.merseyside.utils.view.ext.minus
import com.merseyside.utils.view.ext.plus

class TouchManager(
    val enableLongPress: Boolean = true
) : View.OnTouchListener {

    var enabled: Boolean = true
    var isDragAndDrop: Boolean = false

    var globalWidth: Int = 0
        private set
    var globalHeight: Int = 0
        private set

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    private var isUserScrolling = false
    private var lastPosition: Point = Point(0, 0)
    private var scrollPosition = Point(0, 0)
        set(value) {

            scrollListeners.forEach {
                it.onPositionChanged(value)
            }

            onDeltaChanged(value - field)

            field = value
            if (isUserScrolling) pointQueue.add(value)
        }

    private val pointQueue = BufferQueue<Point>(2)
    private lateinit var view: View

    private var swipeDetector: GestureDetector? = null
    private val clickGestureDetector: GestureDetector by lazy {
        GestureDetector(view.context, object: ClickGestureListener() {
            override fun onLongPress(point: Point): Boolean {
                isLongPressing = true
                this@TouchManager.onLongPress(point)
                return true
            }

            override fun onClick(point: Point) {}
        })
    }

    private val scrollInterpolator: ScrollInterpolator by lazy {
        ScrollInterpolator(
            globalWidth - screenWidth,
            globalHeight - screenHeight
        ).apply {
            setTimeCoef(0.8F)
            setOnNewValueCallback {
                scrollPosition = it
            }
        }
    }

    private var scrollListeners: MutableList<OnScrollListener> = emptyMutableList()
    private var clickListeners: MutableList<OnClickListener> = emptyMutableList()

    interface OnScrollListener {
        fun onPositionChanged(position: Point)
        fun onDeltaChanged(delta: Point)
    }

    private var isLongPressing = false

    interface OnClickListener {
        fun onClick(globalCoord: Point, screenCoord: Point)
        fun onLongPressed(globalCoord: Point, screenCoord: Point)
        fun onLongPressMove(globalCoord: Point, screenCoord: Point)
        fun onLongPressReleased(globalCoord: Point, screenCoord: Point)
    }

    fun setView(view: View) {
        this.view = view
    }

    fun addGestureListener(listener: SwipeGestureListener?) {
        this.swipeDetector = GestureDetector(view.context, listener)
    }

    fun removeGestureListener() {
        this.swipeDetector = null
    }

    fun setGlobalSizes(
        globalWidth: Int,
        globalHeight: Int
    ) {
        this.globalWidth = globalWidth
        this.globalHeight = globalHeight

        scrollInterpolator.maxWidth = globalWidth - screenWidth
        scrollInterpolator.maxHeight = globalHeight - screenHeight
    }

    fun setScreenSizes(
        screenWidth: Int,
        screenHeight: Int
    ) {
        this.screenWidth = screenWidth
        this.screenHeight = screenHeight

        if (globalWidth == 0 || globalHeight == 0) return

        if (scrollPosition.x + screenWidth > globalWidth) {
            var newX = globalWidth - screenWidth
            if (newX < 0) newX = 0
            scrollPosition = Point(newX, scrollPosition.y)
        }

        if (scrollPosition.y + screenHeight > globalHeight) {
            var newY = globalHeight - screenHeight
            if (newY < 0) newY = 0
            scrollPosition = Point(scrollPosition.x, newY)
        }
    }

    private fun onClick(point: Point) {
        clickListeners.forEach { it.onClick(scrollPosition + point, point) }
    }

    private fun onLongPress(point: Point) {
        clickListeners.forEach { it.onLongPressed(scrollPosition + point, point) }
    }

    private fun onLongPressMove(point: Point) {
        clickListeners.forEach { it.onLongPressMove(scrollPosition + point, point) }
    }

    private fun onLongPressReleased(point: Point) {
        clickListeners.forEach { it.onLongPressReleased(scrollPosition + point, point) }
    }

    private fun onDeltaChanged(delta: Point) {
        scrollListeners.forEach { it.onDeltaChanged(delta) }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (enabled) {
            swipeDetector?.let { detector ->
                if (detector.onTouchEvent(event)) return true
            }

            if (enableLongPress) {
                if (clickGestureDetector.onTouchEvent(event)) return true
            }

            if (globalWidth == 0 || globalHeight == 0) return false

            val currentPoint = Point(event.x.toInt(), event.y.toInt())

            when (event.action and MotionEvent.ACTION_MASK) {

                MotionEvent.ACTION_DOWN -> {
                    lastPosition = currentPoint
                    scrollInterpolator.stopScrolling()
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isLongPressing) {
                        onLongPressMove(currentPoint)
                    } else if (lastPosition != currentPoint) {
                        isUserScrolling = true && !isLongPressing
                        scrollPosition = getGlobalPosition(currentPoint)
                    }
                }

                MotionEvent.ACTION_UP -> {
                    if (isLongPressing) {
                        isLongPressing = false
                        onLongPressReleased(currentPoint)
                    } else if (lastPosition == currentPoint && !isUserScrolling) {
                        onClick(lastPosition)
                        view.performClick()
                    } else {
                        if (pointQueue.isNotEmpty() && pointQueue.size != 1) {
                            scrollInterpolator.startScrolling(
                                pointQueue.getFirst()!!,
                                pointQueue.getLast()!!
                            )
                        }
                    }

                    isUserScrolling = false
                    pointQueue.clear()
                }
            }

            return true
        } else {
            return false
        }
    }

    private fun getGlobalPosition(point: Point): Point {
        var delta = Point(lastPosition.x - point.x, lastPosition.y - point.y)

        lastPosition = point

        if (isDragAndDrop) delta = delta.inverse()
        val tmpPosition = scrollPosition + delta

        return getValidPosition(tmpPosition)
    }

    fun moveToPosition(position: Point) {
        scrollPosition = getValidPosition(position)
    }

    fun moveOnDelta(delta: Point) {
        scrollPosition = getValidPosition(scrollPosition + delta)
    }

    fun addScrollListener(listener: OnScrollListener) {
        scrollListeners.add(listener)
    }

    fun removeScrollListener(listener: OnScrollListener) {
        scrollListeners.remove(listener)
    }

    fun addOnClickListener(listener: OnClickListener) {
        clickListeners.add(listener)
    }

    fun removeClickListener(listener: OnClickListener) {
        clickListeners.remove(listener)
    }

    fun clear() {
        lastPosition = Point()
        scrollPosition = Point()
    }

    private fun getValidPosition(tmpPosition: Point): Point {
        if (tmpPosition.x < 0) tmpPosition.x = 0
        if (tmpPosition.x + screenWidth > globalWidth) tmpPosition.x = globalWidth - screenWidth

        if (tmpPosition.y < 0) tmpPosition.y = 0
        if (tmpPosition.y + screenHeight > globalHeight) tmpPosition.y = globalHeight - screenHeight

        var globalPosition = Point()
        if (tmpPosition.x >= 0 && tmpPosition.x + screenWidth <= globalWidth) {
            globalPosition = Point(tmpPosition.x, scrollPosition.y)
        }

        if (tmpPosition.y >= 0 && tmpPosition.y + screenHeight <= globalHeight) {
            globalPosition = Point(globalPosition.x, tmpPosition.y)
        }

        return globalPosition
    }
}