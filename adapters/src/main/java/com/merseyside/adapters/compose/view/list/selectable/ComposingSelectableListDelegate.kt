package com.merseyside.adapters.compose.view.list.selectable

import com.merseyside.adapters.compose.adapter.ViewCompositeAdapter
import com.merseyside.adapters.compose.delegate.ViewDelegatesManager
import com.merseyside.adapters.compose.model.ViewAdapterViewModel
import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.list.BaseComposingListDelegate
import com.merseyside.adapters.feature.selecting.Selecting

class ComposingSelectableListDelegate : BaseComposingListDelegate<ComposingSelectableList,
        ComposingSelectableListViewModel, SCV, ViewAdapterViewModel,
        ViewCompositeAdapter<SCV, ViewAdapterViewModel>>() {

    override fun createItemViewModel(item: ComposingSelectableList) = ComposingSelectableListViewModel(item)

    override fun createCompositeAdapter(
        model: ComposingSelectableListViewModel,
        delegateManager: ViewDelegatesManager<SCV, ViewAdapterViewModel>
    ): ViewCompositeAdapter<SCV, ViewAdapterViewModel> {
        return ViewCompositeAdapter(delegateManager) {
            Selecting {
                with(model) {
                    variableId = item.variableId

                    selectableMode = item.selectableMode
                    isSelectEnabled = item.isSelectEnabled
                    isAllowToCancelSelection = item.isAllowToCancelSelection

                    onSelect = { item, isSelected, isSelectedByUser ->
                        model.item.notifyOnSelected(item, isSelected, isSelectedByUser)
                    }
                }
            }
        }
    }

}