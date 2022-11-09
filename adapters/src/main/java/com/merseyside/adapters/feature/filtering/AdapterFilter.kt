package com.merseyside.adapters.feature.filtering

import com.merseyside.adapters.config.contract.HasWorkManager
import com.merseyside.adapters.feature.filtering.listManager.Filters
import com.merseyside.adapters.utils.runWithDefault
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineQueue
import com.merseyside.merseyLib.kotlin.logger.ILogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import com.merseyside.adapters.model.VM
import com.merseyside.adapters.utils.AdapterWorkManager

abstract class AdapterFilter<Parent, Model : VM<Parent>> : HasWorkManager, ILogger {

    private var filterCallback: FilterCallback<Model>? = null

    internal val filters: Filters by lazy { mutableMapOf() }
    internal var notAppliedFilters: Filters = mutableMapOf()

    var isFiltered = false
        private set(value) {
            if (value != field) {
                field = value
                workManager.doAsync { filterCallback?.onFilterStateChanged(value) }
            }
        }

    val itemsCount: Int
        get() = provideFilteredList().size

    private val isBind: Boolean
        get() = this::workManager.isInitialized

    override lateinit var workManager: AdapterWorkManager

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
    open suspend fun addFilter(key: String, filter: Any) {
        val appliedFilter = filters[key]
        if (appliedFilter != filter) {
            "add filter".log()
            notAppliedFilters[key] = filter
        }
    }

    open suspend fun removeFilter(key: String) {
        val appliedFilter = filters[key]
        if (isFiltered) {
            if (appliedFilter != null) {
                filters.remove(key)
                notAppliedFilters.remove(key)
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

    open suspend fun applyFilters(): Boolean = withContext(Dispatchers.Main) {

        isFiltered = if (isFiltered && areFiltersEmpty()) {
            cancelFiltering()
            false
        } else if (notAppliedFilters.isEmpty()) {
            log("No new filters added. Filtering skipped!")
            return@withContext false
        } else {
            val filteredModels = filterModels()
            putAppliedFilters()
            postResult(filteredModels)
            true
        }

        true
    }

    private suspend fun filterModels(): List<Model> = runWithDefault {
        val canFilterCurrentItems = filters.isNotEmpty() &&
                !notAppliedFilters.keys.any { filters.containsKey(it) }

        if (canFilterCurrentItems) {
            filter(provideFilteredList(), notAppliedFilters)
        } else {
            makeAllFiltersNotApplied()
            filter(provideFullList(), notAppliedFilters)
        }
    }

    internal open suspend fun cancelFiltering() {}

    /**
     * @return true if filters applied, false otherwise.
     */
    open fun applyFiltersAsync(onComplete: (Boolean) -> Unit = {}) {
        workManager.doAsync(
            onComplete,
            onError = {
                putAppliedFilters()
                isFiltered = true
                onComplete(false)
            }
        ) { applyFilters() }

    }

    internal fun areFiltersEmpty(): Boolean {
        return filters.isEmpty() && notAppliedFilters.isEmpty()
    }

    private suspend fun postResult(models: List<Model>) {
        filterCallback?.onFiltered(models)
    }

    abstract fun filter(model: Model, key: String, filter: Any): Boolean

    internal suspend fun filter(model: Model): Boolean {
        return filter(model, filters)
    }

    internal suspend fun filter(models: List<Model>): List<Model> {
        return filter(models, filters)
    }

    internal open suspend fun filter(model: Model, filters: Filters): Boolean {
        return filters.all { (key, value) ->
            if (model.isFilterable) {
                filter(model, key, value)
            } else true
        }
    }

    private suspend fun filter(models: List<Model>, filters: Filters): List<Model> {
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
            workManager.doAsync { callback.onFilterStateChanged(true) }
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
        suspend fun onFiltered(models: List<Model>)

        suspend fun onFilterStateChanged(isFiltered: Boolean)
    }

    override val tag = "AdapterFilter"
}