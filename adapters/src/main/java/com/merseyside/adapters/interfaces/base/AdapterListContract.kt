package com.merseyside.adapters.interfaces.base

import com.merseyside.adapters.model.VM

interface AdapterListContract<Parent, Model : VM<Parent>> {

    val provideModelByItem: suspend (Parent) -> Model
}