package com.merseyside.adapters.delegates

import com.merseyside.adapters.feature.compare.Priority.validatePriority
import com.merseyside.adapters.interfaces.delegate.IPrioritizedDelegateAdapter
import com.merseyside.adapters.model.ComparableAdapterParentViewModel

abstract class PrioritizedDelegateAdapter<Item : Parent, Parent, Model>(
    priority: Int = 0
) : DelegateAdapter<Item, Parent, Model>(), IPrioritizedDelegateAdapter<Item, Parent, Model>
    where Model : ComparableAdapterParentViewModel<Item, Parent> {

    override var priority: Int = priority
        set(value) {
            if (field != value) {
                validatePriority(value)
                field = value
            }
        }

    override fun onModelCreated(model: Model) {
        return super.onModelCreated(model).also { model.priority = priority }
    }
}