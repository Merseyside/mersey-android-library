@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.ext.*
import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

interface ISortedAdapter<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>>
    : IBaseAdapter<Parent, Model>, AdapterListActions<Parent, Model> {

    val sortedList: SortedList<Model>

    override fun notifyModelUpdated(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {
        val position = getPositionOfModel(model)
        adapter.notifyItemChanged(position, payloads)
        sortedList.recalculatePositionOfItemAt(position)
    }

    private fun replaceAll(models: List<Model>) {
        sortedList.batchedUpdate {
            val sortedModels = sortedList.getAll().toSet()
            val subtract = models.subtract(sortedModels)

            val subtractSorted = sortedModels.subtract(models.toSet()).toList()

            sortedList.removeAll(subtractSorted)
            sortedList.addAll(subtract)
        }
    }

    /* Models list actions */
    override fun addModel(model: Model) {
        sortedList.add(model)
    }

    @InternalAdaptersApi
    override fun addModels(models: List<Model>) {
        sortedList.batchedUpdate { addAll(models) }
    }

    override fun removeModel(model: Model): Boolean {
        return sortedList.remove(model)
    }

    override fun removeAll() {
        sortedList.clear()
        adapter.notifyDataSetChanged()
    }
}