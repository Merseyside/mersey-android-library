package com.merseyside.animators.animator

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewTreeObserver
import com.merseyside.animators.Axis
import com.merseyside.animators.BaseAnimatorBuilder
import com.merseyside.animators.BaseSingleAnimator
import com.merseyside.animators.MainPoint
import com.merseyside.merseyLib.kotlin.safeLet
import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.merseyLib.time.units.TimeUnit

class TransitionAnimator (
    builder: Builder
): BaseSingleAnimator(builder) {

    class Builder(
        private val view: View,
        duration: TimeUnit
    ): BaseAnimatorBuilder<TransitionAnimator, Float>(duration) {

        private var viewWidth: Float = -1F
        private var viewHeight: Float = -1F

        init {
            if (view.visibility == View.GONE) {
                view.visibility = View.INVISIBLE

                view.viewTreeObserver.addOnGlobalLayoutListener(
                    object: ViewTreeObserver.OnGlobalLayoutListener {

                        override fun onGlobalLayout() {
                            viewHeight = view.height.toFloat()
                            viewWidth = view.width.toFloat()

                            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            view.visibility = View.GONE
                        }

                    })
            } else {
                viewWidth = view.width.toFloat()
                viewHeight = view.height.toFloat()
            }
        }

        private val current by lazy { CURRENT_FLOAT to MainPoint.TOP_LEFT }

        private var pointList: List<Pair<Float, MainPoint>>? = null
        private var axis: Axis? = null
        var isLogValues = false

        fun setInPercents(vararg pointPercents: Pair<Float, MainPoint>, axis: Axis) {
            this.axis = axis
            pointList = getPixelsFromPercents(pointPercents.toList())
        }

        fun setInPixels(vararg pointPixels: Pair<Float, MainPoint>, axis: Axis) {
            this.axis = axis
            pointList = pointPixels.toList()
        }

        private fun translateAnimation(
            pointFloats: List<Pair<Float, MainPoint>>,
            axis: Axis,
            duration: TimeUnit
        ) : Animator {

            val floatArray = pointFloats.toMutableList().let { list ->

                pointFloats.forEachIndexed { index, value ->
                    if (value == getCurrentValue()) {
                        list[index] = calculateCurrentValue()
                    }
                }

                if (pointFloats.size == 1) {
                    list.add(0, calculateCurrentValue())

                    pointList = list
                }

                recalculateValues(list, axis).also {
                    if (isReverse) it.reverse()
                }
            }

            if (isLogValues) {
                Logger.log(this, floatArray.joinToString())
            }

            return ValueAnimator.ofFloat(*floatArray).apply {
                this.duration = duration.millis

                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Float

                    if (isLogValues) {
                        Logger.log(this@Builder, value)
                    }
                    when (axis) {
                        Axis.X ->
                            view.x = value

                        Axis.Y ->
                            view.y = value
                    }

                    onValueCallback(value)
                }
            }
        }

        private fun getPixelsFromPercents(
            pointPercents: List<Pair<Float, MainPoint>>
        ): List<Pair<Float, MainPoint>> {

            val parentViewSize = when (axis!!) {
                Axis.X -> (view.parent as View).width
                Axis.Y -> (view.parent as View).height
            }

            return pointPercents.toMutableList().let { list ->
                var i = 0

                while (i < pointPercents.size) {

                    if (list[i] != getCurrentValue()) {
                        list[i] = (parentViewSize * list[i].first) to list[i].second
                    }

                    i++
                }

                list
            }
        }

        private fun recalculateValues(values: MutableList<Pair<Float, MainPoint>>, axis: Axis) : FloatArray {

            val viewSize = when (axis) {
                Axis.X -> viewWidth
                Axis.Y -> viewHeight
            }

            val floatArray = FloatArray(values.size)

            var i = 0
            while (i < values.size) {
                val value = values[i].first

                when (values[i].second) {

                    MainPoint.CENTER -> {
                        floatArray[i] = value - viewSize / 2
                    }

                    MainPoint.TOP_RIGHT -> {
                        when (axis) {
                            Axis.X -> floatArray[i] = value + viewSize
                            Axis.Y -> {}
                        }
                    }

                    MainPoint.BOTTOM_LEFT -> {
                        when (axis) {
                            Axis.X -> {}
                            Axis.Y -> floatArray[i] = value - viewSize
                        }
                    }

                    MainPoint.BOTTOM_RIGHT -> {
                        when (axis) {
                            Axis.X -> floatArray[i] = value - viewSize
                            Axis.Y -> floatArray[i] = value - viewSize
                        }
                    }

                    else -> {
                        floatArray[i] = value
                    }
                }

                i++
            }

            return floatArray
        }

        override fun getCurrentValue(): Pair<Float, MainPoint> {
            return current
        }

        override fun calculateCurrentValue(): Pair<Float, MainPoint> {
            return when (axis) {
                Axis.Y -> view.y to MainPoint.TOP_LEFT
                Axis.X -> view.x to MainPoint.TOP_LEFT
                null -> throw NullPointerException()
            }
        }

        @Throws(IllegalArgumentException::class)
        override fun build(): Animator {
            safeLet(pointList, axis) { pointList, axis ->
                return translateAnimation(pointList, axis, duration)
            } ?: throw IllegalArgumentException("Points haven't been set")
        }
    }
}