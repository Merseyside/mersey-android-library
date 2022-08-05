package com.merseyside.adapters.interfaces.base

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

interface AdapterListContract<Parent, Model : AdapterParentViewModel<out Parent, Parent>> {

    val modelProvider: (Parent) -> Model
    val models: List<Model>

    @InternalAdaptersApi
    fun notifyModelChanged(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable>
    ): Int
}