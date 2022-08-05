@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.utils.list

import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest

class DefaultListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    override val listActions: AdapterListActions<Parent, Model>
) : AdapterListChangeDelegate<Parent, Model> {

    //override var onListChangedCallback: AdapterListChangeDelegate.OnListChangedCallback<Parent, Model>? = null

    override fun add(items: List<Parent>): List<Model> {
        val startPosition = getModels().size
        val models = createModels(items)
//        listActions.addModels(models).also {
//            onListChangedCallback?.onItemsAdded(startPosition, models.size)
//        }
        return models
    }

    override fun remove(position: Int): Boolean {
        return try {
            val model = getModels()[position]

            listActions.remove(model)
//                .also { isRemoved ->
//                if (isRemoved) {
//                    onListChangedCallback?.onItemRemoved(position, model)
//                } //           }

            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun update(updateRequest: UpdateRequest<Parent>) {
        TODO("Not yet implemented")
    }

}