@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.listDelegates

import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest

abstract class ListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : BaseListChangeDelegate<Parent, Model>() {

    abstract val listActions: AdapterListActions<Parent, Model>

    final override fun getModels(): List<Model> = listActions.models

    override fun add(items: List<Parent>): List<Model> {
        val models = createModels(items)
        return addModels(models)
    }

    override fun remove(item: Parent): Model? {
        return try {
            val model = getModelByItem(item)
            model?.let { removeModel(model) }
            model
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun removeAll() {
        listActions.removeAll()
    }

    override fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        var isUpdated = false

        with(updateRequest) {
            if (updateRequest.isDeleteOld) {
                isUpdated = removeOldItems(list)
            }

            val addList = ArrayList<Parent>()
            list.forEach { item ->
                if (tryToUpdateWithItem(item) != null) isUpdated = true
                else addList.add(item)
            }

            if (isAddNew) {
                add(addList)
                if (addList.isNotEmpty()) isUpdated = true
            }
        }

        return isUpdated
    }

    internal fun addModel(model: Model): Model {
        listActions.addModel(model)
        return model
    }

    internal fun addModels(models: List<Model>): List<Model> {
        listActions.addModels(models)
        return models
    }

    protected fun removeModels(models: List<Model>) {
        models.forEach { removeModel(it) }
    }

    protected fun removeModel(model: Model): Boolean {
        return listActions.removeModel(model)
    }

    protected open fun updateModel(model: Model, item: Parent): Boolean {
        return listActions.updateModel(model, item)
    }

    /**
     * @return model if it have been updated, null otherwise
     */
    override fun tryToUpdateWithItem(item: Parent): Model? {
        val model = getModelByItem(item)
        return model?.also {
            updateModel(model, item)
        }
    }

    override fun findOldItems(newItems: List<Parent>, models: List<Model>): Set<Model> {
        return models.subtractBy(newItems) { oldModel, newItem ->
            oldModel.deletable && oldModel.areItemsTheSame(newItem)
        }
    }

    override fun removeOldItems(items: List<Parent>, models: List<Model>): Boolean {
        val modelsToRemove = findOldItems(items, models)
        removeModels(modelsToRemove.toList())
        return modelsToRemove.isNotEmpty()
    }

    override fun createModel(item: Parent): Model {
        return listActions.modelProvider(item)
    }

    override fun createModels(items: List<Parent>): List<Model> {
        return items.map { item -> createModel(item) }
    }
}