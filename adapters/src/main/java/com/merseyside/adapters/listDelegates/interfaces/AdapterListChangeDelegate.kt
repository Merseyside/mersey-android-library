package com.merseyside.adapters.listDelegates.interfaces

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.UpdateRequest

interface AdapterListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>> {
    /**
     * Converts items to models and return models that should be processed by adapter
     */
    fun add(items: List<Parent>): List<Model>

    fun remove(item: Parent): Model?

    fun removeAll()

    fun update(updateRequest: UpdateRequest<Parent>): Boolean
}