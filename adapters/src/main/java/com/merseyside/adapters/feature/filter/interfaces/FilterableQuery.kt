package com.merseyside.adapters.feature.filter.interfaces

import com.merseyside.adapters.model.AdapterParentViewModel

interface FilterableQuery<Parent, Model : AdapterParentViewModel<out Parent, Parent>> :
    Filterable<Parent, Model> {

    override val filter: QueryFilterFeature<Parent, Model>

    fun setQuery(query: String?) {
        filter.setQuery(query)
    }

    override fun applyFilters() {
        throw UnsupportedOperationException()
    }

    override fun addFilter(key: String, filter: Any) {
        throw UnsupportedOperationException()
    }

    override fun addAndApplyFilter(key: String, filter: Any) {
        throw UnsupportedOperationException()
    }

    override fun removeFilter(key: String) {
        throw UnsupportedOperationException()
    }
}