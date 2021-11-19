package com.merseyside.adapters.base

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.utils.Logger
import com.merseyside.utils.concurency.Locker
import com.merseyside.utils.ext.*
import com.merseyside.utils.getMinMax
import com.merseyside.utils.isMainThread
import com.merseyside.utils.mainThreadIfNeeds
import com.merseyside.utils.reflection.ReflectionUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlin.collections.set

@Suppress("UNCHECKED_CAST")
abstract class SortedAdapter<M : Any, T : ComparableAdapterViewModel<M>>(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : BaseAdapter<M, T>(), Locker {
    private val computationContext = Dispatchers.Default
    override val mutex: Mutex = Mutex()

    /**
     * Any children of this class have to pass M and T types. Otherwise, this cast throws CastException
     */
    private val persistentClass: Class<T> =
        ReflectionUtils.getGenericParameterClass(this.javaClass, SortedAdapter::class.java, 1)
                as Class<T>

    override val modelList: MutableList<T> = ArrayList()

    private var addJob: Job? = null
    private var updateJob: Job? = null
    private var filterJob: Job? = null

    private val lock = Any()

    var isFiltered = false
        internal set

    private val filtersMap by lazy { HashMap<String, Any>() }
    private val notAppliedFiltersMap by lazy { HashMap<String, Any>() }
    internal var filterPattern: String = ""
    private val filterKeyMap: MutableMap<String, List<T>> by lazy { HashMap() }

    private val listCallback = object : SortedList.Callback<T>() {
        override fun onInserted(position: Int, count: Int) {
            for (i in position until sortedList.size()) {
                sortedList[i].onPositionChanged(i)
            }

            mainThreadIfNeeds { notifyItemRangeInserted(position, count) }
        }

        override fun onRemoved(position: Int, count: Int) {
            for (i in position until sortedList.size()) {
                sortedList[i].onPositionChanged(i)
            }

            mainThreadIfNeeds { notifyItemRangeRemoved(position, count) }
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            val minMax = getMinMax(fromPosition, toPosition)

            for (i in minMax.first..minMax.second) {
                sortedList[i].onPositionChanged(i)
            }

            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int) {
            notifyItemRangeChanged(position, count)
        }

        override fun compare(o1: T, o2: T): Int {
            return o1.compareTo(o2.getItem())
        }

        override fun areContentsTheSame(obj1: T, obj2: T): Boolean {
            return obj1.areContentsTheSame(obj2.getItem())
        }

        override fun areItemsTheSame(obj1: T, obj2: T): Boolean {
            return obj1.areItemsTheSame(obj2.getItem())
        }
    }

    internal val sortedList: SortedList<T> = SortedList(persistentClass, listCallback)

    override fun onBindViewHolder(
        holder: TypedBindingHolder<T>,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val payloadable = payloads[0] as List<ComparableAdapterViewModel.Payloadable>

            if (isPayloadsValid(payloadable)) {
                onPayloadable(holder, payloadable)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun getModelByPosition(position: Int): T {
        return try {
            sortedList[position]
        } catch (e: IndexOutOfBoundsException) {
            modelList[position]
        }
    }

    override fun getItemByPosition(position: Int): M {
        return getModelByPosition(position).getItem()
    }

    override fun getPositionOfItem(obj: M): Int {
        return sortedList.indexOf { item -> item.areItemsTheSame(obj) }
    }

    @Throws(IllegalArgumentException::class)
    override fun getPositionOfModel(model: T): Int {
        return getPositionOfItem(model.getItem()).let { position ->
            if (position != SortedList.INVALID_POSITION) position
            else throw IllegalArgumentException("No data found")
        }
    }

    override fun find(obj: M): T? {
        return sortedList.find {
            it.areItemsTheSame(obj)
        }
    }

    @Throws(IllegalArgumentException::class)
    fun notifyItemChanged(
        model: T,
        payloads: List<ComparableAdapterViewModel.Payloadable> = emptyList()
    ) = try {
        val position = getPositionOfModel(model)

        notifyItemChanged(position, payloads)
        sortedList.recalculatePositionOfItemAt(position)
    } catch (e: IllegalArgumentException) {
        Logger.log("Skip notify item change!")
    }

    override fun add(obj: M) {
        val listItem = initItemViewModel(obj)
        modelList.add(listItem)
        sortedList.add(listItem)
    }

    override fun add(list: List<M>) {
        addModels(itemsToModels(list))
    }

    override fun add(model: T) {
        super.add(model)

        if (!isFiltered) {
            sortedList.add(model)
        }
    }

    override fun addModels(list: List<T>) {
        super.addModels(list)

        if (!isFiltered) {
            addList(list)
        } else {
            applyFilterToNewModels(list)
        }
    }

    fun addAsync(list: List<M>, func: () -> Unit = {}) {
        addJob = scope.asynchronously {
            withLock {
                add(list)
                func.invoke()
            }
        }
    }

    override fun update(updateRequest: UpdateRequest<M>): Boolean {
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

        val addList = ArrayList<M>()
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

    override fun update(obj: M): Boolean {
        return run found@{
            modelList.forEach { model ->
                if (model.areItemsTheSame(obj)) {
                    if (!model.areContentsTheSame(obj)) {
                        mainThreadIfNeeds {
                            notifyItemChanged(model, model.payload(obj))
                        }
                    }
                    return@found true
                }
            }
            false
        }
    }

    fun updateAsync(
        updateRequest: UpdateRequest<M>,
        onUpdated: () -> Unit = {}
    ) {
        updateJob = scope.asynchronously {
            withLock {
                update(updateRequest)
                onUpdated.invoke()
            }
        }
    }

    private fun replaceAll(models: List<T>) {
        sortedList.batchedUpdate {
            val sortedModels = sortedList.getAll().toSet()
            val subtract = models.subtract(sortedModels)

            val subtractSorted = sortedModels.subtract(models.toSet()).toList()

            sortedList.removeAll(subtractSorted)
            sortedList.addAll(subtract)
        }
    }

    open fun addFilter(key: String, obj: Any) {
        notAppliedFiltersMap[key] = obj

        if (filtersMap.containsKey(key)) {
            filtersMap.remove(key)

            filterKeyMap.remove(key)

            isFiltered = false
        }
    }

    open fun removeFilter(key: String) {
        notAppliedFiltersMap.remove(key)
        if (filtersMap.containsKey(key)) {
            filtersMap.remove(key)

            filterKeyMap.remove(key)

            isFiltered = false
        }
    }

    internal open fun applyFilters(models: List<T>): List<T>? {
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
    internal open fun applyFilter(models: List<T>, key: String, filterObj: Any): List<T> {
        return models.filter { filter(it, key, filterObj) }
    }

    private fun applyFilterToNewModels(models: List<T>) {
        val filteredList = if (filtersMap.isNotEmpty()) {
            swapFilters()
            applyFilters(models)
        } else {
            setFilter(models)
        }

        if (filteredList != null) {
            if (this.sortedList.isNotEquals(filteredList)) addList(filteredList)
        }
    }

    /**
     * @return true if filtered list is not empty, false otherwise.
     **/
    open fun applyFilters(): Int {
        val listToSet =
            if (notAppliedFiltersMap.isNullOrEmpty() && filtersMap.isNullOrEmpty()) modelList
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

    internal open fun setFilter(models: List<T>): List<T>? {
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

    private fun setList(list: List<T>) {
        sortedList.batchedUpdate {
            clear()
            addAll(list)
        }
    }

    private fun addList(list: List<T>) {
        sortedList.batchedUpdate {
            addAll(list)
        }
    }

    open fun clearFilters() {
        isFiltered = false

        filtersMap.clear()
        notAppliedFiltersMap.clear()
        filterKeyMap.clear()

        replaceAll(modelList)
    }

    override fun filter(obj: T, query: String): Boolean {
        throw NotImplementedError("Override this method in your implementation!")
    }

    override fun filter(obj: T, key: String, filterObj: Any): Boolean {
        throw NotImplementedError("Override this method in your implementation!")
    }

    override fun getItemCount() = sortedList.size()

    fun getAllItemCount() = modelList.size

    override fun clear() {
        sortedList.batchedUpdate {
            clear()
        }

        modelList.clear()
        clearFilters()
    }

    override fun remove(obj: M) {
        val model = getModelByObj(obj)

        if (model != null) {
            remove(model)
        }
    }

    override fun remove(list: List<M>) {
        val modelList = list.mapNotNull { getModelByObj(it) }
        removeModels(modelList)
    }

    /**
     * Be sure your model's compareTo method handles equal items!
     */
    internal open fun removeModels(list: List<T>): Boolean {
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

    private fun remove(model: T) {
        sortedList.remove(model)
        modelList.remove(model)
        filterKeyMap.forEach { entry ->
            filterKeyMap[entry.key] = entry.value.toMutableList().apply { remove(model) }
        }
        filterKeyMap.clear()
    }

    private fun isPayloadsValid(payloads: List<ComparableAdapterViewModel.Payloadable>): Boolean {
        return payloads.isNotEmpty() &&
                !payloads.contains(ComparableAdapterViewModel.Payloadable.None)
    }

    open fun onPayloadable(
        holder: TypedBindingHolder<T>,
        payloads: List<ComparableAdapterViewModel.Payloadable>
    ) {}
}
