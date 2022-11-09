package com.merseyside.merseyLib.features.adapters.delegate.adapter

import com.merseyside.adapters.delegates.SimplePrioritizedDelegateAdapter
import com.merseyside.adapters.feature.compare.Priority
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.delegate.entity.ButtonItem
import com.merseyside.merseyLib.features.adapters.delegate.model.ButtonViewModel

class ButtonDelegateAdapter : SimplePrioritizedDelegateAdapter<ButtonItem, ButtonViewModel>(
    priority = Priority.ALWAYS_LAST_PRIORITY
) {
    override fun createItemViewModel(item: ButtonItem) = ButtonViewModel(item)
    override fun getLayoutIdForItem(viewType: Int) = R.layout.item_button
    override fun getBindingVariable() = BR.model
}