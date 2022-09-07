package com.merseyside.adapters.delegates

import com.merseyside.adapters.feature.compare.validatePriority
import com.merseyside.adapters.model.ComparableAdapterParentViewModel

abstract class PrioritizedDelegateAdapter<Item : Parent, Parent,
        Model : ComparableAdapterParentViewModel<Item, out Parent>>(
    priority: Int = 0
) : DelegateAdapter<Item, Parent, Model>() {

    var priority: Int = priority
        internal set(value) {
            if (field != value) {
                validatePriority(value)
                field = value
            }
        }

    override fun onModelCreated(model: Model) {
        return super.onModelCreated(model).also { model.priority = priority }
    }
}