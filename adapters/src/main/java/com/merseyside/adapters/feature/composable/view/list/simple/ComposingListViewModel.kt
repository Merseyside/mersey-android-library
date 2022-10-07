package com.merseyside.adapters.feature.composable.view.list.simple

import com.merseyside.adapters.feature.composable.view.base.SCV
import com.merseyside.adapters.model.NestedAdapterParentViewModel

open class ComposingListViewModel(
    list: ComposingList
) : NestedAdapterParentViewModel<ComposingList, SCV, SCV>(list), ListModel {

    override fun getNestedData(): List<SCV> {
        return item.viewList
    }
}