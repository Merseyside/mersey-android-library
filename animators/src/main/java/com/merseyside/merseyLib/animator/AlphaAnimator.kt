package com.merseyside.merseyLib.animator

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import com.merseyside.merseyLib.BaseAnimatorBuilder
import com.merseyside.merseyLib.BaseSingleAnimator
import com.merseyside.merseyLib.utils.time.TimeUnit

class AlphaAnimator(
    builder: Builder
) : BaseSingleAnimator(builder) {

    class Builder(
        view: View,
        duration: TimeUnit
    ): BaseAnimatorBuilder<AlphaAnimator>(view, duration) {

        private var values: FloatArray? = null

        fun values(vararg values: Float) {
            this.values = values.toList().toFloatArray()
        }

        private fun alphaAnimation(
            values: FloatArray,
            duration: TimeUnit
        ): Animator {

            values.forEachIndexed { index, value ->
                if (value == getCurrentValue()) {
                    values[index] = calculateCurrentValue()
                }
            }

            val values = when (values.size) {
                1 -> {
                    val list = values.toMutableList().apply {
                        add(0, view.alpha)
                    }

                    list.toFloatArray().also { this.values = it }
                }
                else -> {
                    values
                }
            }

            if (isReverse) values.reverse()

            return ValueAnimator.ofFloat(*values).apply {
                this.duration = duration.toMillisLong()

                var previousValue: Float? = values[0]

                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Float
                    view.alpha = value

                    if (previousValue != value) {

                        if (previousValue == 0f && previousValue?.compareTo(value) == -1) {
                            view.visibility = View.VISIBLE
                        } else if (previousValue?.compareTo(value) == 1 && value == 0f) {
                            view.visibility = View.INVISIBLE
                        }
                    }

                    previousValue = value
                }
            }
        }


        override fun build(): Animator {
            if (values != null) {
                return alphaAnimation(values!!.copyOf(), duration)
            } else {
                throw IllegalArgumentException("Points haven't been set")
            }
        }

        override fun getCurrentValue(): Float {
            return CURRENT_FLOAT
        }

        override fun calculateCurrentValue(): Float {
            return view.alpha
        }
    }
}