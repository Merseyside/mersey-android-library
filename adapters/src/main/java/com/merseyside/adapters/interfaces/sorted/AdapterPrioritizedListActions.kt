package com.merseyside.adapters.interfaces.sorted

import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.model.ComparableAdapterParentViewModel

interface AdapterPrioritizedListActions<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>> :
    AdapterListActions<Parent, Model> {

    fun addModel(model: Model, priority: Int)
}