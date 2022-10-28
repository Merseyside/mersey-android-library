//@file:OptIn(InternalAdaptersApi::class)
//
//package com.merseyside.adapters.compose.view.list.selectable
//
//import com.merseyside.adapters.compose.adapter.SelectableViewCompositeAdapter
//import com.merseyside.adapters.compose.delegate.ViewDelegatesManager
//import com.merseyside.adapters.compose.model.SelectableViewAdapterViewModel
//import com.merseyside.adapters.compose.view.list.BaseComposingListDelegate
//import com.merseyside.adapters.compose.view.base.selectable.CSV
//import com.merseyside.adapters.delegates.DelegatesManager
//import com.merseyside.adapters.extensions.onItemSelected
//import com.merseyside.adapters.utils.InternalAdaptersApi
//
//class ComposingSelectableListDelegate : BaseComposingListDelegate<ComposingSelectableList,
//        ComposingSelectableListViewModel, CSV, SelectableViewAdapterViewModel,
//        SelectableViewCompositeAdapter<CSV, SelectableViewAdapterViewModel>>() {
//
//    override fun initNestedAdapter(
//        model: ComposingSelectableListViewModel,
//        delegatesManager: DelegatesManager<*, *, *>
//    ): SelectableViewCompositeAdapter<CSV, SelectableViewAdapterViewModel> {
//        return super.initNestedAdapter(model, delegatesManager).also { adapter ->
//            with(adapter) {
//                onItemSelected { item, isSelected, isSelectedByUser ->
//                    item.notifyOnSelected(item, isSelected, isSelectedByUser)
//                }
//            }
//        }
//    }
//
////    @InternalAdaptersApi
////    override fun initNestedAdapter(
////        model: ComposingSelectableListViewModel,
////        delegatesManager: DelegatesManager<*, *, *>
////    ): SelectableViewCompositeAdapter<SelectableViewAdapterViewModel> {
////        return super.initNestedAdapter(model, delegatesManager)
////    }
//
//    //    @InternalAdaptersApi
////    override fun initNestedAdapter(
////        model: ComposingListViewModel,
////        delegatesManager: DelegatesManager<*, *, *>
////    ): ViewCompositeAdapter<SelectableViewAdapterViewModel> {
////        return super.initNestedAdapter(model, delegatesManager)
////    }
////    override fun createCompositeAdapter(
////        delegateManager: ViewDelegatesManager<SelectableViewAdapterViewModel>
////    ) = SelectableViewCompositeAdapter(delegatesManager = delegateManager)
//    override fun createItemViewModel(item: ComposingSelectableList) =
//        ComposingSelectableListViewModel(item)
//
//    override fun createCompositeAdapter(delegateManager: ViewDelegatesManager<CSV, SelectableViewAdapterViewModel>) =
//        SelectableViewCompositeAdapter<CSV, SelectableViewAdapterViewModel>()
//
//}