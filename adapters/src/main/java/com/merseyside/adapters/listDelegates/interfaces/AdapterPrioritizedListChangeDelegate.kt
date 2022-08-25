package com.merseyside.adapters.listDelegates.interfaces

import com.merseyside.adapters.model.ComparableAdapterParentViewModel

interface AdapterPrioritizedListChangeDelegate<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>> :
    AdapterListChangeDelegate<Parent, Model> {

    suspend fun add(item: Parent, priority: Int): Model
}