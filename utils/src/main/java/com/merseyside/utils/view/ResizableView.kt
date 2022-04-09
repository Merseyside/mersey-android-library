package com.merseyside.utils.view

import android.annotation.SuppressLint
import android.graphics.Point
import android.view.MotionEvent
import android.view.View
import com.merseyside.utils.view.ext.getCoordPoint
import com.merseyside.utils.view.ext.isInViewBounds
import com.merseyside.utils.view.ext.isSizeChanged
import com.merseyside.utils.view.ext.setCoordPoint
import com.merseyside.utils.view.ext.isNotEmpty
import com.merseyside.utils.view.ext.minus

class ResizableView(
    private val view: View,
    var enableViewMoving: Boolean = false
) : View.OnTouchListener {

    private val touchViews: MutableList<TouchView> = mutableListOf()
    private var onSizeChangedCallback: (Int, Int) -> Unit = { _, _ -> }
    private var onPositionChangedCallback: (View, Point) -> Boolean = { _, _ -> true }
    private var onStartPositionChangeCallback: (View, Point) -> Unit = { _, _ -> }
    private var onStopResizeCallback: (View) -> Unit = {}
    private var getMovingView: () -> View = { view }

    var size = Point()
        private set(value) {
            field = value
            view.layoutParams = view.layoutParams.apply {
                width = value.x
                height = value.y
            }
            onSizeChangedCallback(value.x, value.y)
        }

    private var oldX = 0F
    private var oldY = 0F

    var minHeight: Int = 0
    var maxHeight: Int = Int.MAX_VALUE

    var minWidth: Int = 0
    var maxWidth: Int = Int.MAX_VALUE

    var touchView: TouchView? = null

    init {
        view.setOnTouchListener(this)
    }

    fun addTouchView(resizeDirection: ResizeDirection, view: View) {
        touchViews.add(TouchView(view, resizeDirection))
    }

    fun removeTouchView(view: View) {
        touchViews.remove(touchViews.find { it.view == view })
    }

    fun setOnSizeChangedCallback(block: (Int, Int) -> Unit) {
        onSizeChangedCallback = block
    }

    fun setOnStartPositionChangeCallback(block: (View, Point) -> Unit) {
        onStartPositionChangeCallback = block
    }

    fun setOnStopPositionChangeCallback(block: (View) -> Unit) {
        onStopResizeCallback = block
    }

    fun setPositionChangeView(block: () -> View) {
        this.getMovingView = block
    }

    fun onPositionChangedCallback(block: (View, Point) -> Boolean) {
        this.onPositionChangedCallback = block
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (touchViews.isNotEmpty()) {
            return when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchView = touchViews.find {
                        with(it.view) {
                            this.isInViewBounds(event) && isTouchable()
                        }
                    }

                    touchView?.let {
                        oldX = event.rawX
                        oldY = event.rawY

                        onStartPositionChangeCallback(getMovingView(), event.getCoordPoint())
                        true
                    } ?: false
                }

                MotionEvent.ACTION_MOVE -> {
                    touchView?.let {
                        val dx = event.rawX - oldX
                        val dy = event.rawY - oldY

                        val layoutParams = view.layoutParams

                        val newSize: Point = if (isVertical(it.resizeDirection)) {
                            var newHeight = when (it.resizeDirection) {
                                ResizeDirection.TOP -> view.measuredHeight - dy.toInt()
                                else -> view.measuredHeight + dy.toInt()
                            }

                            if (newHeight < minHeight) {
                                newHeight = minHeight
                            } else if (newHeight > maxHeight) {
                                newHeight = maxHeight
                            }

                            Point(layoutParams.width, newHeight)
                        } else {
                            var newWidth = when (it.resizeDirection) {
                                ResizeDirection.LEFT -> view.measuredWidth - dx.toInt()
                                else -> view.measuredWidth + dx.toInt()
                            }

                            if (newWidth < minWidth) {
                                newWidth = minWidth
                            } else if (newWidth > maxWidth) {
                                newWidth = maxWidth
                            }

                            Point(newWidth, layoutParams.height)
                        }

                        if (view.isSizeChanged(newSize)) {
                            val ds = if (size.isNotEmpty()) {
                                size - newSize
                            } else {
                                Point(dx.toInt(), dy.toInt())
                            }

                            size = newSize

                            if (enableViewMoving) {
                                getMovingView().let { view ->
                                    var isPositionChanged = true
                                    val positionPoint = view.getCoordPoint()
                                    when (it.resizeDirection) {
                                        ResizeDirection.TOP -> positionPoint.y += ds.y
                                        ResizeDirection.LEFT -> positionPoint.x += ds.x
                                        else -> {
                                            isPositionChanged = false
                                        }
                                    }

                                    if (isPositionChanged) {
                                        val validPosition = onPositionChangedCallback(
                                            view,
                                            view.getCoordPoint()
                                        )

                                        if (validPosition) view.setCoordPoint(positionPoint)
                                    }
                                }
                            }
                        }

                        oldX = event.rawX
                        oldY = event.rawY

                        true
                    } ?: false
                }

                MotionEvent.ACTION_UP -> {
                    onStopResizeCallback(getMovingView())
                    true
                }

                else -> false
            }
        } else return false
    }
}

private fun View.isTouchable(): Boolean {
    return isEnabled
}

private fun isVertical(resizeDirection: ResizeDirection): Boolean {
    return resizeDirection == ResizeDirection.TOP || resizeDirection == ResizeDirection.BOTTOM
}

class TouchView(
    val view: View,
    val resizeDirection: ResizeDirection
)

enum class ResizeDirection { BOTTOM, TOP, LEFT, RIGHT }