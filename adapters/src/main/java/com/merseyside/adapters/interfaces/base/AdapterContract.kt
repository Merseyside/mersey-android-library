package com.merseyside.adapters.interfaces.base

import com.merseyside.adapters.model.VM

interface AdapterContract<Parent, Model : VM<Parent>> {

    val provideModelByItem: suspend (Parent) -> Model
}