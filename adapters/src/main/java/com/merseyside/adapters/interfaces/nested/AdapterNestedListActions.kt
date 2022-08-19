package com.merseyside.adapters.interfaces.nested

import com.merseyside.adapters.interfaces.sorted.AdapterPrioritizedListActions
import com.merseyside.adapters.model.NestedAdapterParentViewModel

interface AdapterNestedListActions<Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, *>, InnerAdapter>
    : AdapterPrioritizedListActions<Parent, Model> {

    fun getNestedAdapterByModel(model: Model): InnerAdapter
}