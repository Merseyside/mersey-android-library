package com.merseyside.adapters.interfaces.sorted

import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

interface AdapterPrioritizedListActions<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>> :
    AdapterListActions<Parent, Model> {

    suspend fun addModel(model: Model, priority: Int)

    @InternalAdaptersApi
    fun comparePriority(model1: Model, model2: Model): Int
}