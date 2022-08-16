package com.merseyside.adapters.listDelegates.interfaces

import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.UpdateRequest

interface AdapterListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>> {
    val listActions: AdapterListActions<Parent, Model>

    /**
     * Converts items to models and return models that should be processed by adapter
     */
    fun add(items: List<Parent>): List<Model>

    fun remove(item: Parent): Model?

    fun removeAll()

    fun update(updateRequest: UpdateRequest<Parent>): Boolean

    fun createModel(item: Parent): Model {
        return listActions.modelProvider(item)
    }

    fun createModels(items: List<Parent>): List<Model> {
        return items.map { item -> createModel(item) }
    }

    fun getModels(): List<Model> = listActions.models

    fun getPositionOfItem(item: Parent, models: List<Model> = getModels()): Int {
        models.forEachIndexed { index, model ->
            if (model.areItemsTheSame(item)) return index
        }

        throw IllegalArgumentException("No data found")
    }

    fun getModelByItem(item: Parent, models: List<Model> = getModels()): Model? {
        return models.find { it.areItemsTheSame(item) }
    }

    fun getItemCount(): Int {
        return getModels().size
    }
}