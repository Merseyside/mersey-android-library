package com.merseyside.adapters.interfaces.sorted

import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

interface AdapterPrioritizedListActions<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>> :
    AdapterListActions<Parent, Model> {

    fun addModel(model: Model, priority: Int)

    @InternalAdaptersApi
    fun comparePriority(o1: Model, o2: Model): Int {
        return o1.priority.compareTo(o2.priority)
    }
}