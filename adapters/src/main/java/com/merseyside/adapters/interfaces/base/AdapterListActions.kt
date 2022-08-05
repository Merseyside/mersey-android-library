package com.merseyside.adapters.interfaces.base

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

interface AdapterListActions<Parent, Model : AdapterParentViewModel<out Parent, Parent>> :
    AdapterListContract<Parent, Model> {

    @InternalAdaptersApi
    fun addModel(model: Model)

    @InternalAdaptersApi
    fun addModels(models: List<Model>)


    @InternalAdaptersApi
    fun remove(model: Model): Boolean

    @InternalAdaptersApi
    fun removeModels(list: List<Model>): Boolean
}