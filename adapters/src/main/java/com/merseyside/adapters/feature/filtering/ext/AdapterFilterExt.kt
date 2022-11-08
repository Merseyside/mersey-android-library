package com.merseyside.adapters.feature.filtering.ext

import com.merseyside.adapters.feature.filtering.AdapterFilter
import com.merseyside.adapters.model.VM

fun AdapterFilter<*, *>.addAndApply(key: String, filter: Any, onComplete: (Boolean) -> Unit = {}) {
    addFilter(key, filter)
    applyFiltersAsync(onComplete)
}

fun AdapterFilter<*, *>.removeAndApply(key: String, onComplete: (Boolean) -> Unit = {}) {
    removeFilter(key)
    applyFiltersAsync(onComplete)
}

fun <Parent, Model> AdapterFilter<Parent, Model>.getFilteredItems(): List<Model>
    where Model : VM<Parent> {
    return provideFilteredList()
}