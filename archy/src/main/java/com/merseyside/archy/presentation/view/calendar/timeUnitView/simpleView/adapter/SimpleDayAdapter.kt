package com.merseyside.archy.presentation.view.calendar.timeUnitView.simpleView.adapter

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.init.initAdapter
import com.merseyside.adapters.single.SimpleAdapter
import com.merseyside.archy.BR
import com.merseyside.archy.R
import com.merseyside.archy.presentation.view.calendar.timeUnitView.model.SimpleDayViewModel
import com.merseyside.merseyLib.time.ranges.TimeRange

open class SimpleDayAdapter<VM : SimpleDayViewModel>(
    config: AdapterConfig<TimeRange, VM>
) : SimpleAdapter<TimeRange, VM>(config) {

    override fun getLayoutIdForPosition(position: Int) = R.layout.view_simple_day
    override fun getBindingVariable() = BR.model
    override fun createItemViewModel(item: TimeRange): VM = SimpleDayViewModel(item) as VM

    companion object {
        operator fun invoke(
            configure: AdapterConfig<TimeRange, SimpleDayViewModel>.() -> Unit
        ): SimpleDayAdapter<SimpleDayViewModel> {
            return initAdapter(::SimpleDayAdapter, configure)
        }
    }
}