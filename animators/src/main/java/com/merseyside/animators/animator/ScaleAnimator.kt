package com.merseyside.animators.animator

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import com.merseyside.animators.Axis
import com.merseyside.animators.BaseAnimatorBuilder
import com.merseyside.animators.BaseSingleAnimator
import com.merseyside.merseyLib.time.TimeUnit

class ScaleAnimator(
    builder: Builder
) : BaseSingleAnimator(builder) {

    class Builder(
        private val view: View,
        duration: TimeUnit
    ) : BaseAnimatorBuilder<ScaleAnimator, Float>(duration) {

        private var values: FloatArray? = null
        var axis: Axis? = null

        fun values(vararg values: Float) {
            this.values = values.toList().toFloatArray()
        }

        private fun scaleAnimation(
            floats: FloatArray,
            axis: Axis,
            duration: TimeUnit
        ) : Animator {

            floats.forEachIndexed { index, value ->
                if (value == getCurrentValue()) {
                    floats[index] = calculateCurrentValue()
                }
            }

            val values = when (floats.size) {
                1 -> {
                    val list = floats.toMutableList().apply {
                        add(0, calculateCurrentValue())
                    }

                    list.toFloatArray().also { this.values = it }
                }
                else -> {
                    floats
                }
            }

            values.also { if (isReverse) it.reverse() }

            return ValueAnimator.ofFloat(*values).apply {
                this.duration = duration.millis
                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Float

                    when (axis) {
                        Axis.X -> {
                            view.scaleX = value
                        }

                        Axis.Y -> {
                            view.scaleY = value
                        }
                    }

                    onValueCallback(value)
                }
            }
        }

        override fun build(): Animator {

            if (values != null && axis != null) {
                return scaleAnimation(values!!.copyOf(), axis!!, duration)
            } else {
                throw IllegalArgumentException("Points haven't been set")
            }
        }

        override fun getCurrentValue(): Float {
            return CURRENT_FLOAT
        }

        override fun calculateCurrentValue(): Float {
            return when (axis) {
                Axis.X -> {
                    view.scaleX
                }

                Axis.Y -> {
                    view.scaleY
                }

                null -> throw IllegalArgumentException()
            }
        }

    }
}