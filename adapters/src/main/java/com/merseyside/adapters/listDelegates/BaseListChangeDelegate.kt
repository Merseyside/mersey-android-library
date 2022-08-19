@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.listDelegates

import com.merseyside.adapters.listDelegates.interfaces.AdapterListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

abstract class BaseListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : AdapterListChangeDelegate<Parent, Model> {

    protected fun addModel(model: Model): Model {
        listActions.addModel(model)
        return model
    }

    protected fun addModels(models: List<Model>): List<Model> {
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
    protected open fun tryToUpdateWithItem(item: Parent): Model? {
        val model = getModelByItem(item)
        return model?.also {
            updateModel(model, item)
        }
    }

    protected fun findOldItems(newItems: List<Parent>, models: List<Model> = getModels()): Set<Model> {
        return models.subtractBy(newItems) { oldModel, newItem ->
            oldModel.deletable && oldModel.areItemsTheSame(newItem)
        }
    }

    protected open fun removeOldItems(items: List<Parent>, models: List<Model> = getModels()): Boolean {
        val modelsToRemove = findOldItems(items, models)
        removeModels(modelsToRemove.toList())
        return modelsToRemove.isNotEmpty()
    }

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

fun <T1, T2> Iterable<T1>.subtractBy( //TODO move to kotlin-ext
    other: Iterable<T2>,
    predicate: (first: T1, second: T2) -> Boolean
): Set<T1> {
    return filter { first ->
        other.find { second -> predicate(first, second) } == null
    }.toSet()
}