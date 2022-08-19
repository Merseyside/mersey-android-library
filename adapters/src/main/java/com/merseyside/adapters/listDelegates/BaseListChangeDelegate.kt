package com.merseyside.adapters.listDelegates

import com.merseyside.adapters.listDelegates.interfaces.AdapterListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel

abstract class BaseListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : AdapterListChangeDelegate<Parent, Model> {

    abstract fun getModels(): List<Model>

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

    abstract fun createModel(item: Parent): Model

    abstract fun createModels(items: List<Parent>): List<Model>

    abstract fun tryToUpdateWithItem(item: Parent): Model?

    abstract fun findOldItems(newItems: List<Parent>, models: List<Model> = getModels()): Set<Model>

    abstract fun removeOldItems(items: List<Parent>, models: List<Model> = getModels()): Boolean
}

fun <T1, T2> Iterable<T1>.subtractBy( //TODO move to kotlin-ext
    other: Iterable<T2>,
    predicate: (first: T1, second: T2) -> Boolean
): Set<T1> {
    return filter { first ->
        other.find { second -> predicate(first, second) } == null
    }.toSet()
}