@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces.sorted

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.extensions.*
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.listDelegates.interfaces.AdapterPrioritizedListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

interface ISortedAdapter<Parent, Model> : IBaseAdapter<Parent, Model>,
    AdapterPrioritizedListActions<Parent, Model>
        where Model : ComparableAdapterParentViewModel<out Parent, Parent> {

    val sortedList: SortedList<Model>
    override val delegate: AdapterPrioritizedListChangeDelegate<Parent, Model>

    fun addAsync(item: Parent, priority: Int, onComplete: (Unit) -> Unit = {}) {
        doAsync(onComplete) { add(item, priority) }
    }

    suspend fun add(item: Parent, priority: Int) {
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
    override suspend fun addModel(model: Model) {
        sortedList.add(model)
    }

    override suspend fun addModel(model: Model, priority: Int) {
        model.priority = priority
        addModel(model)
    }

    @InternalAdaptersApi
    override suspend fun addModels(models: List<Model>) {
        sortedList.batchedUpdate {
            models.forEach { model ->
                addModel(model)
            }
        }
    }

    override suspend fun removeModel(model: Model): Boolean {
        return sortedList.remove(model)
    }

    override suspend fun removeModels(models: List<Model>): Boolean {
        sortedList.batchedUpdate {
            models.map { model -> removeModel(model) }.any()
        }

        return true
    }

    override suspend fun removeAll() {
        sortedList.clear()
        adapter.notifyDataSetChanged()
    }

    override fun comparePriority(model1: Model, model2: Model): Int {
        return model1.priority.compareTo(model2.priority)
    }
}