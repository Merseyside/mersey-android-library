package com.merseyside.merseyLib.features.calendar.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.merseyside.archy.presentation.fragment.BaseBindingFragment
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.databinding.FragmentCalendarBinding
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.merseyLib.time.Time
import com.merseyside.merseyLib.time.units.Millis

class CalendarFragment : BaseBindingFragment<FragmentCalendarBinding>() {

    override fun getLayoutId()= R.layout.fragment_calendar
    override fun performInjection(bundle: Bundle?, vararg params: Any) {}
    override fun getTitle(context: Context) = "Calendar"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val time = Time.nowGMT

        with(requireBinding()) {
            weekView.setTime(time)
            monthView.setTime(time)
        }
    }
}

sealed class kek {
    abstract val domain: String
}