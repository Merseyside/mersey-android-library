package com.merseyside.adapters.interfaces.base

import com.merseyside.adapters.model.AdapterParentViewModel

interface AdapterPositionListActions<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : AdapterListActions<Parent, Model> {

    fun addModelByPosition(position: Int, model: Model)

    fun addModelsByPosition(position: Int, models: List<Model>)

    fun changeModelPosition(model: Model, oldPosition: Int, newPosition: Int)
}