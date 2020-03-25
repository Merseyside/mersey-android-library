package com.merseyside.merseyLib.animator

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import com.merseyside.merseyLib.Axis
import com.merseyside.merseyLib.BaseAnimatorBuilder
import com.merseyside.merseyLib.BaseSingleAnimator
import com.merseyside.merseyLib.utils.Logger
import com.merseyside.merseyLib.utils.time.TimeUnit

class SizeAnimator(
    builder: Builder
) : BaseSingleAnimator(builder) {

    class Builder(
        view: View,
        duration: TimeUnit
    ) : BaseAnimatorBuilder<SizeAnimator>(view, duration) {

        var values: IntArray? = null
        var axis: Axis? = null

        fun setInPercents(values: IntArray, axis: Axis) {
            this.axis = axis

            this.values = getPixelsFromPercents(values, axis)
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
                this.duration = duration.toMillisLong()

                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int

                    when (axis) {
                        Axis.X -> {
                            view.layoutParams.width = value
                        }

                        Axis.Y -> {
                            view.layoutParams.height = value
                        }
                    }
                }

            }
        }

        override fun build(): Animator {
            if (values != null && axis != null) {
                return changeSizeAnimation(values!!.copyOf(), axis!!, duration)
            } else {
                throw IllegalArgumentException("Points haven't been set")
            }
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