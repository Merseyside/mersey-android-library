package com.merseyside.archy.presentation.view.calendar.timeUnitView

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.viewbinding.ViewBinding
import com.merseyside.adapters.config.ext.getAdapterSelect
import com.merseyside.adapters.decorator.SimpleItemOffsetDecorator
import com.merseyside.adapters.feature.selecting.AdapterSelect
import com.merseyside.adapters.feature.selecting.Selecting
import com.merseyside.adapters.interfaces.ext.addOrUpdateAsync
import com.merseyside.archy.R
import com.merseyside.merseyLib.kotlin.utils.safeLet
import com.merseyside.merseyLib.time.Time
import com.merseyside.merseyLib.time.ext.toDayRanges
import com.merseyside.merseyLib.time.ranges.TimeRange
import com.merseyside.merseyLib.time.units.TimeUnit
import com.merseyside.utils.attributes.AttributeHelper
import com.merseyside.utils.delegate.dimensionPixelSize
import ru.izibook.view.schedule1.timeUnitView.timeUnitRecyclerView.TimeUnitRecyclerView
import com.merseyside.adapters.feature.selecting.ext.selectFirstIf
import com.merseyside.archy.presentation.view.calendar.timeUnitView.model.SimpleDayViewModel
import com.merseyside.archy.presentation.view.calendar.timeUnitView.simpleView.adapter.SimpleDayAdapter
import com.merseyside.merseyLib.time.ext.contains

abstract class TimeRangeView<TR : TimeRange>(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private val attrs = AttributeHelper(
        context,
        attributeSet,
        R.styleable.TimeRangeView,
        "TimeRangeView",
        defStyleAttr,
        0,
        "day"
    )

    private val horizontalSpacing by attrs.dimensionPixelSize()
    private val verticalSpacing by attrs.dimensionPixelSize()

    abstract val binding: ViewBinding
    abstract val recycler: TimeUnitRecyclerView

    open val unitAdapter: SimpleDayAdapter<out SimpleDayViewModel> by lazy {
        SimpleDayAdapter() {
            Selecting {
                isAllowToCancelSelection = true
            }
        }
    }

    open val adapterSelect: AdapterSelect<TimeRange, out SimpleDayViewModel>?
        get() = unitAdapter.adapterConfig.getAdapterSelect()

    open fun setupRecycler() {
        recycler.layoutManager = getLayoutManager()
        recycler.adapter = unitAdapter

        recycler.addItemDecoration(getItemDecorator())
    }

    /**
     * Presents time with view initialize
     */
    var time: TimeUnit = Time.getCurrentDayTime()
        private set

    fun setTime(time: TimeUnit) {
        this.time = time

        val timeRange: TR = getTimeRange(time)
        unitAdapter.addOrUpdateAsync(timeRangeToListOfDayRanges(timeRange)) {
            safeLet(adapterSelect) { select ->
                select.selectFirstIf { timeRange -> timeRange.contains(time) }
            }
        }
    }

    abstract fun getTimeRange(time: TimeUnit): TR


    abstract fun getLayoutManager(): LayoutManager

    private fun getItemDecorator(): ItemDecoration {
        return SimpleItemOffsetDecorator(verticalSpacing, horizontalSpacing)
    }

    private fun timeRangeToListOfDayRanges(timeRange: TR): List<TimeRange> {
        return timeRange.toDayRanges()
    }

}