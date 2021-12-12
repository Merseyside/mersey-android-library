package com.merseyside.archy.presentation.view.animatedCheckButton

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageButton
import com.merseyside.animators.AnimatorList
import com.merseyside.animators.Approach
import com.merseyside.archy.R
import com.merseyside.animators.animator.ColorAnimator
import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.utils.ext.onClick
import com.merseyside.utils.ext.setColor
import com.merseyside.utils.time.Millis

class AnimatedCheckImageButton(context: Context, attributeSet: AttributeSet)
    : AppCompatImageButton(context, attributeSet), ICheckableView {

    override var listener: ICheckableView.OnCheckedListener? = null
    private var animator: AnimatorList? = null

    private var isInitialized: Boolean = false
    private var isChecked: Boolean = false

    override var isCheckable: Boolean = true

    private var duration = Millis(400)

    private var isClickableWhenTransition = false

    @ColorInt
    private var color: Int = Color.WHITE

    @ColorInt
    private var middleColor: Int? = null
        private set(value) {
            field = if (value == NO_VALUE) {
                null
            } else {
                value
            }
        }

    @ColorInt
    private var checkedColor: Int = NO_VALUE

    @ColorInt
    private var defaultImageColor: Int = NO_VALUE

    @ColorInt
    private var checkedImageColor: Int = NO_VALUE

    init {
        loadAttrs(attributeSet)
        doLayout()
    }

    private fun loadAttrs(attributeSet: AttributeSet) {
        val array = context.theme.obtainStyledAttributes(attributeSet, R.styleable.AnimatedCheckImageButton, 0, 0)

        isCheckable = array.getBoolean(R.styleable.AnimatedCheckImageButton_android_checkable, isCheckable)
        color = array.getColor(R.styleable.AnimatedCheckImageButton_color, Color.WHITE)
        middleColor = array.getColor(R.styleable.AnimatedCheckImageButton_middleColor, NO_VALUE)
        checkedColor = array.getColor(R.styleable.AnimatedCheckImageButton_checkedColor, NO_VALUE)
        duration = Millis(array.getInteger(R.styleable.AnimatedCheckImageButton_duration, 400).toLong())
        isChecked = array.getBoolean(R.styleable.AnimatedCheckImageButton_android_checked, false)
        checkedImageColor = array.getColor(R.styleable.AnimatedCheckImageButton_checkedImageColor, NO_VALUE)
        defaultImageColor = array.getColor(R.styleable.AnimatedCheckImageButton_defaultImageColor, NO_VALUE)

        array.recycle()
    }

    private fun doLayout() {
        setForceChecked(isChecked)

        onClick {
            if (isCheckable) {
                isChecked = !isChecked
                changeState()
            }
        }

        isInitialized = true
    }

    private fun changeState() {
        listener?.onChecked(isChecked)

        animateState()
    }

    private fun setBackground(@ColorInt color: Int) {
        if (color != NO_VALUE) {
            val drawable = background.mutate()
            Logger.log(this, drawable)

            drawable.setColor(color)
        }
    }

    private fun setImageColor(@ColorInt color: Int) {
        if (color != NO_VALUE) {
            colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    private fun animateState() {

        if (animator == null) {

            animator = AnimatorList(Approach.TOGETHER)
                .apply {
                setLegacyReverse(true)

                if (color != NO_VALUE && checkedColor != NO_VALUE) {
                    addAnimator(
                        ColorAnimator(
                            ColorAnimator.Builder(
                                view = this@AnimatedCheckImageButton,
                                duration = duration
                            ).apply {
                                values(
                                    *listOf(color, middleColor, checkedColor).filterNotNull()
                                        .toIntArray()
                                )
                            })
                    )
                }

                if (defaultImageColor != NO_VALUE && checkedImageColor != NO_VALUE) {
                    addAnimator(
                        ColorAnimator(
                            ColorAnimator.Builder(
                                drawable = drawable,
                                duration = duration,
                                isUseColorFilter = true
                            ).apply {
                                values(defaultImageColor, checkedImageColor)
                            })
                    )
                }
            }
        }

        if (!isClickableWhenTransition) {
            isClickable = false
            animator!!.setOnEndCallback { _, _ ->
                isClickable = true
            }
        }

        if (isChecked) {
            animator!!.start()
        } else {
            animator!!.reverse()
        }
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun setChecked(isChecked: Boolean) {
        this.isChecked = isChecked

        if (isInitialized) {
            changeState()
        }
    }

    override fun setForceChecked(isChecked: Boolean) {
        this.isChecked = isChecked

        if (isChecked) {
            setBackground(checkedColor)
            setImageColor(checkedImageColor)

        } else {
            setBackground(color)
            setImageColor(defaultImageColor)
        }
    }

    fun setDefaultColor(@ColorInt color: Int) {
        this.color = color

        animator = null
    }

    fun setMiddleColor(@ColorInt color: Int) {
        this.middleColor = color
    }

    fun setCheckedColor(@ColorInt color: Int) {
        this.checkedColor = color

        animator = null
    }

    fun setDefaultTextColor(@ColorInt color: Int) {
        this.defaultImageColor = color

        animator = null
    }

    fun setCheckedTextColor(@ColorInt color: Int) {
        this.checkedImageColor = color

        animator = null
    }

    fun setClickableWhenTransition(isClickable: Boolean) {
        this.isClickableWhenTransition = isClickable
    }

    companion object {
        private const val NO_VALUE = -2
    }
}