package com.merseyside.adapters.listDelegates.interfaces

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.UpdateRequest

interface AdapterListChangeDelegate<Parent, Model>
    where Model : AdapterParentViewModel<out Parent, Parent> {
    /**
     * Converts items to models and return models that should be processed by adapter
     */
    suspend fun add(item: Parent): Model

    suspend fun add(items: List<Parent>): List<Model>

    suspend fun remove(item: Parent): Model?

    suspend fun remove(items: List<Parent>): List<Model>

    suspend fun clear()

    suspend fun update(updateRequest: UpdateRequest<Parent>): Boolean

    suspend fun addOrUpdate(items: List<Parent>)

    suspend fun getModelByItem(item: Parent): Model?
}