package com.merseyside.archy.presentation.view.calendar.timeUnitView.simpleView.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.core.graphics.toRectF
import androidx.core.view.isVisible
import com.merseyside.archy.R
import com.merseyside.archy.presentation.view.calendar.timeUnitView.model.SimpleDayViewModel
import com.merseyside.archy.presentation.view.calendar.timeUnitView.view.DayView
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.utils.view.canvas.ext.drawTextCenter
import com.merseyside.utils.view.ext.getTextHeight
import com.merseyside.utils.view.ext.getTextWidth

open class SimpleDayView(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int
) : DayView(context, attributeSet, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, R.attr.dayViewStyle)

    override fun getDesiredWidth(measureSpec: Int): Int {
        return (contentPadding * 2 + textPaint.getTextWidth("22"))
    }

    override fun getDesiredHeight(measureSpec: Int): Int {
        return contentPadding * 2 + textPaint.getTextHeight("22")
    }

    override fun Canvas.drawText(rect: Rect, paint: Paint) {
        drawTextCenter(rect, paint, dayNumber.toString())
    }

    override fun Canvas.drawForeground() {}

    override fun Canvas.drawBackgroundRect(rect: Rect, paint: Paint) {
        drawRoundRect(rect.toRectF(), cornerRadius.toFloat(), cornerRadius.toFloat(), paint)
    }

    open fun applyModel(model: SimpleDayViewModel) {
        isWeekend = model.isWeekend
        setDay(model.monthDay)
        isVisible = model.isVisible
    }
}