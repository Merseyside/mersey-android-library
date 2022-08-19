package com.merseyside.adapters.listDelegates.interfaces

import com.merseyside.adapters.model.AdapterParentViewModel

interface AdapterPositionListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : AdapterListChangeDelegate<Parent, Model> {

    fun add(position: Int, item: Parent)

    fun add(position: Int, items: List<Parent>)

}