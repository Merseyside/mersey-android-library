package com.merseyside.merseyLib.features.adapters.delegate.model

import com.merseyside.adapters.delegates.SimpleComparableAdapterViewModel
import com.merseyside.merseyLib.features.adapters.delegate.entity.ButtonItem

class ButtonViewModel(item: ButtonItem) : SimpleComparableAdapterViewModel<ButtonItem>(item) {

    override fun areItemsTheSame(other: ButtonItem) = false
    override fun notifyUpdate() {}

    fun getTitle() = item.title

    override fun compareTo(other: Any): Int {
        TODO("Not yet implemented")
    }
}