package com.merseyside.adapters.feature.composable.view.list.simple

import com.merseyside.adapters.feature.composable.SCV
import com.merseyside.adapters.model.NestedAdapterParentViewModel

class ComposingListViewModel(
    list: ComposingList
) : NestedAdapterParentViewModel<ComposingList, SCV, SCV>(list) {

    override fun getNestedData(): List<SCV> {
        return item.viewList
    }
}