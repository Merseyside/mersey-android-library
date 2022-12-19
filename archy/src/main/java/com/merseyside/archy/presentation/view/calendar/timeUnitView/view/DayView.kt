package com.merseyside.archy.presentation.view.calendar.timeUnitView.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.CallSuper
import com.merseyside.archy.R
import com.merseyside.merseyLib.kotlin.extensions.isNotZero
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.delegate.*
import com.merseyside.utils.ext.getColorByCurrentState
import com.merseyside.utils.view.ext.set
import kotlin.math.min
import android.R.attr.*
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.merseyLib.kotlin.logger.logMsg
import com.merseyside.utils.view.measure.getSquaredViewSizes
import com.merseyside.utils.view.measure.logMeasureSpec

abstract class DayView(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int
) : View(context, attributeSet, defStyleAttr) {

    private val attrs = AttributeHelper(
        context,
        attributeSet,
        R.styleable.DayView,
        "DayView",
        defStyleAttr,
        0,
        "day"
    )

    protected var dayNumber by attrs.int(defaultValue = 0, resName = "")

    protected var textSize by attrs.dimension()
    private val defaultTextColor by attrs.colorStateList(resName = "textColor")
    private val defaultBackgroundColor by attrs.colorStateList(resName = "backgroundColor")

    open val textColor by lazy {
        weekendTextColor.log("kek")
        if (isWeekend && weekendTextColor != null) {
            weekendTextColor!!
        } else defaultTextColor
    }
    open val backgroundColor by lazy {
        if (isWeekend && weekendBackgroundColor != null) {
            weekendBackgroundColor!!
        } else defaultBackgroundColor
    }

    protected var isWeekend by attrs.bool()
    protected val weekendTextColor by attrs.colorStateListOrNull()
    protected val weekendBackgroundColor by attrs.colorStateListOrNull()

    protected var cornerRadius by attrs.dimensionPixelSize()
    protected var contentPadding by attrs.dimensionPixelSize()

    protected var cropCircle by attrs.bool()


    protected val backgroundPaint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = getColorByCurrentState(backgroundColor, VIEW_STATES)
        }
    }

    protected val textPaint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            color = getColorByCurrentState(textColor, VIEW_STATES)
            textSize = this@DayView.textSize
        }
    }

    // whole view rect excluding paddings
    val viewRect: Rect by lazy { Rect().apply(::calculateViewRect) }
    val contentRect: Rect by lazy { Rect(viewRect).apply(::calculateContentRect) }

    fun setDay(day: Int) {
        dayNumber = day
        invalidate()
    }

    /**
     * Must be called after view measured!
     */
    open fun calculateViewRect(rect: Rect) {
        with(rect) {
            set(
                Point(paddingLeft, paddingTop),
                Point(width - paddingRight, height - paddingBottom)
            )
        }
    }

    open fun calculateContentRect(rect: Rect) {
        with(rect) {
            if (contentPadding.isNotZero()) {
                inset(contentPadding, contentPadding)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width: Int
        var height: Int

//        logMeasureSpec(widthMeasureSpec, "width")
//        logMeasureSpec(heightMeasureSpec, "height")
//        logMsg("MeasureSpec", "<--->")

        if (!cropCircle) {
            width = measureWithDesiredSize(widthMeasureSpec, ::getDesiredWidth)
            height = measureWithDesiredSize(heightMeasureSpec, ::getDesiredHeight)
        } else {
            val desiredWidthMeasureSpec =
                makeMeasureSpecWithDesiredSize(widthMeasureSpec, ::getDesiredWidth)
            val desiredHeightMeasureSpec =
                makeMeasureSpecWithDesiredSize(heightMeasureSpec, ::getDesiredHeight)

            try {
                val pair = getSquaredViewSizes(desiredWidthMeasureSpec, desiredHeightMeasureSpec)

                width = pair.first
                height = pair.second

            } catch (e: IllegalArgumentException) {
                width = MeasureSpec.getSize(desiredWidthMeasureSpec)
                height = MeasureSpec.getSize(desiredHeightMeasureSpec)
            }

            cornerRadius = width / 2
        }

        onMeasured(width, height)
    }

    abstract fun getDesiredWidth(measureSpec: Int): Int
    abstract fun getDesiredHeight(measureSpec: Int): Int

    /* Call it only in onMeasure() */
    @CallSuper
    open fun onMeasured(measuredWidth: Int, measuredHeight: Int) {
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    open fun Canvas.drawBackground(rect: Rect, paint: Paint) {
        drawBackgroundRect(rect, paint)
    }

    abstract fun Canvas.drawText(rect: Rect, paint: Paint)
    abstract fun Canvas.drawForeground()

    abstract fun Canvas.drawBackgroundRect(rect: Rect, paint: Paint)

    final override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isValid()) {
            with(canvas) {
                drawBackground(viewRect, backgroundPaint)
                drawText(contentRect, textPaint)
                drawForeground()
            }
        }
    }

    private fun isValid(): Boolean {
        return dayNumber in 1..31
    }

    private fun updateTextPaint() {
        textPaint.color = getColorByCurrentState(textColor, VIEW_STATES)
    }

    private fun updateBackgroundPaint() {
        backgroundPaint.color = getColorByCurrentState(backgroundColor, VIEW_STATES)
    }

    /**
     * Overrides view's state setters in order to change paints colors from ColorStateLists
     */
    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        onStateChanged()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        onStateChanged()
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        onStateChanged()
    }


    private fun onStateChanged() {
        updateBackgroundPaint()
        updateTextPaint()
    }

    companion object {
        //Handle only this view's states
        private val VIEW_STATES =
            intArrayOf(state_hovered, state_pressed, state_selected, state_enabled)
    }

    private fun measureWithDesiredSize(
        measureSpec: Int,
        desireSizeBlock: (measureSpec: Int) -> Int
    ): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)

        return when (mode) {
            MeasureSpec.EXACTLY -> size
            MeasureSpec.AT_MOST -> min(desireSizeBlock(measureSpec), size)
            MeasureSpec.UNSPECIFIED -> desireSizeBlock(measureSpec)

            else -> throw IllegalArgumentException()
        }
    }

    private fun makeMeasureSpecWithDesiredSize(
        measureSpec: Int,
        desireSizeBlock: (measureSpec: Int) -> Int
    ): Int {

        return MeasureSpec.makeMeasureSpec(
            measureWithDesiredSize(measureSpec, desireSizeBlock),
            MeasureSpec.getMode(measureSpec)
        )
    }

    protected open fun getBackgroundColor() {

    }
}