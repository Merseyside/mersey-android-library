package com.merseyside.adapters.interfaces.base

import com.merseyside.adapters.model.AdapterParentViewModel

interface AdapterListContract<Parent, Model : AdapterParentViewModel<out Parent, Parent>> {

    val provideModelByItem: suspend (Parent) -> Model
    val models: List<Model>
}