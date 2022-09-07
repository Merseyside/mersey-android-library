@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces.simple

import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.listDelegates.interfaces.AdapterPositionListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.extensions.move

interface ISimpleAdapter<Parent, Model> : IBaseAdapter<Parent, Model>,
    AdapterPositionListActions<Parent, Model>
        where Model : AdapterParentViewModel<out Parent, Parent> {

    override val delegate: AdapterPositionListChangeDelegate<Parent, Model>

    @InternalAdaptersApi
    val mutModels: MutableList<Model>

    fun addAsync(position: Int, item: Parent, onComplete: (Unit) -> Unit) {
        doAsync(onComplete) { add(position, item) }
    }

    suspend fun add(position: Int, item: Parent) {
        delegate.add(position, item)
    }

    fun addAsync(position: Int, items: List<Parent>, onComplete: (Unit) -> Unit) {
        doAsync(onComplete) { add(position, items) }
    }

    suspend fun add(position: Int, items: List<Parent>) {
        delegate.add(position, items)
    }

    suspend fun addBefore(beforeItem: Parent, item: Parent) {
        val position = getPositionOfItem(beforeItem)
        add(position, item)
    }

    suspend fun addBefore(beforeItem: Parent, items: List<Parent>) {
        val position = getPositionOfItem(beforeItem)
        add(position, items)
    }

    suspend fun addAfter(afterItem: Parent, item: Parent) {
        val position = getPositionOfItem(afterItem)
        add(position + 1, item)
    }

    suspend fun addAfter(afterItem: Parent, item: List<Parent>) {
        val position = getPositionOfItem(afterItem)
        add(position + 1, item)
    }

    override suspend fun notifyModelUpdated(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {
        val position = getPositionOfModel(model)
        adapter.notifyItemChanged(position, payloads)
    }

    fun notifyPositionsChanged(startsWithPosition: Int) {
        if (startsWithPosition < getLastPositionIndex()) {
            (startsWithPosition until adapter.itemCount).forEach { index ->
                models[index].onPositionChanged(index)
            }
        }
    }

    @InternalAdaptersApi
    override suspend fun addModel(model: Model) {
        mutModels.add(model)
        adapter.notifyItemInserted(getLastPositionIndex())
    }

    @InternalAdaptersApi
    override suspend fun addModels(models: List<Model>) {
        mutModels.addAll(models)
        val modelsCount = models.size
        adapter.notifyItemRangeInserted(getItemsCount() - modelsCount, modelsCount)
    }

    @InternalAdaptersApi
    override suspend fun addModelByPosition(position: Int, model: Model) {
        mutModels.add(position, model)
        adapter.notifyItemInserted(position)
    }

    @InternalAdaptersApi
    override suspend fun addModelsByPosition(position: Int, models: List<Model>) {
        mutModels.addAll(position, models)
        adapter.notifyItemRangeInserted(position, models.size)
    }

    @InternalAdaptersApi
    override suspend fun removeModel(model: Model): Boolean {
        val position = getPositionOfModel(model)
        val removed = mutModels.remove(model)
        if (removed) {
            adapter.notifyItemRemoved(position)
        }
        notifyPositionsChanged(position)

        return removed
    }

    override suspend fun removeModels(models: List<Model>): Boolean {
        return models.map { model -> removeModel(model) }.any()
    }

    override suspend fun removeAll() {
        mutModels.clear()
        adapter.notifyDataSetChanged()
    }

    override suspend fun changeModelPosition(model: Model, oldPosition: Int, newPosition: Int) {
        mutModels.move(oldPosition, newPosition)
    }

    override suspend fun getPositionOfModel(model: Model): Int {
        return models.indexOf(model)
    }
}
