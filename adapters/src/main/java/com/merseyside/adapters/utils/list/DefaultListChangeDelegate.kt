@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.utils.list

import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest

open class DefaultListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    override val listActions: AdapterListActions<Parent, Model>
) : AdapterListChangeDelegate<Parent, Model> {

    override fun add(items: List<Parent>): List<Model> {
        val models = createModels(items)
        return addModels(models)
    }

    protected fun addModel(model: Model): Model {
        listActions.addModel(model)
        return model
    }

    protected fun addModels(models: List<Model>): List<Model> {
        listActions.addModels(models)
        return models
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

    protected fun removeModels(models: List<Model>) {
        models.forEach { removeModel(it) }
    }

    protected fun removeModel(model: Model): Boolean {
        return listActions.removeModel(model)
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

    protected open fun updateModel(model: Model, item: Parent): Boolean {
        return listActions.updateModel(model, item)
    }

    /**
     * @return model if it have been updated, null otherwise
     */
    protected open fun tryToUpdateWithItem(item: Parent): Model? {
        val model = getModelByItem(item)
        return model?.also {
            updateModel(model, item)
        }
    }

    protected fun findOldItems(newItems: List<Parent>, models: List<Model> = getModels()): Set<Model> {
        return models.subtractBy(newItems) { oldModel, newItem ->
            oldModel.isDeletable() && oldModel.areItemsTheSame(newItem)
        }
    }

    protected open fun removeOldItems(items: List<Parent>, models: List<Model> = getModels()): Boolean {
        val modelsToRemove = findOldItems(items, models)
        removeModels(modelsToRemove.toList())
        return modelsToRemove.isNotEmpty()
    }
}

fun <T1, T2> Iterable<T1>.subtractBy( //TODO move to kotlin-ext
    other: Iterable<T2>,
    predicate: (first: T1, second: T2) -> Boolean
): Set<T1> {
    return filter { first ->
        other.find { second -> predicate(first, second) } == null
    }.toSet()
}