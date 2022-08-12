package com.merseyside.adapters.utils.list

import com.merseyside.adapters.model.AdapterParentViewModel

interface AdapterPositionListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : AdapterListChangeDelegate<Parent, Model> {

    fun add(position: Int, item: Parent)

    fun add(position: Int, items: List<Parent>)

    fun isValidPosition(position: Int, models: List<Model> = getModels()): Boolean {
        return getModels().size >= position
    }

}