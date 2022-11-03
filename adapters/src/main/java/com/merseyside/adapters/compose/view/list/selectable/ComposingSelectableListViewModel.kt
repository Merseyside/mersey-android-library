package com.merseyside.adapters.compose.view.list.selectable

import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.list.simple.ListModel
import com.merseyside.adapters.model.NestedAdapterParentViewModel

class ComposingSelectableListViewModel(list: ComposingSelectableList) :
    NestedAdapterParentViewModel<ComposingSelectableList, SCV, SCV>(list), ListModel {

    override fun getNestedData(): List<SCV> {
        return item.viewList
    }
}