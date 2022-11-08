package com.merseyside.adapters.compose.view.list.simple

import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.model.NestedAdapterParentViewModel

open class ComposingListViewModel<L : ComposingList>(
    list: L
) : NestedAdapterParentViewModel<L, SCV, SCV>(list) {

    override fun getNestedData(): List<SCV> {
        return item.viewList
    }
}