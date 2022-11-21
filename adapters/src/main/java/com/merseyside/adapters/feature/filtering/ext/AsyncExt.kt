package com.merseyside.adapters.feature.filtering.ext

import com.merseyside.adapters.feature.filtering.AdapterFilter
import com.merseyside.adapters.feature.filtering.QueryAdapterFilter

/**
 * @return true if filters applied, false otherwise.
 */
fun AdapterFilter<*, *>.applyFiltersAsync(onComplete: (Boolean) -> Unit = {}) {
    workManager.doAsync(
        onComplete,
        onError = {
            putAppliedFilters()
            isFiltered = true
            onComplete(false)
        }
    ) { applyFilters() }
}

fun QueryAdapterFilter<*, *>.setQueryAsync(query: String?, onComplete: (Boolean) -> Unit = {}) {
    workManager.doAsync(onComplete) { setQuery(query) }
}