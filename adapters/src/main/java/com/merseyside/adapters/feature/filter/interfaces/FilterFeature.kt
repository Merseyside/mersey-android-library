package com.merseyside.adapters.feature.filter.interfaces

import com.merseyside.adapters.feature.filter.Filters
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.merseyLib.kotlin.logger.ILogger

abstract class FilterFeature<Parent, Model : AdapterParentViewModel<out Parent, Parent>> : ILogger {

    private var filterCallback: FilterCallback<Model>? = null

    internal val filters: Filters by lazy { mutableMapOf() }
    internal var notAppliedFilters: Filters = mutableMapOf()

    var isFiltered = false
        private set(value) {
            if (value != field) {
                field = value
                filterCallback?.onFilterStateChanged(value)
            }
        }

    val itemsCount: Int
        get() = provideFilteredList().size

    internal var provideFullList: () -> List<Model> = { emptyList() }
    internal var provideFilteredList: () -> List<Model> = { provideFullList() }

    internal fun initListProviders(
        fullListProvider: () -> List<Model>,
        filteredListProvider: () -> List<Model>
    ) {
        this.provideFullList = fullListProvider
        this.provideFilteredList = filteredListProvider
    }
    /**
     * If you pass an object as filter be sure isEquals() implemented properly.
     */
    open fun addFilter(key: String, filter: Any) {
        val appliedFilter = filters[key]
        if (appliedFilter != filter) {
            notAppliedFilters[key] = filter
        }
    }

    open fun removeFilter(key: String) {
        val appliedFilter = filters[key]
        if (isFiltered) {
            if (appliedFilter != null) {
                filters.remove(key)
                makeAllFiltersNotApplied()
            }
        }
    }

    fun clearFilters() {
        filters.clear()
        notAppliedFilters.clear()
    }

    fun getAllModels(): List<Model> {
        return provideFullList()
    }

    /**
     * @return true if filters applied, false otherwise.
     */
    open fun apply(): Boolean {
        if (isFiltered && areFiltersEmpty()) {
            isFiltered = false
        } else if (notAppliedFilters.isEmpty()) {
            log("No new filters added. Filtering skipped!")
            return false
        } else {
            val canFilterCurrentItems = filters.isNotEmpty() &&
                    !notAppliedFilters.keys.any { filters.containsKey(it) }

            val filteredModels = if (canFilterCurrentItems) {
                filter(provideFilteredList(), notAppliedFilters)
            } else {
                makeAllFiltersNotApplied()
                filter(provideFullList(), notAppliedFilters)
            }

            putAppliedFilters()
            postResult(filteredModels)
            isFiltered = true
        }

        return true
    }

    internal fun areFiltersEmpty(): Boolean {
        return filters.isEmpty() && notAppliedFilters.isEmpty()
    }

    private fun postResult(models: List<Model>) {
        filterCallback?.onFiltered(models)
    }

    abstract fun filter(model: Model, key: String, filter: Any): Boolean

    internal fun filter(model: Model): Boolean {
        return filter(model, filters)
    }

    internal fun filter(models: List<Model>): List<Model> {
        return filter(models, filters)
    }

    internal open fun filter(model: Model, filters: Filters): Boolean {
        return filters.all { (key, value) ->
            if (model.filterable) {
                filter(model, key, value)
            } else true
        }
    }

    private fun filter(models: List<Model>, filters: Filters): List<Model> {
        return models.filter { model ->
            filter(model, filters)
        }
    }

    private fun putAppliedFilters() {
        filters.putAll(notAppliedFilters)
        notAppliedFilters.clear()
    }

    private fun makeAllFiltersNotApplied() {
        notAppliedFilters = (filters + notAppliedFilters).toMutableMap()
        filters.clear()
    }

    internal fun setFilterCallback(callback: FilterCallback<Model>) {
        this.filterCallback = callback
        if (isFiltered) {
            callback.onFilterStateChanged(true)
        }
    }

    private fun isFilterAlreadyExists(key: String): Boolean {
        return filters.containsKey(key)
    }

    private fun logFilters(prefix: String = "") {
        filters.log("$prefix filters =")
        notAppliedFilters.log("$prefix not applied =")
    }

    interface FilterCallback<Model> {
        fun onFiltered(models: List<Model>)

        fun onFilterStateChanged(isFiltered: Boolean)
    }

    override val tag = "FilterFeature"
}