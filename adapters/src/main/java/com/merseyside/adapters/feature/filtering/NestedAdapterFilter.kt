package com.merseyside.adapters.feature.filtering

import com.merseyside.adapters.feature.filtering.listManager.Filters
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.merseyLib.kotlin.extensions.isNotZero

abstract class NestedAdapterFilter<Parent, Model : NestedAdapterParentViewModel<out Parent, Parent, *>> :
    AdapterFilter<Parent, Model>() {

    internal lateinit var getAdapterFilterByModel: suspend (Model) -> AdapterFilter<*, *>?

    final override suspend fun addFilter(key: String, filter: Any) {
        super.addFilter(key, filter)
        val fullList = provideFullList()
        fullList.forEach { model ->
            val filterable = getAdapterFilterByModel(model)
            filterable?.addFilter(key, filter)
        }
    }

    final override suspend fun removeFilter(key: String) {
        super.removeFilter(key)
        val fullList = provideFullList()
        fullList.forEach { model ->
            val adapterFilter = getAdapterFilterByModel(model)
            adapterFilter?.removeFilter(key)
        }
    }

    final override suspend fun filter(model: Model, filters: Filters): Boolean {
        val isFiltered = super.filter(model, filters)
        val adapterFilter = getAdapterFilterByModel(model)

        if (adapterFilter != null) {
            adapterFilter.applyFilters()
            val innerItemsCount = adapterFilter.itemsCount

            if (isFiltered) return filter(model, innerItemsCount.isNotZero())
        }

        return isFiltered
    }

    open fun filter(model: Model, hasItems: Boolean): Boolean {
        return hasItems
    }

    internal suspend fun initAdapterFilter(adapterFilter: AdapterFilter<*, *>) {
        filters.forEach { (key, value) ->
            adapterFilter.addFilter(key, value)
        }

        adapterFilter.applyFilters()
    }

    override suspend fun cancelFiltering() {
        super.cancelFiltering()
        provideFullList()
            .mapNotNull { model -> getAdapterFilterByModel(model) }
            .forEach { adapterFilter -> adapterFilter.applyFilters() }
    }

}