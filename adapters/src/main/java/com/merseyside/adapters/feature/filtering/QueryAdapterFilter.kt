package com.merseyside.adapters.feature.filtering

import com.merseyside.adapters.model.VM
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty

abstract class QueryAdapterFilter<Parent, Model : VM<Parent>> :
    AdapterFilter<Parent, Model>() {

    abstract fun filter(model: Model, query: String): Boolean

    fun setQueryAsync(query: String?, onComplete: (Boolean) -> Unit = {}) {
        workManager.doAsync(onComplete) { setQuery(query) }
    }

    suspend fun setQuery(query: String?): Boolean {
        if (query.isNotNullAndEmpty()) {
            addFilter(QUERY_KEY, query)
        } else {
            removeFilter(QUERY_KEY)
        }
        return applyFilters()
    }

    final override fun filter(model: Model, key: String, filter: Any): Boolean {
        val query = filter as String
        return filter(model, query)
    }

    companion object {
        private val QUERY_KEY = "query_filter_key"
    }
}