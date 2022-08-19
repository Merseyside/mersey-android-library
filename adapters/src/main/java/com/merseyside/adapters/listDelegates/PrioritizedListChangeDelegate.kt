package com.merseyside.adapters.listDelegates

import com.merseyside.adapters.feature.compare.validatePriority
import com.merseyside.adapters.interfaces.sorted.AdapterPrioritizedListActions
import com.merseyside.adapters.listDelegates.interfaces.AdapterPrioritizedListChangeDelegate
import com.merseyside.adapters.model.ComparableAdapterParentViewModel

open class PrioritizedListChangeDelegate<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>>(
    override val listActions: AdapterPrioritizedListActions<Parent, Model>
) : ListChangeDelegate<Parent, Model>(), AdapterPrioritizedListChangeDelegate<Parent, Model> {

    override fun add(item: Parent, priority: Int): Model {
        validatePriority(priority)
        val model = createModel(item)
        addModel(model, priority)
        return model
    }

    internal fun addModel(model: Model, priority: Int) {
        listActions.addModel(model, priority)
    }
}