@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.interfaces.simple

import android.annotation.SuppressLint
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.logger.Logger

interface ISimpleAdapter<Parent, Model : AdapterParentViewModel<out Parent, Parent>>
    : IBaseAdapter<Parent, Model>, SimpleAdapterListActions<Parent, Model> {

    @InternalAdaptersApi
    val mutModels: MutableList<Model>

    fun add(position: Int, item: Parent) {
        val size = models.size
        if (position in 0..size) {
            this.addModel(position, createModel(item))
            adapter.notifyItemInserted(position)
        } else {
            throw IndexOutOfBoundsException("List size is $size. Your index is $position")
        }
    }

    fun add(position: Int, items: List<Parent>) {
        val size = models.size
        if (position in 0..size) {
            addModels(position, createModels(items))
            adapter.notifyItemRangeInserted(position, items.size)
        } else {
            throw IndexOutOfBoundsException("List size is $size. Your index is $position")
        }
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

    /**
     * Updates already existing items and remove oldItems
     */
    override fun update(items: List<Parent>): Boolean {
        removeOldItems(items)
        items.forEachIndexed { index, item ->

            if (updateAndNotify(item)) {
                val oldPosition = getPositionOfItem(item)
                if (oldPosition != index) adapter.notifyItemMoved(oldPosition, index)
            } else {
                add(index, item)
            }
        }

        return true
    }

    private fun removeOldItems(newList: List<Parent>) {
        val removeList = ArrayList<Model>()
        models.forEach { model ->
            val item = newList.find { item -> model.areItemsTheSame(item) }
            if (item == null) removeList.add(model)
        }

        removeModels(removeList)
    }



    private fun addModels(position: Int, list: List<Model>) {
        mutModels.addAll(position, list)
    }

    fun notifyPositionsChanged(startsWithPosition: Int) {
        if (startsWithPosition < adapter.itemCount - 1) {
            (startsWithPosition until adapter.itemCount).forEach { index ->
                models[index].onPositionChanged(index)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun clear() {
        mutModels.clear()
        adapter.notifyDataSetChanged()
    }



    @InternalAdaptersApi
    override fun addModels(models: List<Model>) {
        mutModels.addAll(models)
    }

    @InternalAdaptersApi
    override fun addModel(model: Model) {
        mutModels.add(model)
    }

    @InternalAdaptersApi
    override fun addModel(index: Int, model: Model) {
        mutModels.add(index, model)
    }


    @InternalAdaptersApi
    override fun remove(model: Model): Boolean {
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
            list.forEach { remove(it) }
            //modelList.removeAll(list)
            filterKeyMap.clear()
            filterKeyMap.forEach { entry ->
                filterKeyMap[entry.key] = entry.value.toMutableList().apply { removeAll(list) }
            }

            true
        } else false
    }


    @Throws(IllegalArgumentException::class)
    @InternalAdaptersApi
    override fun notifyModelChanged(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable>
    ): Int = try {
        val position = getPositionOfModel(model)
        adapter.notifyItemChanged(position, payloads)
        position
    } catch (e: IllegalArgumentException) {
        Logger.log("Skip notify item change!")
        throw e
    }

    override fun onItemsAdded(position: Int, itemsCount: Int) {
        adapter.notifyItemRangeInserted(position, itemsCount)
    }

    override fun onItemRemoved(position: Int, model: Model) {
        adapter.notifyItemRemoved(position)
        notifyPositionsChanged(position)
    }
}