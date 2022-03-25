@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.UpdateRequest
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.merseyLib.kotlin.extensions.isZero
import com.merseyside.merseyLib.kotlin.extensions.minByNullable

@SuppressLint("NotifyDataSetChanged")
interface AdapterListUtils<Parent, Model: AdapterParentViewModel<out Parent, Parent>> :
    HasOnItemClickListener<Parent> {

    @InternalAdaptersApi
    val modelList: MutableList<Model>
    val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>

    @InternalAdaptersApi
    fun createModels(items: List<Parent>): List<Model>

    fun add(item: Parent) {
        addModels(createModels(listOf(item)))
        adapter.notifyItemInserted(adapter.itemCount - 1)
    }

    fun add(items: List<Parent>) {
        val startPosition = adapter.itemCount - 1
        addModels(createModels(items))
        adapter.notifyItemRangeChanged(startPosition, items.size)
    }

    @InternalAdaptersApi
    fun add(model: Model) {
        modelList.add(model)
    }

    fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        TODO("Not implemented")
    }

    fun update(item: Parent): Boolean {
        TODO("Not implemented")
    }

    @Throws(NotImplementedError::class)
    fun setFilter(query: String): Int {
        throw NotImplementedError()
    }

    @Throws(NotImplementedError::class)
    fun setFilterAsync(query: String, callback: (filteredCount: Int) -> Unit = {}) {
        throw NotImplementedError()
    }

    fun filter(model: Model, query: String): Boolean {
        return true
    }

    fun filter(model: Model, key: String, filterObj: Any): Boolean {
        return true
    }

    @Throws(IllegalArgumentException::class)
    @InternalAdaptersApi
    open fun notifyModelChanged(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable> = emptyList()
    ): Int = try {
        val position = getPositionOfModel(model)
        adapter.notifyItemChanged(position, payloads)
        position
    } catch (e: IllegalArgumentException) {
        Logger.log("Skip notify item change!")
        throw e
    }

    @Throws(IllegalArgumentException::class)
    fun getPositionOfItem(item: Parent): Int {
        modelList.forEachIndexed { index, t ->
            if (t.areItemsTheSame(item)) return index
        }

        throw IllegalArgumentException("No data found")
    }

    @Throws(IllegalArgumentException::class)
    fun getPositionOfModel(model: Model): Int {
        modelList.forEachIndexed { index, t ->
            if (t == model) return index
        }

        throw IllegalArgumentException("No data found")
    }

    @InternalAdaptersApi
    fun find(item: Parent): Model? {
        modelList.forEach {
            if (it.areItemsTheSame(item)) {
                return it
            }
        }

        return null
    }

    fun getModelByPosition(position: Int): Model {
        return modelList[position]
    }

    fun getItemByPosition(position: Int): Parent {
        return getModelByPosition(position).item
    }

    @InternalAdaptersApi
    fun addModels(list: List<Model>) {
        modelList.addAll(list)
    }

    @InternalAdaptersApi
    fun getModelByItem(item: Parent): Model? {
        return modelList.firstOrNull { it.areItemsTheSame(item) }
    }

    fun remove(item: Parent): Boolean {
        val foundObj = getModelByItem(item)

        return if (foundObj != null) {
            remove(foundObj)
        } else false
    }

    fun remove(items: List<Parent>): Boolean {
        val removeList = items.mapNotNull { getModelByItem(it) }

        val removed = modelList.removeAll(removeList)
        if (removed) {
            notifyItemsRemoved(getSmallestPosition(removeList))
        }

        return removed
    }

    @InternalAdaptersApi
    fun remove(model: Model): Boolean {
        val position = getPositionOfModel(model)
        val removed = modelList.remove(model)
        notifyItemsRemoved(position)

        return removed
    }

    private fun removeList(models: List<Model>) {
        if (models.isNotEmpty()) {
            val smallestPosition = getSmallestPosition(models)
            modelList.removeAll(models)

            notifyItemsRemoved(smallestPosition)
        }
    }

    private fun getSmallestPosition(models: List<Model>): Int {
        return run minValue@{

            models.minByNullable {
                try {
                    val position = getPositionOfModel(it)

                    if (position.isZero()) return@minByNullable position
                    else position
                } catch (e: IllegalArgumentException) {
                    null
                }
            }?.getPosition() ?: 0
        }
    }

    fun notifyItemsRemoved(startsWithPosition: Int) {
        if (startsWithPosition < adapter.itemCount - 1) {
            (startsWithPosition until adapter.itemCount).forEach { index ->
                modelList[index].onPositionChanged(index)
            }
        }

        adapter.notifyDataSetChanged()
    }

    fun clear() {
        modelList.clear()
        adapter.notifyDataSetChanged()
    }

    fun getAllModels(): List<Model> {
        return modelList.toList()
    }

    /**
     * @return true if modelList has items else - false
     */
    fun isEmpty(): Boolean = modelList.isEmpty()

    fun isNotEmpty(): Boolean = !isEmpty()

    @Throws(IndexOutOfBoundsException::class)
    fun first(): Parent {
        try {
            return getModelByPosition(0).item
        } catch (e: Exception) {
            throw IndexOutOfBoundsException("List is empty")
        }
    }

    @Throws(IndexOutOfBoundsException::class)
    fun last(): Parent {
        try {
            return getModelByPosition(adapter.itemCount - 1).item
        } catch (e: Exception) {
            throw IndexOutOfBoundsException("List is empty")
        }
    }

    @InternalAdaptersApi
    private fun getModelsByItems(items: List<Parent>): List<Model> {
        return items.mapNotNull { item -> modelList.find { model -> model.areItemsTheSame(item) } }
    }

    fun getAll(): List<Parent> {
        return modelList.map { it.item }
    }

    fun notifyUpdateAll() {
        modelList.forEach { it.notifyUpdate() }
    }

    fun notifyAdapterRemoved() {}

    fun isPayloadsValid(payloads: List<AdapterParentViewModel.Payloadable>): Boolean {
        return payloads.isNotEmpty() &&
                !payloads.contains(AdapterParentViewModel.Payloadable.None)
    }

    fun onPayloadable(
        holder: TypedBindingHolder<Model>,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {}
}