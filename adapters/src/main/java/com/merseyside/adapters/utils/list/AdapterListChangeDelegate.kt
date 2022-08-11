@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.utils.list

import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest

interface AdapterListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>> {
    val listActions: AdapterListActions<Parent, Model>
    //var onListChangedCallback: OnListChangedCallback<Parent, Model>?

    /**
     * Converts items to models and return models that should be processed by adapter
     */
    fun add(items: List<Parent>): List<Model>

    fun remove(position: Int): Boolean

    fun removeAll()

    fun update(updateRequest: UpdateRequest<Parent>): Boolean

    fun createModel(item: Parent): Model {
        return listActions.modelProvider(item)
    }

    fun createModels(items: List<Parent>): List<Model> {
        return items.map { item -> createModel(item) }
    }

    fun getModels(): List<Model> = listActions.models

    fun getPositionOfItem(item: Parent): Int {
        getModels().forEachIndexed { index, model ->
            if (model.areItemsTheSame(item)) return index
        }

        throw IllegalArgumentException("No data found")
    }

    fun getModelByItem(item: Parent): Model? {
        return getModels().find { it.areItemsTheSame(item) }
    }

    fun remove(models: List<Model>) {
        models.forEach { remove(it) }
    }

    fun remove(model: Model): Boolean {
        return listActions.removeModel(model)
    }

    fun getItemCount(): Int {
        return getModels().size
    }
}