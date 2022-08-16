@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces.sorted

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.extensions.*
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.listDelegates.interfaces.AdapterPrioritizedListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

interface ISortedAdapter<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>>
    : IBaseAdapter<Parent, Model>, AdapterPrioritizedListActions<Parent, Model> {

    val sortedList: SortedList<Model>
    override val delegate: AdapterPrioritizedListChangeDelegate<Parent, Model>

    fun add(item: Parent, priority: Int) {
        delegate.add(item, priority)
    }

    override fun notifyModelUpdated(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {
        val position = getPositionOfModel(model)
        adapter.notifyItemChanged(position, payloads)
        sortedList.recalculatePositionOfItemAt(position)
    }

    /* Models list actions */
    override fun addModel(model: Model) {
        sortedList.add(model)
    }

    override fun addModel(model: Model, priority: Int) {
        model.priority = priority
        addModel(model)
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