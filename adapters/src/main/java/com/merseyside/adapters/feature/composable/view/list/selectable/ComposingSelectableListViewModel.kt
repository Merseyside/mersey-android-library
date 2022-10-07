package com.merseyside.adapters.feature.composable.view.list.selectable

import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.feature.composable.view.list.simple.ComposingList
import com.merseyside.adapters.feature.composable.view.list.simple.ListModel
import com.merseyside.adapters.feature.composable.view.selectable.CSV
import com.merseyside.adapters.model.NestedAdapterParentViewModel

class ComposingSelectableListViewModel(list: ComposingSelectableList) :
    NestedAdapterParentViewModel<ComposingSelectableList, SCV, CSV>(list), ListModel {

    override fun getNestedData(): List<CSV> {
        return item.viewList
    }
}