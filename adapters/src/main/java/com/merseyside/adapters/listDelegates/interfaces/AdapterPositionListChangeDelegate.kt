package com.merseyside.adapters.listDelegates.interfaces

import com.merseyside.adapters.model.AdapterParentViewModel

interface AdapterPositionListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : AdapterListChangeDelegate<Parent, Model> {
    suspend fun add(position: Int, item: Parent)

    suspend fun add(position: Int, items: List<Parent>)

}