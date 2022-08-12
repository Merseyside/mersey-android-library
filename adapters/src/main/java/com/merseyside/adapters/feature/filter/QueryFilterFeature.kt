package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty

abstract class QueryFilterFeature<Parent, Model : AdapterParentViewModel<out Parent, Parent>> : FilterFeature<Parent, Model>() {

    abstract fun filter(model: Model, query: String): Boolean

    fun setQuery(query: String?) {
        if (query.isNotNullAndEmpty()) {
            addFilter(QUERY_KEY, query)
        } else {
            removeFilter(QUERY_KEY)
        }
        apply()
    }

    final override fun filter(model: Model, key: String, filter: Any): Boolean {
        val query = filter as String
        return filter(model, query)
    }

    companion object {
        private val QUERY_KEY = "query_filter_key"
    }
}