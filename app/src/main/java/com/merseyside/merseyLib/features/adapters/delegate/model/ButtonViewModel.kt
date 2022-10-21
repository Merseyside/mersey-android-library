package com.merseyside.merseyLib.features.adapters.delegate.model

import com.merseyside.adapters.delegates.SimpleAdapterViewModel
import com.merseyside.merseyLib.features.adapters.delegate.entity.ButtonItem

class ButtonViewModel(item: ButtonItem) : SimpleAdapterViewModel<ButtonItem>(item) {

    override fun areItemsTheSame(other: ButtonItem) = false
    override fun notifyUpdate() {}

    fun getTitle() = item.title
}