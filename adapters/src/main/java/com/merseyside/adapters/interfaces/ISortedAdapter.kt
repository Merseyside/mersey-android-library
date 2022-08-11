@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.ext.*
import com.merseyside.adapters.interfaces.base.AdapterListActions
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.extensions.intersect
import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.utils.isMainThread

interface ISortedAdapter<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>>
    : IBaseAdapter<Parent, Model>, AdapterListActions<Parent, Model> {

    val sortedList: SortedList<Model>

    override fun notifyModelUpdated(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {
        val position = getPositionOfModel(model)
        adapter.notifyItemChanged(position, payloads)
        sortedList.recalculatePositionOfItemAt(position)
    }

    override fun filter(model: Model, query: String): Boolean {
        throw NotImplementedError("Override this method in your implementation!")
    }

    override fun filter(model: Model, key: String, filterObj: Any): Boolean {
        throw NotImplementedError("Override this method in your implementation!")
    }

    private fun applyFilterToNewModels(models: List<Model>) {
        val filteredList = if (filtersMap.isNotEmpty()) {
            swapFilters()
            applyFilters(models)
        } else {
            setFilter(models)
        }

        if (filteredList != null) {
            if (sortedList.isNotEquals(filteredList)) addList(filteredList)
        }
    }

    /**
     * @return true if filtered list is not empty, false otherwise.
     **/
    fun applyFilters(): Int {
        TODO()
//        val listToSet =
//            if (notAppliedFiltersMap.isEmpty() && filtersMap.isEmpty()) modelList
//            else applyFilters(modelList)
//
//        if (listToSet != null &&
//            this.sortedList.isNotEquals(listToSet)
//        ) replaceAll(listToSet)
//
//        return listToSet?.size ?: 0
    }

    fun applyFiltersAsync(callback: (filteredCount: Int) -> Unit = {}) {
        filterJob = scope.asynchronously {
            val count = withLock {
                applyFilters()
            }

            callback(count)
        }
    }

    private fun swapFilters() {
        notAppliedFiltersMap.putAll(filtersMap)
        filtersMap.clear()
    }

    fun setFilter(models: List<Model>): List<Model>? {
        filterJob?.let {
            if (!it.isActive) return null
        }

        return models.filter {
            filter(it, filterPattern)
        }.also { isFiltered = true }
    }

    /**
     * Don't forget to override areItemsTheSame method with real value
     * @return filtered items count.
     */
    override fun setFilter(query: String): Int {
//        if (filterPattern != query) {
//            filterPattern = query
//
//            if (filterPattern.isNotEmpty()) {
//                val filteredList = setFilter(modelList)
//                if (filteredList != null) {
//                    replaceAll(filteredList)
//                }
//            } else {
//                isFiltered = false
//                replaceAll(modelList)
//            }
//        }

        return getItemsCount()
    }

    override fun setFilterAsync(query: String, callback: (filteredCount: Int) -> Unit) {
        filterJob?.cancel()

        filterJob = scope.asynchronously {
            val count = withLock {
                setFilter(query)
            }

            callback(count)
        }
    }

    fun addFilter(key: String, obj: Any) {
        notAppliedFiltersMap[key] = obj

        if (filtersMap.containsKey(key)) {
            filtersMap.remove(key)

            filterKeyMap.remove(key)

            isFiltered = false
        }
    }

    fun removeFilter(key: String) {
        notAppliedFiltersMap.remove(key)
        if (filtersMap.containsKey(key)) {
            filtersMap.remove(key)

            filterKeyMap.remove(key)

            isFiltered = false
        }
    }

    @InternalAdaptersApi
    fun applyFilters(models: List<Model>): List<Model>? {
        try {
            if (notAppliedFiltersMap.isNotEmpty()) {
                notAppliedFiltersMap.forEach { entry ->
                    val filteredByKeyList = applyFilter(models, entry.key, entry.value)

                    filterKeyMap[entry.key] = filterKeyMap[entry.key]?.let {
                        it.toMutableList().apply { addAll(filteredByKeyList) }
                    } ?: filteredByKeyList
                }

                filtersMap.putAll(notAppliedFiltersMap)
                notAppliedFiltersMap.clear()

                isFiltered = true
            }

            if (isMainThread() || filterJob?.isActive == true) {

                return filterKeyMap
                    .map { entry -> entry.value }
                    .intersect()
                    .toMutableList()
            }

        } catch (e: ConcurrentModificationException) {
            Logger.logErr(e)
        }

        return null
    }

    @Throws(NotImplementedError::class)
    fun applyFilter(models: List<Model>, key: String, filterObj: Any): List<Model> {
        return models.filter { filter(it, key, filterObj) }
    }

    private fun replaceAll(models: List<Model>) {
        sortedList.batchedUpdate {
            val sortedModels = sortedList.getAll().toSet()
            val subtract = models.subtract(sortedModels)

            val subtractSorted = sortedModels.subtract(models.toSet()).toList()

            sortedList.removeAll(subtractSorted)
            sortedList.addAll(subtract)
        }
    }

    fun clearFilters() {
        isFiltered = false

        filtersMap.clear()
        notAppliedFiltersMap.clear()
        filterKeyMap.clear()

        //replaceAll(modelList)
    }

    override fun clear() {
        sortedList.batchedUpdate { clear() }
        clearFilters()
    }

    /* Models list actions */
    override fun addModel(model: Model) {
        sortedList.add(model)
    }

    @InternalAdaptersApi
    override fun addModels(models: List<Model>) {
        if (!isFiltered) {
            addList(models)
        } else {
            applyFilterToNewModels(models)
        }
    }

    private fun addList(list: List<Model>) = sortedList.batchedUpdate { addAll(list) }

    /**
     * Be sure your model's compareTo method handles equal items!
     */
    override fun removeModels(list: List<Model>): Boolean {
        sortedList.batchedUpdate {
            removeAll(list)
        }
        return true
    }

    override fun removeModel(model: Model): Boolean {
        sortedList.remove(model)
        filterKeyMap.forEach { entry ->
            filterKeyMap[entry.key] = entry.value.toMutableList().apply { remove(model) }
        }
        filterKeyMap.clear()

        return true
    }

    override fun removeAll() {
        sortedList.clear()
        adapter.notifyDataSetChanged()
    }
}