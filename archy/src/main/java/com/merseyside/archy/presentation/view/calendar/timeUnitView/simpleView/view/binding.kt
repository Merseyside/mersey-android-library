package com.merseyside.archy.presentation.view.calendar.timeUnitView.simpleView.view

import androidx.databinding.BindingAdapter
import com.merseyside.archy.presentation.view.calendar.timeUnitView.model.SimpleDayViewModel

@BindingAdapter("dayModel")
fun bindModel(view: SimpleDayView, model: SimpleDayViewModel?) {
    if (model != null) {
        view.applyModel(model)
    }
}

