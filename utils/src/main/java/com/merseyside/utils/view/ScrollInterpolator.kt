package com.merseyside.utils.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Point
import android.view.animation.DecelerateInterpolator
import com.merseyside.utils.view.ext.*
import kotlin.math.abs

class ScrollInterpolator(
    var maxWidth: Int,
    var maxHeight: Int
) {

    private var onNewValueCallback: (Point) -> Unit = {}
    private var timeCoef: Float = 1F
    private var speedScrollCoef: Float = 1F
    private var animator: Animator? = null

    fun setTimeCoef(coef: Float) {
        timeCoef = coef
    }

    fun setSpeedScrollCoed(coef: Float) {
        speedScrollCoef = coef
    }

    fun startScrolling(first: Point, last: Point) {
        if (first != last) {
            val delta = last - first
            if (abs(delta.x) > 12 || abs(delta.y) > 12) {
                var endPoint = (delta * MULTIPLIER * speedScrollCoef) + last
                if (endPoint.x < 0) endPoint = Point(0, endPoint.y)
                else if (endPoint.x > maxWidth) endPoint = Point(maxWidth, endPoint.y)

                if (endPoint.y < 0) endPoint = Point(endPoint.x, 0)
                else if (endPoint.y > maxHeight) endPoint = Point(endPoint.x, maxHeight)

                val absDelta = delta.abs()
                val maxDelta = absDelta.max()

                val isXCoordMax = absDelta.x == maxDelta

                val from: Int
                val to: Int

                if (isXCoordMax) {
                    from = last.x
                    to = endPoint.x
                } else {
                    from = last.y
                    to = endPoint.y
                }

                val animationTime =
                    ((TIME_MILLIS_MULTIPLIER * maxDelta) * timeCoef).toLong()

                animator = ValueAnimator.ofInt(from, to).apply {
                    duration = animationTime

                    addUpdateListener { valueAnimator ->
                        val value = valueAnimator.animatedValue as Int

                        if (isXCoordMax) {
                            if (value in 1 until maxWidth) {
                                onNewValueCallback(
                                    Point(
                                        value,
                                        calculateSecondValue(
                                            isXCoordMax,
                                            delta,
                                            value,
                                            last,
                                            from,
                                            to,
                                            endPoint
                                        )
                                    )
                                )
                            } else {
                                animator?.cancel()
                            }
                        } else {
                            if (value in 1 until maxHeight) {
                                onNewValueCallback(
                                    Point(
                                        calculateSecondValue(
                                            isXCoordMax,
                                            delta,
                                            value,
                                            last,
                                            from,
                                            to,
                                            endPoint
                                        ), value
                                    )
                                )
                            } else {
                                animator?.cancel()
                            }
                        }
                    }

                    interpolator = DecelerateInterpolator()
                    start()
                }
            }
        }
    }

    private fun calculateSecondValue(
        isXMaxCoord: Boolean,
        delta: Point,
        animatorValue: Int,
        lastPoint: Point,
        from: Int,
        to: Int,
        endPoint: Point
    ): Int {
        val endPointDifference = abs(from - to)
        val animatorDifference = abs(from - animatorValue)

        val ratio = animatorDifference / endPointDifference.toFloat()
        return if (isXMaxCoord) {
            if (delta.y == 0) return lastPoint.y

            val coordDifference = abs(lastPoint.y - endPoint.y)
            val secondCoordPossibleDifference = (coordDifference * ratio).toInt()

            val possibleNewCoord = if (delta.y > 0) lastPoint.y + secondCoordPossibleDifference
            else lastPoint.y - secondCoordPossibleDifference
            when {
                possibleNewCoord in 0 until maxHeight -> possibleNewCoord
                possibleNewCoord < 0 -> 0
                else -> maxHeight
            }
        } else {
            if (delta.x == 0) return lastPoint.x

            val coordDifference = abs(lastPoint.x - endPoint.x)
            val secondCoordPossibleDifference = (coordDifference * ratio).toInt()

            val possibleNewCoord = if (delta.x > 0) lastPoint.x + secondCoordPossibleDifference
            else lastPoint.x - secondCoordPossibleDifference
            when {
                possibleNewCoord in 0 until maxWidth -> possibleNewCoord
                possibleNewCoord < 0 -> 0
                else -> maxWidth
            }
        }
    }

    fun stopScrolling() {
        animator?.cancel()
    }

    fun setOnNewValueCallback(callback: (Point) -> Unit) {
        onNewValueCallback = callback
    }

    companion object {
        private const val TIME_MILLIS_MULTIPLIER = 12
        private const val MULTIPLIER = 30
    }
}