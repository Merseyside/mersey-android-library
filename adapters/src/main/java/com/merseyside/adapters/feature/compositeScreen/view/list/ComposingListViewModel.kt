package com.merseyside.adapters.feature.compositeScreen.view.list

import com.merseyside.adapters.feature.compositeScreen.SCV
import com.merseyside.adapters.model.NestedAdapterParentViewModel

class ComposingListViewModel(
    list: ComposingList
) : NestedAdapterParentViewModel<ComposingList, SCV, SCV>(list) {

    override fun getNestedData(): List<SCV> {
        return item.viewList
    }
}