package com.merseyside.adapters.feature.composable.view.list.simple

import com.merseyside.adapters.feature.composable.adapter.ViewCompositeAdapter
import com.merseyside.adapters.feature.composable.delegate.ViewDelegatesManager
import com.merseyside.adapters.feature.composable.model.ViewAdapterViewModel
import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.feature.composable.view.list.BaseComposingListDelegate
import com.merseyside.adapters.model.AdapterParentViewModel

open class ComposingListDelegate : BaseComposingListDelegate<ComposingList, ComposingListViewModel,
        SCV, ViewAdapterViewModel, ViewCompositeAdapter<SCV, ViewAdapterViewModel>>() {

    override fun createItemViewModel(item: ComposingList) = ComposingListViewModel(item)
    override fun createCompositeAdapter(
        delegateManager: ViewDelegatesManager<SCV, ViewAdapterViewModel>
    ) = ViewCompositeAdapter(delegatesManager = delegateManager)
}