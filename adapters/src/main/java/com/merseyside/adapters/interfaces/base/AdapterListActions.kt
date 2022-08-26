package com.merseyside.adapters.interfaces.base

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

interface AdapterListActions<Parent, Model : AdapterParentViewModel<out Parent, Parent>> :
    AdapterListContract<Parent, Model> {

    @InternalAdaptersApi
    suspend fun addModel(model: Model)

    @InternalAdaptersApi
    suspend fun addModels(models: List<Model>)


    @InternalAdaptersApi
    suspend fun removeModel(model: Model): Boolean

    @InternalAdaptersApi
    suspend fun removeModels(models: List<Model>): Boolean

    @InternalAdaptersApi
    suspend fun removeAll()

    /**
     * Updates [model] with [item]
     * @return true if model's item and item are not the same.
     */
    @InternalAdaptersApi
    suspend fun updateModel(model: Model, item: Parent): Boolean
}