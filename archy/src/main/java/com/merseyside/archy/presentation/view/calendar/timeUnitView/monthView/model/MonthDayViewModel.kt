package com.merseyside.archy.presentation.view.calendar.timeUnitView.monthView.model

import com.merseyside.adapters.feature.selecting.SelectState
import com.merseyside.archy.presentation.view.calendar.timeUnitView.model.SimpleDayViewModel
import com.merseyside.merseyLib.time.ranges.TimeRange

open class MonthDayViewModel(
    item: TimeRange,
    var isOutMonthDay: Boolean = false
) : SimpleDayViewModel(item) {

    override val selectState = SelectState(selectable = !isOutMonthDay)
}