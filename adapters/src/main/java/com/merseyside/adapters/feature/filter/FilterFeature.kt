package com.merseyside.adapters.feature.filter

import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.list.AdapterListChangeDelegate
import com.merseyside.merseyLib.kotlin.logger.ILogger

abstract class FilterFeature<Parent, Model : AdapterParentViewModel<out Parent, Parent>> :
    AdapterListChangeDelegate<Parent, Model>, ILogger {

    //override lateinit var adapter: AdapterListUtils<Parent, Model>

    private val filters: Filters by lazy { mutableMapOf() }
    private var notAppliedFilters: Filters = mutableMapOf()

    private var isFiltered = false

    private lateinit var filterCallback: FilterCallback<Model>

    private val allItems: List<Model>
        get() = filterCallback.getAllItems()

    private val currentItems: List<Model>
        get() = filterCallback.getCurrentItems()


    /**
    * If you pass an object as filter be sure isEquals() implemented properly.
    */
    fun addFilter(key: String, filter: Any) {
        val appliedFilter = filters[key]
        if (appliedFilter != filter) {
            notAppliedFilters[key] = filter
        }
    }

    fun removeFilter(key: String) {
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

    /**
     * @return true if filters applied, false otherwise.
     */
    fun apply(): Boolean {
        if (isFiltered && areFiltersEmpty()) {
            postResult(allItems)
        } else if (notAppliedFilters.isEmpty()) {
            log("No new filters added. Filtering skipped!")
            return false
        } else {
            val canFilterCurrentItems = !notAppliedFilters.keys.any { filters.containsKey(it) }
            val filteredModels = if (canFilterCurrentItems) {
                filter(currentItems)
            } else {
                makeAllFiltersNotApplied()
                filter(allItems)
            }

            putAppliedFilters()
            postResult(filteredModels)
        }

        isFiltered = filters.isNotEmpty()
        return true
    }
    

    abstract fun filter(model: Model, key: String, filter: Any): Boolean

    internal fun filter(models: List<Model>): List<Model> {
        return filter(models, filters)
    }

    private fun areFiltersEmpty(): Boolean {
        return filters.isEmpty() && notAppliedFilters.isEmpty()
    }

    private fun postResult(models: List<Model>) {
        filterCallback.onFiltered(models)
    }

    private fun filter(models: List<Model>, filters: Filters = notAppliedFilters): List<Model> {
        return models.filter { model ->
            filters.all { (key, value) ->
                filter(model, key, value)
            }
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
    }

    private fun isFilterAlreadyExists(key: String): Boolean {
        return filters.containsKey(key)
    }

    interface FilterCallback<Model> {
        fun getAllItems(): List<Model>

        fun getCurrentItems(): List<Model>

        fun onFiltered(filteredModels: List<Model>)
    }

    override val tag = "FilterFeature"
}

internal typealias Filters = MutableMap<String, Any>