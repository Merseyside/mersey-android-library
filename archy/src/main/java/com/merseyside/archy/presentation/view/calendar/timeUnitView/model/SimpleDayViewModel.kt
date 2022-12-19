package com.merseyside.archy.presentation.view.calendar.timeUnitView.model

import androidx.databinding.Bindable
import com.merseyside.adapters.feature.selecting.SelectState
import com.merseyside.adapters.feature.selecting.SelectableItem
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.archy.BR
import com.merseyside.merseyLib.time.ext.isWeekendDay
import com.merseyside.merseyLib.time.ext.toDayOfMonth
import com.merseyside.merseyLib.time.ext.toDayOfWeek
import com.merseyside.merseyLib.time.ranges.TimeRange

open class SimpleDayViewModel(
    item: TimeRange
) : AdapterViewModel<TimeRange>(item), SelectableItem {

    override val selectState: SelectState = SelectState()

    @get:Bindable
    open var isVisible: Boolean = true
        set(value) {
            if (field != value) {
                field = value
                notifyUpdate()
            }
        }

    @get:Bindable val isWeekend: Boolean by lazy {
        item.start.toDayOfWeek().isWeekendDay()
    }

    @get:Bindable
    val monthDay: Int by lazy { item.start.toDayOfMonth().intValue }

    override fun areItemsTheSame(other: TimeRange): Boolean {
        return item.start == other.start
    }

    override fun notifyUpdate() {
        super.notifyUpdate()
        notifyPropertyChanged(BR.visible)
    }
}