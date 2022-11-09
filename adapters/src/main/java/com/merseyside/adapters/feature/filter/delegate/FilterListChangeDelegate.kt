package com.merseyside.adapters.feature.filter.delegate

import com.merseyside.adapters.feature.filter.FilterFeature
import com.merseyside.adapters.listDelegates.BaseListChangeDelegate
import com.merseyside.adapters.listDelegates.ListChangeDelegate
import com.merseyside.adapters.listDelegates.utils.UpdateTransaction
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.kotlin.logger.ILogger
import com.merseyside.merseyLib.kotlin.logger.log

abstract class FilterListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    filterFeature: FilterFeature<Parent, Model>
) : BaseListChangeDelegate<Parent, Model>(), ILogger {

    abstract val filterFeature: FilterFeature<Parent, Model>

    protected abstract val listChangeDelegate: ListChangeDelegate<Parent, Model>

    override fun getModels(): List<Model> {
        return listChangeDelegate.getModels()
    }

    protected val mutAllModelList: MutableList<Model> = ArrayList()
    protected val allModelList: List<Model> = mutAllModelList
        get() = field.ifEmpty { filteredList }

    protected val filteredList: List<Model>
        get() = getModels()

    init {
        filterFeature.apply {
            initListProviders(
                fullListProvider = { allModelList },
                filteredListProvider = { filteredList }
            )

            setFilterCallback(object : FilterFeature.FilterCallback<Model> {
                override suspend fun onFiltered(models: List<Model>) {
                    if (mutAllModelList.isEmpty()) mutAllModelList.addAll(getModels())
                    setModels(models)
                }

                override suspend fun onFilterStateChanged(isFiltered: Boolean) {
                    if (!isFiltered) {
                        setModels(allModelList)
                        mutAllModelList.clear()
                    }
                }
            })
        }
    }

    override suspend fun createModel(item: Parent): Model {
        return listChangeDelegate.createModel(item)
    }

    override suspend fun add(item: Parent): Model {
        with(filterFeature) {
            return if (!isFiltered) {
                listChangeDelegate.add(item)
            } else {
                val model = createModel(item)
                mutAllModelList.add(model)
                addModel(model)

                model
            }
        }
    }

    override suspend fun add(items: List<Parent>): List<Model> {
        with(filterFeature) {
            return if (!isFiltered) {
                listChangeDelegate.add(items)
            } else {
                val models = createModels(items)
                mutAllModelList.addAll(models)
                addModels(models)

                models
            }
        }
    }

    override suspend fun addOrUpdate(items: List<Parent>) {
        if (allModelList.isEmpty()) add(items)
        else update(items)
    }

    override suspend fun remove(item: Parent): Model? {
        with(filterFeature) {
            return listChangeDelegate.remove(item).also {
                if (isFiltered) {
                    val model = it ?: getModelByItem(item, allModelList)
                    model?.let { mutAllModelList.remove(model) }
                }
            }
        }
    }

    override suspend fun removeModel(model: Model): Boolean {
        return listChangeDelegate.removeModel(model)
    }

    override suspend fun removeModels(models: List<Model>): Boolean {
        return listChangeDelegate.removeModels(models)
    }

    override suspend fun clear() {
        mutAllModelList.clear()
        listChangeDelegate.clear()
    }

    final override suspend fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        return if (!isFiltered()) {
            listChangeDelegate.update(updateRequest)
        } else {
            val updateTransaction = getUpdateTransaction(updateRequest, allModelList)
            applyUpdateTransaction(updateTransaction)
        }
    }

    override suspend fun applyUpdateTransaction(
        updateTransaction: UpdateTransaction<Parent, Model>
    ): Boolean {
        with(updateTransaction) {
            if (modelsToRemove.isNotEmpty()) {
                removeFromAllModels(modelsToRemove)
                removeModels(modelsToRemove)
            }

            if (modelsToUpdate.isNotEmpty()) {
                updateModels(modelsToUpdate)
            }

            if (itemsToAdd.isNotEmpty()) {
                add(itemsToAdd)
            }
        }

        return !updateTransaction.isEmpty()
    }

    override suspend fun setModels(models: List<Model>) {
        listChangeDelegate.setModels(models)
    }

    override suspend fun addModel(model: Model): Boolean {
        if (isFiltered()) {
            if (filterFeature.filter(model)) listChangeDelegate.addModel(model)
        }

        return true
    }

    override suspend fun addModels(models: List<Model>): Boolean {
        if (isFiltered()) {
            val filteredModels = filterFeature.filter(models)
            listChangeDelegate.addModels(filteredModels)
        }
        return true
    }

    override suspend fun updateModel(model: Model, item: Parent): Boolean {
        val isUpdated = listChangeDelegate.updateModel(model, item)
        if (isUpdated && isFiltered()) {
            val filtered = filterFeature.filter(model)
            if (!filtered) removeModel(model)
        }

        return isFiltered()
    }

    internal open suspend fun removeFromAllModels(model: Model): Boolean {
        return mutAllModelList.remove(model)
    }

    internal suspend fun removeFromAllModels(models: List<Model>) {
        models.forEach { model -> removeFromAllModels(model) }
    }

    override suspend fun getModelByItem(item: Parent): Model? {
        return listChangeDelegate.getModelByItem(item)
    }

    protected fun isFiltered() = filterFeature.isFiltered

    protected fun areListsEquals() = filteredList.size == allModelList.size

    override val tag = "FilterFeature"
}

internal typealias Filters = MutableMap<String, Any>