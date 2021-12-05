package com.merseyside.animators.animator

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import com.merseyside.animators.Axis
import com.merseyside.animators.BaseAnimatorBuilder
import com.merseyside.animators.BaseSingleAnimator
import com.merseyside.merseyLib.kotlin.safeLet
import com.merseyside.merseyLib.time.TimeUnit
import com.merseyside.utils.Logger

class SizeAnimator(
    builder: Builder
) : BaseSingleAnimator(builder) {

    class Builder(
        private val view: View,
        duration: TimeUnit
    ) : BaseAnimatorBuilder<SizeAnimator, Int>(duration) {

        var values: IntArray? = null
        var axis: Axis? = null

        fun setInPercents(values: IntArray, axis: Axis) {
            this.axis = axis
            this.values = getPixelsFromPercents(values, axis)
        }

        fun setInPixels(values: IntArray, axis: Axis) {
            this.axis = axis
            this.values = values
        }

        private fun getPixelsFromPercents(
            percents: IntArray,
            axis: Axis
        ) : IntArray {

            val newValues = IntArray(percents.size)

            val viewSize = when (axis) {
                Axis.X -> {
                    view.width
                }
                Axis.Y -> {
                    view.height
                }
            }

            var i = 0
            while (i < percents.size) {

                newValues[i] = (viewSize * percents[i])
                i++
            }

            Logger.log(this, newValues)

            return newValues
        }

        private fun changeSizeAnimation(
            ints: IntArray,
            axis: Axis,
            duration: TimeUnit
        ) : Animator {

            val array = ints.toMutableList()

            array.forEachIndexed { index, value ->
                if (value == getCurrentValue()) {
                    array[index] = calculateCurrentValue()
                }
            }

            if (ints.size == 1) {
                array.add(0, calculateCurrentValue())
                this.values = array.toIntArray()
            }

            array.also { if (isReverse) it.reverse() }

            return ValueAnimator.ofInt(*array.toIntArray()).apply {
                this.duration = duration.millis

                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int

                    val layoutParams = view.layoutParams
                    when (axis) {
                        Axis.X -> {
                            layoutParams.width = value
                        }

                        Axis.Y -> {
                            layoutParams.height = value
                        }
                    }

                    view.layoutParams = layoutParams
                    onValueCallback(value)
                }

            }
        }

        override fun build(): Animator {
            return safeLet(values, axis) { values, axis ->
                changeSizeAnimation(values.copyOf(), axis, duration)
            } ?: throw IllegalArgumentException("Points haven't been set")
        }

        override fun getCurrentValue(): Int {
            return CURRENT_INT
        }

        override fun calculateCurrentValue(): Int {
            return when (axis) {
                Axis.Y ->
                    view.height
                Axis.X -> {
                    view.width
                }
                null -> throw IllegalArgumentException()
            }
        }
    }
}