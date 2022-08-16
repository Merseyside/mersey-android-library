package com.merseyside.adapters.delegates

import com.merseyside.adapters.feature.compare.validatePriority
import com.merseyside.adapters.model.ComparableAdapterParentViewModel

abstract class PrioritizedDelegateAdapter<Item : Parent, Parent,
        Model : ComparableAdapterParentViewModel<Item, out Parent>>(
    private val priority: Int
) : DelegateAdapter<Item, Parent, Model>() {

    init {
        validatePriority(priority)
    }

    override fun createItemViewModel(item: Item): Model {
        return super.createItemViewModel(item).also { it.priority = priority }
    }
}