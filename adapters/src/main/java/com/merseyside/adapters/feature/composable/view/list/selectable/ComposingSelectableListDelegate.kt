package com.merseyside.adapters.feature.composable.view.list.selectable

import com.merseyside.adapters.feature.composable.adapter.SelectableViewCompositeAdapter
import com.merseyside.adapters.feature.composable.delegate.ViewDelegatesManager
import com.merseyside.adapters.feature.composable.model.SelectableViewAdapterViewModel
import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.feature.composable.view.list.BaseComposingListDelegate
import com.merseyside.adapters.feature.composable.view.list.simple.ComposingList
import com.merseyside.adapters.feature.composable.view.selectable.CSV
import com.merseyside.adapters.model.SelectableAdapterParentViewModel

class ComposingSelectableListDelegate : BaseComposingListDelegate<ComposingSelectableList,
        ComposingSelectableListViewModel, CSV, SelectableViewAdapterViewModel,
        SelectableViewCompositeAdapter<CSV, SelectableViewAdapterViewModel>>() {

//    @InternalAdaptersApi
//    override fun initNestedAdapter(
//        model: ComposingSelectableListViewModel,
//        delegatesManager: DelegatesManager<*, *, *>
//    ): SelectableViewCompositeAdapter<SelectableViewAdapterViewModel> {
//        return super.initNestedAdapter(model, delegatesManager)
//    }

    //    @InternalAdaptersApi
//    override fun initNestedAdapter(
//        model: ComposingListViewModel,
//        delegatesManager: DelegatesManager<*, *, *>
//    ): ViewCompositeAdapter<SelectableViewAdapterViewModel> {
//        return super.initNestedAdapter(model, delegatesManager)
//    }
//    override fun createCompositeAdapter(
//        delegateManager: ViewDelegatesManager<SelectableViewAdapterViewModel>
//    ) = SelectableViewCompositeAdapter(delegatesManager = delegateManager)
    override fun createItemViewModel(item: ComposingSelectableList) =
        ComposingSelectableListViewModel(item)

    override fun createCompositeAdapter(delegateManager: ViewDelegatesManager<CSV, SelectableViewAdapterViewModel>) =
        SelectableViewCompositeAdapter<CSV, SelectableViewAdapterViewModel>()

}