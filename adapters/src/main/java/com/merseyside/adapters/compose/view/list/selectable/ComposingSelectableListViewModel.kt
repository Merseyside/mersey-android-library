package com.merseyside.adapters.compose.view.list.selectable

import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.compose.view.list.simple.ListModel
import com.merseyside.adapters.compose.view.base.selectable.CSV
import com.merseyside.adapters.model.NestedAdapterParentViewModel

class ComposingSelectableListViewModel(list: ComposingSelectableList) :
    NestedAdapterParentViewModel<ComposingSelectableList, SCV, CSV>(list), ListModel {

    override fun getNestedData(): List<CSV> {
        return item.viewList
    }
}