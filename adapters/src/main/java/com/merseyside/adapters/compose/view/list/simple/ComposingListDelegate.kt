package com.merseyside.adapters.compose.view.list.simple

import com.merseyside.adapters.compose.adapter.ViewCompositeAdapter
import com.merseyside.adapters.compose.delegate.ViewDelegatesManager
import com.merseyside.adapters.compose.model.ViewAdapterViewModel
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.list.BaseComposingListDelegate

open class ComposingListDelegate : BaseComposingListDelegate<ComposingList, ComposingListViewModel,
        SCV, ViewAdapterViewModel, ViewCompositeAdapter<SCV, ViewAdapterViewModel>>() {

    override fun createItemViewModel(item: ComposingList) = ComposingListViewModel(item)
    override fun createCompositeAdapter(
        delegateManager: ViewDelegatesManager<SCV, ViewAdapterViewModel>
    ) = ViewCompositeAdapter(delegatesManager = delegateManager)
}