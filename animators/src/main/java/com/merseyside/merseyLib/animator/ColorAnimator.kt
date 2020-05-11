package com.merseyside.merseyLib.animator

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import com.merseyside.merseyLib.BaseAnimatorBuilder
import com.merseyside.merseyLib.BaseSingleAnimator
import com.merseyside.merseyLib.utils.ext.setColor
import com.merseyside.merseyLib.utils.time.TimeUnit

class ColorAnimator(
    builder: Builder
) : BaseSingleAnimator(builder) {

    class Builder(
        private val view: View? = null,
        private val drawable: Drawable? = null,
        duration: TimeUnit,
        val propertyName: String = BACKGROUND_COLOR,
        val isUseColorFilter: Boolean = false
    ) : BaseAnimatorBuilder<ColorAnimator>(duration) {

        init {
            if (view == null && drawable == null) throw IllegalArgumentException("Please, pass view or drawable")

            if (drawable != null && propertyName != BACKGROUND_COLOR)
                throw IllegalStateException("You can not pass property name with Drawable")
        }

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

            if (propertyName == BACKGROUND_COLOR) {

                return ValueAnimator.ofArgb(*values).apply {
                    this.duration = duration.toMillisLong()

                    val drawable = getMutableDrawable()

                    addUpdateListener { valueAnimator ->
                        val value = valueAnimator.animatedValue as Int

                        if (!isUseColorFilter) {
                            drawable.setColor(value)
                        } else {
                            drawable.colorFilter = PorterDuffColorFilter(value, PorterDuff.Mode.SRC_IN)
                        }
                    }
                }
            } else {

                return ObjectAnimator.ofObject(
                    view,
                    propertyName,
                    ArgbEvaluator(),
                    *values.toTypedArray()
                ).apply {
                    this.duration = duration.toMillisLong()
                }
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
            getMutableDrawable().let { drawable ->
                if (drawable is ColorDrawable) {
                    return drawable.color
                } else {
                    throw IllegalArgumentException("Background is not ColorDrawable")
                }
            }
        }

        private fun getMutableDrawable(): Drawable {
            return (view?.background ?: drawable!!).mutate()
        }
    }

    companion object {
        private const val BACKGROUND_COLOR = "backgroundColor"
    }

}