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
    fun removeModel(model: Model): Boolean

    @InternalAdaptersApi
    fun removeAll()

    /**
     * Updates [model] with [item]
     * @return true if model's item and item are not the same.
     */
    @InternalAdaptersApi
    fun updateModel(model: Model, item: Parent): Boolean
}