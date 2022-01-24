@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.utils

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.base.UpdateRequest
import com.merseyside.adapters.ext.*
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.merseyLib.kotlin.concurency.Locker
import com.merseyside.merseyLib.kotlin.extensions.intersect
import com.merseyside.utils.isMainThread
import com.merseyside.utils.mainThreadIfNeeds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex

interface SortedAdapterListUtils<Item : Parent,
        Parent, Model : ComparableAdapterParentViewModel<Item, Parent>>
    : AdapterListUtils<Item, Parent, Model>, Locker {

    val sortedList: SortedList<Model>

    val scope: CoroutineScope
    override val mutex: Mutex

    var addJob: Job?
    var updateJob: Job?
    var filterJob: Job?

    val lock: Any

    var isFiltered: Boolean

    val filtersMap: HashMap<String, Any>
    val notAppliedFiltersMap: HashMap<String, Any>
    var filterPattern: String
    val filterKeyMap: MutableMap<String, List<Model>>

    override fun add(model: Model) {
        super.add(model)

        if (!isFiltered) {
            sortedList.add(model)
        }
    }

    override fun add(item: Item) {
        addModels(createModels(listOf(item)))
    }

    override fun add(items: List<Item>) {
        addModels(createModels(items))
    }

    fun addAsync(list: List<Item>, func: () -> Unit = {}) {
        addJob = scope.asynchronously {
            withLock {
                add(list)
                func.invoke()
            }
        }
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

    override fun update(updateRequest: UpdateRequest<Item>): Boolean {
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

        val addList = ArrayList<Item>()
        for (obj in updateRequest.list) {
            if (isMainThread() || updateJob?.isActive == true) {
                if (!update(obj) && updateRequest.isAddNew) {
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

    override fun update(item: Item): Boolean {
        return run found@{
            modelList.forEach { model ->
                if (model.areItemsTheSame(item)) {
                    if (!model.areContentsTheSame(item)) {
                        mainThreadIfNeeds {
                            notifyItemChanged(model, model.payload(item))
                        }
                    }
                    return@found true
                }
            }
            false
        }
    }

    fun updateAsync(
        updateRequest: UpdateRequest<Item>,
        onUpdated: () -> Unit = {}
    ) {
        updateJob = scope.asynchronously {
            withLock {
                update(updateRequest)
                onUpdated.invoke()
            }
        }
    }

    override fun remove(items: List<Item>): Boolean {
        val modelList = items.mapNotNull { getModelByItem(it) }
        return removeModels(modelList)
    }

    /**
     * Be sure your model's compareTo method handles equal items!
     */
    fun removeModels(list: List<Model>): Boolean {
        return if (list.isNotEmpty()) {
            modelList.removeAll(list)
            filterKeyMap.clear()
            filterKeyMap.forEach { entry ->
                filterKeyMap[entry.key] = entry.value.toMutableList().apply { removeAll(list) }
            }

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

    override fun getItemByPosition(position: Int): Item {
        return getModelByPosition(position).item
    }

    override fun getPositionOfItem(item: Item): Int {
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
    override fun find(item: Item): Model? {
        return sortedList.find {
            it.areItemsTheSame(item)
        }
    }

    @Throws(IllegalArgumentException::class)
    fun notifyItemChanged(
        model: Model,
        payloads: List<ComparableAdapterParentViewModel.Payloadable> = emptyList()
    ) = try {
        val position = getPositionOfModel(model)

        adapter.notifyItemChanged(position, payloads)
        sortedList.recalculatePositionOfItemAt(position)
    } catch (e: IllegalArgumentException) {
        Logger.log("Skip notify item change!")
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

    fun isPayloadsValid(payloads: List<ComparableAdapterParentViewModel.Payloadable>): Boolean {
        return payloads.isNotEmpty() &&
                !payloads.contains(ComparableAdapterParentViewModel.Payloadable.None)
    }

    fun onPayloadable(
        holder: TypedBindingHolder<Model>,
        payloads: List<ComparableAdapterParentViewModel.Payloadable>
    ) {}
}