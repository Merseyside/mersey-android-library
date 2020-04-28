package com.merseyside.merseyLib.animator

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import com.merseyside.merseyLib.BaseAnimatorBuilder
import com.merseyside.merseyLib.BaseSingleAnimator
import com.merseyside.merseyLib.utils.Logger
import com.merseyside.merseyLib.utils.ext.log
import com.merseyside.merseyLib.utils.time.TimeUnit

class ColorAnimator(
    builder: Builder
) : BaseSingleAnimator(builder) {

    class Builder(
        view: View,
        duration: TimeUnit,
        private val propertyName: String = "backgroundColor"
    ) : BaseAnimatorBuilder<ColorAnimator>(view, duration) {

        fun values(@ColorInt vararg colors: Int) {
            this.values = colors
        }

        private var values: IntArray? = null

        @SuppressLint("ObjectAnimatorBinding")
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun rgbsAnimation(
            ints: IntArray,
            duration: TimeUnit,
            propertyName: String
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

            return ObjectAnimator.ofObject(view, propertyName, ArgbEvaluator(), *values.toTypedArray()).apply {
                this.duration = duration.toMillisLong()
            }
        }

        override fun build(): Animator {
            if (values != null) {
                return rgbsAnimation(values!!.copyOf(), duration, propertyName)
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