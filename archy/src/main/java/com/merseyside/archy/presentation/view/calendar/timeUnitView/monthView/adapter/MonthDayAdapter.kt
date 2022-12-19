package com.merseyside.archy.presentation.view.calendar.timeUnitView.monthView.adapter

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.init.initAdapter
import com.merseyside.archy.R
import com.merseyside.archy.presentation.view.calendar.timeUnitView.monthView.model.MonthDayViewModel
import com.merseyside.archy.presentation.view.calendar.timeUnitView.monthView.model.OutMonthDayViewModel
import com.merseyside.archy.presentation.view.calendar.timeUnitView.simpleView.adapter.SimpleDayAdapter
import com.merseyside.merseyLib.time.ranges.TimeRange

class MonthDayAdapter(
    config: AdapterConfig<TimeRange, MonthDayViewModel>,
    private val isPrevMonthDayVisible: Boolean,
    private val isOutMonthDay: (TimeRange) -> Boolean,
) : SimpleDayAdapter<MonthDayViewModel>(config) {

    override fun getLayoutIdForPosition(position: Int) = R.layout.view_month_day

    override fun createItemViewModel(item: TimeRange): MonthDayViewModel {
        return if (!isOutMonthDay(item)) {
            MonthDayViewModel(item)
        } else {
            OutMonthDayViewModel(item, isPrevMonthDayVisible)
        }
    }

    companion object {
        operator fun invoke(
            isPrevMonthDayVisible: Boolean,
            isPrevMonthDay: (TimeRange) -> Boolean,
            configure: AdapterConfig<TimeRange, MonthDayViewModel>.() -> Unit
        ): MonthDayAdapter {
            return initAdapter(::MonthDayAdapter, isPrevMonthDayVisible, isPrevMonthDay, configure)
        }
    }
}