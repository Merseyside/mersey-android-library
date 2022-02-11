package com.merseyside.archy.presentation.view.animatedCheckButton

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatButton
import com.merseyside.archy.R
import com.merseyside.animators.AnimatorList
import com.merseyside.animators.Approach
import com.merseyside.animators.animator.ColorAnimator
import com.merseyside.merseyLib.kotlin.extensions.forEachNotNull
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty
import com.merseyside.merseyLib.time.units.Millis
import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.utils.ext.*

class AnimatedCheckButton(context: Context, attributeSet: AttributeSet)
    : AppCompatButton(context, attributeSet), ICheckableView {

    override var listener: ICheckableView.OnCheckedListener? = null
    private var animator: AnimatorList? = null

    private var defaultText: String? = null
    private var checkedText: String? = null

    private var isInitialized: Boolean = false
    private var isChecked: Boolean = false

    override var isCheckable: Boolean = true

    private var isClickableWhenTransition = false

    private var onClick: (View) -> Unit = {}

    @ColorInt
    private var color: Int = NO_VALUE

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
    private var defaultTextColor: Int = NO_VALUE

    @ColorInt
    private var checkedTextColor: Int = NO_VALUE

    init {
        loadAttrs(attributeSet)
        doLayout()
    }

    private fun loadAttrs(attributeSet: AttributeSet) {
        val array = context.theme.obtainStyledAttributes(attributeSet, R.styleable.AnimatedCheckButton, 0, 0)

        isCheckable = array.getBoolean(R.styleable.AnimatedCheckButton_android_checkable, isCheckable)
        color = array.getColor(R.styleable.AnimatedCheckButton_color, NO_VALUE)
        middleColor = array.getColor(R.styleable.AnimatedCheckButton_middleColor, NO_VALUE)
        checkedColor = array.getColor(R.styleable.AnimatedCheckButton_checkedColor, NO_VALUE)
        duration = Millis(array.getInteger(R.styleable.AnimatedCheckButton_duration, 400).toLong())
        isChecked = array.getBoolean(R.styleable.AnimatedCheckButton_android_checked, false)
        checkedTextColor = array.getColor(R.styleable.AnimatedCheckButton_checkedTextColor, NO_VALUE)
        defaultTextColor = array.getColor(R.styleable.AnimatedCheckButton_defaultTextColor, NO_VALUE)
        defaultText = array.getString(R.styleable.AnimatedCheckButton_defaultText)
        checkedText = array.getString(R.styleable.AnimatedCheckButton_checkedText)

        array.recycle()
    }

    private fun doLayout() {
        setForceChecked(isChecked)

        onClick {
            onClick.invoke(this)

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

    private fun animateState() {
        if (animator == null) {

            animator = AnimatorList(Approach.TOGETHER)
                .apply {
                setLegacyReverse(true)

                if (color != NO_VALUE && checkedColor != NO_VALUE) {
                    addAnimator(
                        ColorAnimator(
                            ColorAnimator.Builder(
                                view = this@AnimatedCheckButton,
                                duration = duration
                            ).apply {
                                values(
                                    *listOfNotNull(color, middleColor, checkedColor)
                                        .toIntArray()
                                )
                            })
                    )
                }

                if (defaultTextColor != NO_VALUE && checkedTextColor != NO_VALUE) {
                    addAnimator(
                        ColorAnimator(
                            ColorAnimator.Builder(
                                view = this@AnimatedCheckButton,
                                duration = duration,
                                propertyName = "textColor"
                            ).apply {
                                values(defaultTextColor, checkedTextColor)
                            })
                    )


                    getDrawables().forEachNotNull { drawable ->
                        addAnimator(
                            ColorAnimator(
                                ColorAnimator.Builder(
                                    drawable = drawable,
                                    duration = duration,
                                    isUseColorFilter = true
                                ).apply {
                                    values(defaultTextColor, checkedTextColor)
                                })
                        )
                    }
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
            if (checkedText != null) {
                text = checkedText
            }

            animator!!.start()
        } else {
            if (defaultText != null) {
                text = defaultText
            }

            animator!!.reverse()
        }
    }

    private fun getDrawables(): List<Drawable> {
        return compoundDrawablesRelative.toList()
    }

    private fun setCompoundDrawablesColor(@ColorInt color: Int) {
        getDrawables().forEachNotNull { compoundDrawable ->
            compoundDrawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
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

        text = if (isChecked) {
            setBackground(checkedColor)
            if (checkedTextColor != NO_VALUE) {
                setTextColor(checkedTextColor)
                setCompoundDrawablesColor(checkedTextColor)
            }

            if (checkedText.isNotNullAndEmpty()) checkedText else text

        } else {
            setBackground(color)

            if (color != NO_VALUE) {
                setTextColor(defaultTextColor)
                setCompoundDrawablesColor(defaultTextColor)
            }

            if (defaultText.isNotNullAndEmpty()) defaultText else text
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
        this.defaultTextColor = color

        animator = null
    }

    fun setCheckedTextColor(@ColorInt color: Int) {
        this.checkedTextColor = color

        animator = null
    }

    fun setDefaultText(text: String) {
        this.defaultText = text
    }

    fun setCheckedText(text: String) {
        this.checkedText = text
    }

    fun setClickableWhenTransition(isClickable: Boolean) {
        this.isClickableWhenTransition = isClickable
    }

    fun setOnClick(click: (View) -> Unit) {
        this.onClick = click
    }

    companion object {
        private const val NO_VALUE = -2

        private var duration = Millis(400)
    }
}