@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces.simple

import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.adapters.utils.list.AdapterPositionListChangeDelegate

interface ISimpleAdapter<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : IBaseAdapter<Parent, Model>, SimpleAdapterListActions<Parent, Model> {

    override val delegate: AdapterPositionListChangeDelegate<Parent, Model>

    @InternalAdaptersApi
    val mutModels: MutableList<Model>

    fun add(position: Int, item: Parent) {
        delegate.add(position, item)
    }

    fun add(position: Int, items: List<Parent>) {
        delegate.add(position, items)
    }

    fun addBefore(beforeItem: Parent, item: Parent) {
        val position = getPositionOfItem(beforeItem)
        add(position, item)
    }

    fun addBefore(beforeItem: Parent, items: List<Parent>) {
        val position = getPositionOfItem(beforeItem)
        add(position, items)
    }

    fun addAfter(afterItem: Parent, item: Parent) {
        val position = getPositionOfItem(afterItem)
        add(position + 1, item)
    }

    fun addAfter(afterItem: Parent, item: List<Parent>) {
        val position = getPositionOfItem(afterItem)
        add(position + 1, item)
    }


    @InternalAdaptersApi
    override fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        return super.update(updateRequest)
    }

    override fun notifyModelUpdated(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {
        val position = getPositionOfModel(model)
        adapter.notifyItemChanged(position, payloads)
    }

    private fun removeOldItems(newList: List<Parent>) {
        val removeList = ArrayList<Model>()
        models.forEach { model ->
            val item = newList.find { item -> model.areItemsTheSame(item) }
            if (item == null) removeList.add(model)
        }

        removeModels(removeList)
    }


    fun notifyPositionsChanged(startsWithPosition: Int) {
        if (startsWithPosition < adapter.itemCount - 1) {
            (startsWithPosition until adapter.itemCount).forEach { index ->
                models[index].onPositionChanged(index)
            }
        }
    }

    @InternalAdaptersApi
    override fun addModel(model: Model) {
        mutModels.add(model)
        adapter.notifyItemInserted(getItemsCount() - 1)
    }

    @InternalAdaptersApi
    override fun addModels(models: List<Model>) {
        mutModels.addAll(models)
        val modelsCount = models.size
        adapter.notifyItemRangeInserted(getItemsCount() - modelsCount, modelsCount)
    }

    @InternalAdaptersApi
    override fun addModelByPosition(position: Int, model: Model) {
        mutModels.add(position, model)
        adapter.notifyItemInserted(position)
    }

    @InternalAdaptersApi
    override fun addModelsByPosition(position: Int, models: List<Model>) {
        mutModels.addAll(position, models)
        adapter.notifyItemRangeInserted(position, models.size)
    }

    @InternalAdaptersApi
    override fun removeModel(model: Model): Boolean {
        val position = getPositionOfModel(model)
        val removed = mutModels.remove(model)
        if (removed) {
            adapter.notifyItemRemoved(position)
        }
        notifyPositionsChanged(position)

        return removed
    }

    @InternalAdaptersApi
    override fun removeModels(list: List<Model>): Boolean {
        return if (list.isNotEmpty()) {
            list.forEach { removeModel(it) }
            //modelList.removeAll(list)
            filterKeyMap.clear()
            filterKeyMap.forEach { entry ->
                filterKeyMap[entry.key] = entry.value.toMutableList().apply { removeAll(list) }
            }

            true
        } else false
    }

    override fun removeAll() {
        mutModels.clear()
        adapter.notifyDataSetChanged()
    }

    override fun changeModelPosition(model: Model, oldPosition: Int, newPosition: Int) {
        mutModels.move(oldPosition, newPosition)
    }
}

/**
 * Moves item from old position to new position
 * @return moved item
 * */
fun <T> MutableList<T>.move(oldPosition: Int, newPosition: Int): T { //TODO move to kotlin-ext
    val item = get(oldPosition)
    removeAt(oldPosition)
    add(newPosition, item)
    return item
}

fun <T> MutableList<T>.move(item: T, newPosition: Int): T {
    val oldPosition = indexOf(item)
    removeAt(oldPosition)
    add(newPosition, item)
    return item
}