package com.merseyside.merseyLib.features.adapters.delegate.model

import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.merseyLib.features.adapters.delegate.entity.ButtonItem

class ButtonViewModel(item: ButtonItem) : AdapterViewModel<ButtonItem>(item) {

    override fun areItemsTheSame(other: ButtonItem) = false
    override fun notifyUpdate() {}

    fun getTitle() = item.title
}