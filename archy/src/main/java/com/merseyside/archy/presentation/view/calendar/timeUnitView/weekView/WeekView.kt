package com.merseyside.archy.presentation.view.calendar.timeUnitView.weekView

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.archy.R
import com.merseyside.archy.databinding.ViewWeekTimeUnitBinding
import com.merseyside.archy.presentation.view.calendar.timeUnitView.TimeRangeView
import com.merseyside.archy.presentation.view.calendar.timeUnitView.weekView.recycler.WeekRecyclerView
import com.merseyside.merseyLib.time.ext.toWeekRange
import com.merseyside.merseyLib.time.ranges.WeekRange
import com.merseyside.merseyLib.time.units.TimeUnit
import com.merseyside.utils.delegate.getValue
import com.merseyside.utils.delegate.viewBinding
import com.merseyside.utils.layout.LinearLayoutManager

class WeekView(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
    TimeRangeView<WeekRange>(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, R.attr.timeRangeViewStyle)

    override val binding: ViewWeekTimeUnitBinding by viewBinding(R.layout.view_week_time_unit)
    override val recycler: WeekRecyclerView
        get() = binding.recyclerWeek

    init {
        setupRecycler()
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, HORIZONTAL)
    }


    override fun getTimeRange(time: TimeUnit): WeekRange {
        return time.toWeekRange()
    }

}