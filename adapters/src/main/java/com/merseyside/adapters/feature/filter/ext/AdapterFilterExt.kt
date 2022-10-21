package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.model.AdapterParentViewModel

fun AdapterFilter<*, *>.addAndApply(key: String, filter: Any, onComplete: (Boolean) -> Unit = {}) {
    addFilter(key, filter)
    applyFiltersAsync(onComplete)
}

fun AdapterFilter<*, *>.removeAndApply(key: String, onComplete: (Boolean) -> Unit = {}) {
    removeFilter(key)
    applyFiltersAsync(onComplete)
}

fun <Parent, Model> AdapterFilter<Parent, Model>.getFilteredItems(): List<Model>
    where Model : AdapterParentViewModel<out Parent, Parent> {
    return provideFilteredList()
}