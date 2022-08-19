@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.listDelegates

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest

abstract class ListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : BaseListChangeDelegate<Parent, Model>() {

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
}