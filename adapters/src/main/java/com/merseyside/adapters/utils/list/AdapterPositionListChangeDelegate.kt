package com.merseyside.adapters.utils.list

import com.merseyside.adapters.model.AdapterParentViewModel

interface AdapterPositionListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : AdapterListChangeDelegate<Parent, Model> {

    //override val listActions: AdapterPositionListActions<Parent, Model>

    fun add(position: Int, item: Parent)

    fun add(position: Int, items: List<Parent>)

}