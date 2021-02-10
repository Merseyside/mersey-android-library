package com.merseyside.animators.animator

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.merseyside.animators.BaseAnimatorBuilder
import com.merseyside.animators.BaseSingleAnimator
import com.merseyside.utils.time.TimeUnit
import com.merseyside.utils.time.toMillis

class AlphaAnimator(
    builder: Builder
) : BaseSingleAnimator(builder) {

    class Builder(
        val view: View,
        duration: TimeUnit,
        private val endVisibilityState: Int = INVISIBLE
    ): BaseAnimatorBuilder<AlphaAnimator>(duration) {

        constructor(
            view: View,
            durationMillis: Long,
            endVisibilityState: Int = INVISIBLE
        ): this(view, durationMillis.toMillis(), endVisibilityState)

        private var values: FloatArray? = null

        private var visibilityCallback: OnVisibilityChangeCallback? = null

        fun values(vararg values: Float) {
            this.values = values.toList().toFloatArray()
        }

        fun setOnVisibilityChangeCallback(callback: OnVisibilityChangeCallback) {
            this.visibilityCallback = callback
        }

        fun setOnVisibilityChangeCallback(onChange: (state: Int) -> Unit) {
            this.visibilityCallback = object: OnVisibilityChangeCallback {
                override fun onChange(state: Int) {
                    onChange.invoke(state)
                }
            }
        }

        fun removeOnVisibilityChangeCallback() {
            visibilityCallback = null
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

                var previousValue: Float = values[0]

                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Float
                    view.alpha = value

                    if (previousValue != value) {
                        if (previousValue > 0f && previousValue < value && view.visibility != VISIBLE) {
                            view.visibility = VISIBLE.also { visibilityCallback?.onChange(it) }
                        } else if (previousValue > value && value == 0f) {
                            view.visibility = endVisibilityState.also { visibilityCallback?.onChange(it) }
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

    interface OnVisibilityChangeCallback {
        fun onChange(state: Int)
    }
}