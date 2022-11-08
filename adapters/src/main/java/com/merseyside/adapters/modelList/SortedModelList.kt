package com.merseyside.adapters.modelList

import com.merseyside.adapters.extensions.find
import com.merseyside.adapters.extensions.removeAll
import com.merseyside.adapters.extensions.subList
import com.merseyside.adapters.feature.sorting.Comparator
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.list.SortedList
import com.merseyside.adapters.model.VM
import kotlinx.coroutines.runBlocking

class SortedModelList<Parent, Model : VM<Parent>>(
    internal val sortedList: SortedList<Model>,
    private val comparator: Comparator<Parent, Model>
) : ModelList<Parent, Model>() {

    init {
        val callback = object : SortedList.Callback<Model>() {
            override fun onInserted(position: Int, count: Int) {
                val models = getModels().subList(position, position + count - 1)
                onInserted(models, position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                onRemoved(emptyList(), position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                onMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int) {}

            override fun compare(item1: Model, item2: Model): Int {
                return comparator.compare(item1, item2)
            }

            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.areContentsTheSame(newItem.item)
            }

            override fun areItemsTheSame(item1: Model, item2: Model): Boolean {
                return item1.areItemsTheSame(item2.item)
            }
        }

        sortedList.setCallback(callback)
    }

    override fun getModels(): List<Model> {
        return sortedList.getAll()
    }

    override fun getModelByItem(item: Parent): Model? {
        return sortedList.find { it.areItemsTheSame(item) }
    }

    override suspend fun addAll(position: Int, models: List<Model>) {
        throw Exception("Adding by position is not supported.")
    }

    override suspend fun add(position: Int, model: Model) {
        throw Exception("Adding by position is not supported.")
    }

    override fun get(index: Int): Model {
        return sortedList[index]
    }

    override fun iterator(): Iterator<Model> {
        return listIterator()
    }

    override suspend fun remove(model: Model): Boolean {
        return sortedList.remove(model)
    }

    override suspend fun removeAll(models: List<Model>) {
        sortedList.removeAll(models)
    }

    override fun lastIndexOf(element: Model): Int {
        return indexOf(element)
    }

    override fun indexOf(element: Model): Int = runBlocking {
        sortedList.indexOf(element)
    }

    override suspend fun addAll(models: List<Model>) {
        sortedList.addAll(models)
    }

    override suspend fun add(model: Model) {
        sortedList.add(model)
    }

    override suspend fun onModelUpdated(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {
        val position = getPositionOfModel(model)
        sortedList.recalculatePositionOfItemAt(position)
        onChanged(model, getPositionOfModel(model), payloads)
    }

    override fun clear() {
        sortedList.clear()
    }

    override fun listIterator(): ListIterator<Model> {
        return listIterator(0)
    }

    override fun listIterator(index: Int): ListIterator<Model> {
        return SortedListIterator(index, sortedList)
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Model> {
        return sortedList.subList(fromIndex, toIndex)
    }
}

internal class SortedListIterator<Model>(
    startIndex: Int = 0,
    private val sortedList: SortedList<Model>
) : ModelListIterator<Model>(startIndex) {
    override fun getItem(index: Int): Model {
        return sortedList[index]
    }

    override fun getSize(): Int {
        return sortedList.size()
    }
}