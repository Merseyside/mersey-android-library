package com.merseyside.archy.presentation.view.calendar.timeUnitView.monthView.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.merseyside.archy.R
import com.merseyside.archy.presentation.view.calendar.timeUnitView.model.SimpleDayViewModel
import com.merseyside.archy.presentation.view.calendar.timeUnitView.monthView.model.MonthDayViewModel
import com.merseyside.archy.presentation.view.calendar.timeUnitView.simpleView.view.SimpleDayView
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.delegate.bool
import com.merseyside.utils.delegate.colorStateList

class MonthDayView(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int
) : SimpleDayView(context, attributeSet, defStyleAttr) {

    private val attrs = AttributeHelper(
        context,
        attributeSet,
        R.styleable.MonthDayView,
        "MonthDayView",
        defStyleAttr,
        0,
        "month"
    )

    private var isOutMonthDay by attrs.bool()
    private val prevDayTextColor by attrs.colorStateList()
    private val prevDayBackgroundColor by attrs.colorStateList()

    override val backgroundColor: ColorStateList
        get() {
            return if (isOutMonthDay) {
                prevDayBackgroundColor
            } else super.backgroundColor
        }

    override val textColor: ColorStateList
        get() {
            return if (isOutMonthDay) {
                prevDayTextColor
            } else super.textColor
        }

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, R.attr.monthDayViewStyle)

    fun setOutOfMonth(isOut: Boolean) {
        isOutMonthDay = isOut
    }

    override fun applyModel(model: SimpleDayViewModel) {
        super.applyModel(model)
        model as MonthDayViewModel
        setOutOfMonth(model.isOutMonthDay)
    }
}