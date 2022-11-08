package com.merseyside.adapters.feature.filtering.listManager

import com.merseyside.adapters.config.update.UpdateLogic
import com.merseyside.adapters.feature.filtering.AdapterFilter
import com.merseyside.adapters.interfaces.base.AdapterActions
import com.merseyside.adapters.listManager.ModelListManager
import com.merseyside.adapters.modelList.ModelList
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.kotlin.extensions.move
import com.merseyside.merseyLib.kotlin.logger.ILogger
import com.merseyside.adapters.model.VM

open class FilterListManager<Parent, Model : VM<Parent>>(
    override val modelList: ModelList<Parent, Model>,
    override val adapterActions: AdapterActions<Parent, Model>,
    val adapterFilter: AdapterFilter<Parent, Model>
): ModelListManager<Parent, Model>, ILogger {

    override lateinit var updateLogic: UpdateLogic<Parent, Model>
    override val hashMap: MutableMap<Any, Model> = mutableMapOf()

    private var isFiltering: Boolean = false

    protected val isFiltered: Boolean
        get() = adapterFilter.isFiltered

    protected val mutAllModelList: MutableList<Model> = ArrayList()
    protected val allModelList: List<Model> = mutAllModelList

    protected val filteredList: List<Model>
        get() = modelList

    init {
        adapterFilter.apply {
            initListProviders(
                fullListProvider = { allModelList },
                filteredListProvider = { filteredList }
            )

            setFilterCallback(object : AdapterFilter.FilterCallback<Model> {
                override suspend fun onFiltered(models: List<Model>) {
                    filterUpdate { update(models) }
                }

                override suspend fun onFilterStateChanged(isFiltered: Boolean) {
                    if (!isFiltered) {
                        filterUpdate { update(allModelList) }
                    }
                }
            })
        }
    }

    override suspend fun addModel(model: Model): Boolean {
        if (!isFiltering) {
            mutAllModelList.add(model)
        }

        return if (adapterFilter.filter(model)) super.addModel(model)
        else false
    }

    override suspend fun removeModel(model: Model): Boolean {
        if (!isFiltering) {
            mutAllModelList.remove(model)
        }

        return super.removeModel(model)
    }

    override suspend fun removeModels(models: List<Model>): Boolean {
        models.forEach { model -> removeModel(model) }
        return true
    }

    override suspend fun getModelByItem(item: Parent): Model? {
        return getModelByItem(item, allModelList)
    }

    @InternalAdaptersApi
    override suspend fun clear() {
        mutAllModelList.clear()
        super.clear()
    }

    final override suspend fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        return update(updateRequest, allModelList)
    }


    override suspend fun addModels(models: List<Model>): Boolean {
        if (!isFiltering) {
            mutAllModelList.addAll(models)
        }

        val filteredModels = if (isFiltered) {
             adapterFilter.filter(models)
        } else models

        return super.addModels(filteredModels)
    }

    override suspend fun updateModel(model: Model, item: Parent): Boolean {
        super.updateModel(model, item)
        if (isFiltered) {
            val filtered = adapterFilter.filter(model)
            if (!filtered) removeModel(model)
        }

        return true
    }

    protected fun areListsEquals() = filteredList.size == allModelList.size

    override suspend fun addModel(position: Int, model: Model): Boolean {
        requireValidPosition(position, allModelList)
        if (!isFiltering) {
            mutAllModelList.add(position, model)
        }

        if (isFiltered) {
            if (adapterFilter.filter(model)) {
                val filteredPosition = calculatePositionInFilteredList(position)
                super.addModel(filteredPosition, model)
            }
        } else {
            super.addModel(position, model)
        }

        return true
    }

    override suspend fun addModels(position: Int, models: List<Model>): Boolean {
        models.forEachIndexed { index, parent ->
            addModel(position + index, parent)
        }

        return true
    }

    private fun calculatePositionInFilteredList(desiredPosition: Int): Int {
        fun getNearestFilteredPosition(): Int {
            for (index in desiredPosition..allModelList.lastIndex) {
                if (filteredList.contains(allModelList[index])) return index
            }

            for (index in desiredPosition downTo 0) {
                if (filteredList.contains(allModelList[index])) return index
            }

            return 0
        }

        return if (areListsEquals()) {
            desiredPosition
        } else {
            val pos = getNearestFilteredPosition()
            if (pos < desiredPosition) pos + 1
            else pos
        }
    }

    override suspend fun move(oldModel: Model, fromPosition: Int, toPosition: Int) {
        if (isFiltered) {
            mutAllModelList.move(fromPosition, toPosition)
        } else super.move(oldModel, fromPosition, toPosition)
    }

    suspend fun filterUpdate(block: suspend () -> Unit) {
        isFiltering = true
        block()
        isFiltering = false
    }

    override val tag = "FilterFeature"
}

internal typealias Filters = MutableMap<String, Any>