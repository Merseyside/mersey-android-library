package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.feature.filter.interfaces.FilterFeature
import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.listDelegates.BaseListChangeDelegate
import com.merseyside.adapters.listDelegates.ListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.kotlin.logger.ILogger

abstract class FilterListChangeDelegate<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    filterFeature: FilterFeature<Parent, Model>
) : BaseListChangeDelegate<Parent, Model>(), ILogger {

    abstract val filterFeature: FilterFeature<Parent, Model>

    protected abstract val listChangeDelegate: ListChangeDelegate<Parent, Model>

    override val listActions: AdapterListActions<Parent, Model>
        get() = listChangeDelegate.listActions

    protected val mutAllModelList: MutableList<Model> = ArrayList()
    protected val allModelList: List<Model> = mutAllModelList
        get() = field.ifEmpty { getModels() }

    protected val filteredList: List<Model>
        get() = getModels()

    init {
        filterFeature.initListProviders(
            fullListProvider = { allModelList },
            filteredListProvider = { filteredList }
        )

        filterFeature.setFilterCallback(object : FilterFeature.FilterCallback<Model> {
            override fun onFiltered(models: List<Model>) {
                if (mutAllModelList.isEmpty()) mutAllModelList.addAll(getModels())
                update(models)
            }

            override fun onFilterStateChanged(isFiltered: Boolean) {
                if (!isFiltered) {
                    addModels(allModelList)
                    mutAllModelList.clear()
                }
            }
        })
    }

    override fun add(items: List<Parent>): List<Model> {
        with(filterFeature) {
            return if (!isFiltered) {
                listChangeDelegate.add(items)
            } else {
                val models = createModels(items)
                mutAllModelList.addAll(models)
                addModels(filter(models))
            }
        }
    }

    override fun remove(item: Parent): Model? {
        with(filterFeature) {
            return listChangeDelegate.remove(item).also {
                if (isFiltered) {
                    val model = it ?: getModelByItem(item, mutAllModelList)
                    model?.let { mutAllModelList.remove(model) }
                }
            }
        }
    }

    override fun removeOldItems(items: List<Parent>, models: List<Model>): Boolean {
        val modelsToRemove = findOldItems(items, mutAllModelList)
        mutAllModelList.removeAll(modelsToRemove)
        return super.removeOldItems(items, models)
    }

    override fun removeAll() {
        listChangeDelegate.removeAll()
        mutAllModelList.clear()
    }

    override fun tryToUpdateWithItem(item: Parent): Model? {
        var model = super.tryToUpdateWithItem(item)
        return if (model == null) {
            model = getModelByItem(item, mutAllModelList)
            model?.also { it.payload(item) }
        } else model
    }

    override fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        return listChangeDelegate.update(updateRequest)
    }

    private fun update(models: List<Model>) {
        super.removeOldItems(models.map { it.item }, filteredList)
        super.addModels(models)
    }

    protected fun isFiltered() = filterFeature.isFiltered

    protected fun areListsEquals() = filteredList.size == allModelList.size

    override val tag = "FilterFeature"
}

internal typealias Filters = MutableMap<String, Any>