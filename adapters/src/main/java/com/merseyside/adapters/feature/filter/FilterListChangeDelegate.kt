package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.listDelegates.BaseListChangeDelegate
import com.merseyside.adapters.listDelegates.ListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineWorkManager
import com.merseyside.merseyLib.kotlin.logger.ILogger

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
        get() = field.ifEmpty { getModels() }

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

    final override suspend fun createModels(items: List<Parent>): List<Model> {
        return items.map { item -> createModel(item) }
    }

    override suspend fun add(item: Parent): Model {
        with(filterFeature) {
            return if (!isFiltered) {
                listChangeDelegate.add(item)
            } else {
                val model = createModel(item)
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
                addModels(models)

                models
            }
        }
    }

    override suspend fun remove(item: Parent): Model? {
        with(filterFeature) {
            return listChangeDelegate.remove(item).also {
                if (isFiltered) {
                    val model = it ?: getModelByItem(item, mutAllModelList)
                    model?.let { mutAllModelList.remove(model) }
                }
            }
        }
    }

    override suspend fun removeModel(model: Model): Boolean {
        if (isFiltered()) {
            mutAllModelList.remove(model)
        }

        return listChangeDelegate.removeModel(model)
    }

    override suspend fun removeModels(models: List<Model>): Boolean {
        if (isFiltered()) {
            mutAllModelList.removeAll(models)
        }

        return listChangeDelegate.removeModels(models)
    }

    override suspend fun removeAll() {
        mutAllModelList.clear()
        listChangeDelegate.removeAll()
    }

    override suspend fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        return if (!isFiltered()) {
            listChangeDelegate.update(updateRequest)
        } else {
            val updateTransaction = getUpdateTransaction(updateRequest, allModelList).log()

            with(updateTransaction) {
                if (modelsToRemove.isNotEmpty()) {
                    removeModels(modelsToRemove)
                }

                if (modelsToUpdate.isNotEmpty()) {
                    updateModels(modelsToUpdate)
                }

                if (itemsToAdd.isNotEmpty()) {
                    add(itemsToAdd)
                }
            }

            !updateTransaction.isEmpty()
        }
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
        mutAllModelList.addAll(models)
        models.forEach { model -> addModel(model) }
        return true
    }

    override suspend fun updateModel(model: Model, item: Parent): Boolean {
        return listChangeDelegate.updateModel(model, item)
    }

    protected fun isFiltered() = filterFeature.isFiltered

    protected fun areListsEquals() = filteredList.size == allModelList.size

    override val tag = "FilterFeature"
}

internal typealias Filters = MutableMap<String, Any>