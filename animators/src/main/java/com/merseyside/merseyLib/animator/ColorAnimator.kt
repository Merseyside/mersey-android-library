package com.merseyside.merseyLib.animator

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import com.merseyside.merseyLib.BaseAnimatorBuilder
import com.merseyside.merseyLib.BaseSingleAnimator
import com.merseyside.merseyLib.utils.time.TimeUnit

class ColorAnimator(
    builder: Builder
) : BaseSingleAnimator(builder) {

    class Builder(
        view: View,
        duration: TimeUnit
    ) : BaseAnimatorBuilder<ColorAnimator>(view, duration) {

        var values: IntArray? = null

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        fun rgbsAnimation(
            ints: IntArray,
            duration: TimeUnit
        ): Animator {

            ints.forEachIndexed { index, value ->
                if (value == getCurrentValue()) {
                    ints[index] = calculateCurrentValue()
                }
            }

            val values = if (ints.size == 1) {
                ints.toMutableList().apply {
                    add(0, calculateCurrentValue())
                }.toIntArray().also {
                    this.values = it
                }

            } else {
                ints
            }

            values.also { if (isReverse) it.reverse() }

            return ValueAnimator.ofArgb(*values).apply {
                this.duration = duration.toMillisLong()

                    addUpdateListener { valueAnimator ->
                        val value = valueAnimator.animatedValue as Int

                        view.setBackgroundColor(value)
                    }
                }
            }

        override fun build(): Animator {
            if (values != null) {
                return rgbsAnimation(values!!.copyOf(), duration)
            } else {
                throw IllegalArgumentException("Points haven't been set")
            }
        }

        override fun getCurrentValue(): Int {
            return CURRENT_INT
        }

        override fun calculateCurrentValue(): Int {
            val background: Drawable = view.background
            if (background is ColorDrawable) {
                return background.color
            } else {
                throw IllegalArgumentException("Background is not ColorDrawable")
            }
        }
    }

}