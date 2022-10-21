package com.merseyside.adapters.compose.view.list.selectable

import com.merseyside.adapters.compose.adapter.SelectableViewCompositeAdapter
import com.merseyside.adapters.compose.delegate.ViewDelegatesManager
import com.merseyside.adapters.compose.model.SelectableViewAdapterViewModel
import com.merseyside.adapters.compose.view.list.BaseComposingListDelegate
import com.merseyside.adapters.compose.view.selectable.CSV

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