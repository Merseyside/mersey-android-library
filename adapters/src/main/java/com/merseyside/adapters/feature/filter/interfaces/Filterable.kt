package com.merseyside.adapters.feature.filter.interfaces

import com.merseyside.adapters.model.AdapterParentViewModel

interface Filterable<Parent, Model : AdapterParentViewModel<out Parent, Parent>> {
    val filter: FilterFeature<Parent, Model>

    fun addFilter(key: String, filter: Any) {
        this.filter.addFilter(key, filter)
    }

    fun removeFilter(key: String) {
        filter.removeFilter(key)
    }

    fun clearFilters() {
        filter.clearFilters()
    }

    fun applyFilters() {
        filter.apply()
    }

    fun addAndApplyFilter(key: String, filter: Any) {
        addFilter(key, filter)
        applyFilters()
    }

    fun removeAndApplyFilter(key: String) {
        removeFilter(key)
        applyFilters()
    }

    fun getFilteredItems(): List<Model> {
        return filter.provideFilteredList()
    }
}