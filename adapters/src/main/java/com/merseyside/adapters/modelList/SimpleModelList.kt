package com.merseyside.adapters.modelList

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.merseyLib.kotlin.extensions.move

open class SimpleModelList<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    private val mutModels: MutableList<Model> = ArrayList()
) : ModelList<Parent, Model>() {

    override fun getModels(): List<Model> {
        return mutModels
    }

    override suspend fun onModelUpdated(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {
        val position = getPositionOfModel(model)
        listCallback.onChanged(model, position, payloads)
    }

    override fun get(index: Int): Model {
        return mutModels[index]
    }

    override fun iterator(): Iterator<Model> {
        return listIterator()
    }

    override suspend fun remove(model: Model): Boolean {
        val position = getPositionOfModel(model)
        return try {
            val removedModel = mutModels.removeAt(position)
            listCallback.onRemoved(listOf(removedModel), position)
            true
        } catch (e: IndexOutOfBoundsException) {
            false
        }
    }

    override suspend fun removeAll(models: List<Model>) {
        models.forEach { model -> remove(model) }
    }

    override fun lastIndexOf(element: Model): Int {
        return indexOf(element)
    }

    override fun indexOf(element: Model): Int {
        return mutModels.indexOf(element)
    }

    override suspend fun addAll(models: List<Model>) {
        mutModels.addAll(models)
        listCallback.onInserted(models, size, models.size)
    }

    override suspend fun add(model: Model) {
        mutModels.add(model)
        listCallback.onInserted(listOf(model), size)
    }

    override suspend fun addAll(position: Int, models: List<Model>) {
        mutModels.addAll(position, models)
        listCallback.onInserted(models, position, models.size)
    }

    override suspend fun add(position: Int, model: Model) {
        mutModels.add(position, model)
        listCallback.onInserted(listOf(model), position)
    }

    suspend fun move(fromIndex: Int, toIndex: Int) {
        mutModels.move(fromIndex, toIndex)
        listCallback.onMoved(fromIndex, toIndex)
    }

    override fun clear() {
        mutModels.clear()
    }

    override fun listIterator(): ListIterator<Model> {
        return listIterator(0)
    }

    override fun listIterator(index: Int): ListIterator<Model> {
        return mutModels.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Model> {
        return mutModels.subList(fromIndex, toIndex)
    }

    override val tag: String = "SimpleModelList"
}