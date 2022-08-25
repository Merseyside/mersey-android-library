package com.merseyside.adapters.feature.filter.interfaces

import com.merseyside.adapters.feature.filter.FilterFeature
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

    fun applyFiltersAsync(onComplete: (Boolean) -> Unit = {}) {
        filter.applyFiltersAsync(onComplete)
    }

    fun addAndApplyFilter(key: String, filter: Any, onComplete: (Boolean) -> Unit = {}) {
        addFilter(key, filter)
        applyFiltersAsync(onComplete)
    }

    fun removeAndApplyFilter(key: String, onComplete: (Boolean) -> Unit = {}) {
        removeFilter(key)
        applyFiltersAsync(onComplete)
    }

    fun getFilteredItems(): List<Model> {
        return filter.provideFilteredList()
    }
}