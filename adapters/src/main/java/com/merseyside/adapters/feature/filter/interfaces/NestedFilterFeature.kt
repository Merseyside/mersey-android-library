package com.merseyside.adapters.feature.filter.interfaces

import com.merseyside.adapters.feature.filter.FilterFeature
import com.merseyside.adapters.feature.filter.Filters
import com.merseyside.adapters.model.NestedAdapterParentViewModel

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

    final override fun filter(model: Model, filters: Filters): Boolean {
        val isFiltered = super.filter(model, filters)
        val filterable = getFilterableByModel(model)

        if (filterable != null) {
            val innerItemsCount = filterable.getFilteredItems().size

            if (isFiltered) return filter(model, innerItemsCount)
        }

        return isFiltered
    }

    final override fun apply(): Boolean {
        provideFullList().forEach { model ->
            getFilterableByModel(model)?.applyFilters()
        }
        return super.apply()
    }

    abstract fun filter(model: Model, innerAdapterItemsCount: Int): Boolean

    internal fun setFilters(filterable: Filterable<*, *>) {
        val filterFeature = filterable.filter
        filters.forEach { (key, value) ->
            filterFeature.addFilter(key, value)
        }

        filterFeature.apply()
    }

}