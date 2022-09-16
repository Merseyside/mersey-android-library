package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.feature.filter.delegate.Filters
import com.merseyside.adapters.feature.filter.interfaces.Filterable
import com.merseyside.adapters.model.NestedAdapterParentViewModel
import com.merseyside.merseyLib.kotlin.extensions.isNotZero

abstract class NestedFilterFeature<Parent, Model : NestedAdapterParentViewModel<out Parent, Parent, *>> :
    FilterFeature<Parent, Model>() {

    internal lateinit var getFilterableByModel: (Model) -> Filterable<*, *>?

    final override fun addFilter(key: String, filter: Any) {
        super.addFilter(key, filter)
        val fullList = provideFullList()
        fullList.forEach { model ->
            val filterable = getFilterableByModel(model)
            filterable?.addFilter(key, filter)
        }
    }

    final override fun removeFilter(key: String) {
        super.removeFilter(key)
        val fullList = provideFullList()
        fullList.forEach { model ->
            val filterable = getFilterableByModel(model)
            filterable?.removeFilter(key)
        }
    }

    final override suspend fun filter(model: Model, filters: Filters): Boolean {
        val isFiltered = super.filter(model, filters)
        val filterable = getFilterableByModel(model)

        if (filterable != null) {
            filterable.filter.applyFilters()
            val innerItemsCount = filterable.getFilteredItems().size

            if (isFiltered) return filter(model, innerItemsCount.isNotZero())
        }

        return isFiltered
    }

    open fun filter(model: Model, hasItems: Boolean): Boolean {
        return hasItems
    }

    internal suspend fun setFilters(filterable: Filterable<*, *>) {
        val filterFeature = filterable.filter
        filters.forEach { (key, value) ->
            filterFeature.addFilter(key, value)
        }

        filterFeature.applyFilters()
    }

    override suspend fun cancelFiltering() {
        super.cancelFiltering()
        provideFullList()
            .mapNotNull { model -> getFilterableByModel(model) }
            .forEach { filterable -> filterable.filter.applyFilters() }
    }

}