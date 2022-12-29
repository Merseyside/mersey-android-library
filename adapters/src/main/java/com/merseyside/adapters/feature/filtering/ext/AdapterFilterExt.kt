package com.merseyside.adapters.feature.filtering.ext

import com.merseyside.adapters.feature.filtering.AdapterFilter
import com.merseyside.adapters.model.VM

fun AdapterFilter<*, *>.addAndApplyAsync(key: String, filter: Any, onComplete: (Boolean) -> Unit = {}) {
    workManager.doAsync(onComplete) {
        addFilter(key, filter)
        applyFilters()
    }
}

fun AdapterFilter<*, *>.removeAndApplyAsync(key: String, onComplete: (Boolean) -> Unit = {}) {
    workManager.doAsync(onComplete) {
        removeFilter(key)
        applyFilters()
    }
}

fun AdapterFilter<*, *>.addFilterAsync(key: String, filter: Any, onComplete: (Unit) -> Unit = {}) {
    workManager.doAsync(onComplete) { addFilter(key, filter) }
}

fun AdapterFilter<*, *>.removeFilterAsync(key: String, onComplete: (Unit) -> Unit = {}) {
    workManager.doAsync(onComplete) { removeFilter(key) }
}

fun <Parent, Model> AdapterFilter<Parent, Model>.getFilteredItems(): List<Model>
    where Model : VM<Parent> {
    return provideFilteredList()
}