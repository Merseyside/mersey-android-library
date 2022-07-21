@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.utils

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.base.UpdateRequest
import com.merseyside.adapters.ext.*
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.merseyLib.kotlin.extensions.intersect
import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.utils.isMainThread

interface SortedAdapterListUtils<Parent, Model : ComparableAdapterParentViewModel<out Parent, Parent>>
    : AdapterListUtils<Parent, Model> {

    val sortedList: SortedList<Model>

    override fun add(model: Model) {
        super.add(model)

        if (!isFiltered) {
            sortedList.add(model)
        }
    }

    override fun add(item: Parent) {
        addModels(createModels(listOf(item)))
    }

    override fun add(items: List<Parent>) {
        addModels(createModels(items))
    }

    override fun add(position: Int, item: Parent) {
        throw Exception("Can be used only with BaseAdapter")
    }

    override fun add(position: Int, items: List<Parent>) {
        throw Exception("Can be used only with BaseAdapter")
    }

    override fun addBefore(beforeItem: Parent, item: Parent) {
        throw Exception("Can be used only with BaseAdapter")
    }

    override fun addBefore(beforeItem: Parent, items: List<Parent>) {
        throw Exception("Can be used only with BaseAdapter")
    }

    override fun addAfter(afterItem: Parent, item: Parent) {
        throw Exception("Can be used only with BaseAdapter")
    }

    override fun addAfter(afterItem: Parent, item: List<Parent>) {
        throw Exception("Can be used only with BaseAdapter")
    }

    @InternalAdaptersApi
    override fun add(index: Int, model: Model) {
        throw Exception("Can be used only with BaseAdapter")
    }

    override fun update(items: List<Parent>): Boolean {
        return update(UpdateRequest.Builder(items)
            .isDeleteOld(true)
            .build()
        )
    }

    fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        val removed = if (updateRequest.isDeleteOld) {
            val removeList = modelList
                .filter { model ->
                    if (model.isDeletable()) {
                        updateRequest.list.find {
                            model.areItemsTheSame(it)
                        } == null
                    } else {
                        false
                    }
                }

            removeModels(removeList)
        } else false

        val addList = ArrayList<Parent>()
        for (obj in updateRequest.list) {
            if (isMainThread() || updateJob?.isActive == true) {
                if (!updateAndNotify(obj) && updateRequest.isAddNew) {
                    addList.add(obj)
                }
            } else {
                break
            }
        }

        if (addList.isNotEmpty()) {
            add(addList)
        }

        return addList.isNotEmpty() || removed
    }

    fun updateAsync(
        updateRequest: UpdateRequest<Parent>,
        onUpdated: () -> Unit = {}
    ) {
        updateJob = scope.asynchronously {
            withLock {
                update(updateRequest)
                onUpdated.invoke()
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    @InternalAdaptersApi
    override fun notifyModelChanged(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable>
    ): Int {
        val position = super.notifyModelChanged(model, payloads)
        sortedList.recalculatePositionOfItemAt(position)
        return position
    }

    @InternalAdaptersApi
    override fun addModels(list: List<Model>) {
        super.addModels(list)

        if (!isFiltered) {
            addList(list)
        } else {
            applyFilterToNewModels(list)
        }
    }

    fun getAllItemCount(): Int {
        return modelList.size
    }

    override fun remove(items: List<Parent>): Boolean {
        val modelList = items.mapNotNull { getModelByItem(it) }
        return removeModels(modelList)
    }

    /**
     * Be sure your model's compareTo method handles equal items!
     */
    override fun removeModels(list: List<Model>): Boolean {
        return if (super.removeModels(list)) {

            sortedList.batchedUpdate {
                removeAll(list)
            }
            true
        } else false
    }

    override fun remove(model: Model): Boolean {
        sortedList.remove(model)
        modelList.remove(model)
        filterKeyMap.forEach { entry ->
            filterKeyMap[entry.key] = entry.value.toMutableList().apply { remove(model) }
        }
        filterKeyMap.clear()

        return true
    }

    private fun addList(list: List<Model>) = sortedList.batchedUpdate { addAll(list) }

    @Throws(IndexOutOfBoundsException::class)
    override fun getModelByPosition(position: Int): Model {
        return try {
            sortedList[position]
        } catch (e: IndexOutOfBoundsException) {
            modelList[position]
        }
    }

    override fun getItemByPosition(position: Int): Parent {
        return getModelByPosition(position).item
    }

    override fun getPositionOfItem(item: Parent): Int {
        return sortedList.indexOf { it.areItemsTheSame(item) }
    }

    @Throws(IllegalArgumentException::class)
    override fun getPositionOfModel(model: Model): Int {
        return getPositionOfItem(model.item).let { position ->
            if (position != SortedList.INVALID_POSITION) position
            else throw IllegalArgumentException("No data found")
        }
    }

    @InternalAdaptersApi
    override fun find(item: Parent): Model? {
        return sortedList.find {
            it.areItemsTheSame(item)
        }
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
        val listToSet =
            if (notAppliedFiltersMap.isEmpty() && filtersMap.isEmpty()) modelList
            else applyFilters(modelList)

        if (listToSet != null &&
            this.sortedList.isNotEquals(listToSet)
        ) replaceAll(listToSet)

        return listToSet?.size ?: 0
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
        if (filterPattern != query) {
            filterPattern = query

            if (filterPattern.isNotEmpty()) {
                val filteredList = setFilter(modelList)
                if (filteredList != null) {
                    replaceAll(filteredList)
                }
            } else {
                isFiltered = false
                replaceAll(modelList)
            }
        }

        return sortedList.size()
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

        replaceAll(modelList)
    }

    override fun clear() {
        sortedList.batchedUpdate { clear() }

        modelList.clear()
        clearFilters()
    }
}