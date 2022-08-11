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
        listActions.addModels(models)
        return models
    }

    override fun remove(position: Int): Boolean {
        return try {
            val model = getModels()[position]
            remove(model)
            true
        } catch (e: IllegalArgumentException) {
            false
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
                val model = getModelByItem(item)
                model?.let {
                    if (update(model, item)) isUpdated = true
                } ?: addList.add(item)
            }

            if (isAddNew) {
                add(addList)
                if (addList.isNotEmpty()) isUpdated = true
            }
        }

        return isUpdated
    }

    protected fun update(model: Model, item: Parent): Boolean {
        return listActions.updateModel(model, item)
    }

    protected fun findOldItems(newItems: List<Parent>): Set<Model> {
        val models = getModels()
        return models.subtractBy(newItems) { oldModel, newItem ->
            oldModel.isDeletable() && oldModel.areItemsTheSame(newItem)
        }
    }

    protected fun removeOldItems(items: List<Parent>): Boolean {
        val modelsToRemove = findOldItems(items)
        remove(modelsToRemove.toList())
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