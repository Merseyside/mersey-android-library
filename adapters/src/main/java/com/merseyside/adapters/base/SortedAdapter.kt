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

    private var isFiltered = false

    private val filtersMap by lazy { HashMap<String, Any>() }
    private val notAppliedFiltersMap by lazy { HashMap<String, Any>() }
    private var filterPattern: String = ""
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

        override fun compare(o1: T, o2: T): Int {
            return o1.compareTo(o2.getItem())
        }

        override fun onChanged(position: Int, count: Int) {
            notifyItemRangeChanged(position, count)
        }

        override fun areContentsTheSame(obj1: T, obj2: T): Boolean {
            return obj1.areContentsTheSame(obj2.getItem())
        }

        override fun areItemsTheSame(obj1: T, obj2: T): Boolean {
            return obj1.areItemsTheSame(obj2.getItem())
        }
    }

    private val sortedList: SortedList<T> = SortedList(persistentClass, listCallback)

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

    @Throws(NoSuchElementException::class)
    override fun getPositionOfItem(obj: M): Int {
        try {
            return sortedList.indexOf { item -> item.areItemsTheSame(obj) }
        } catch (e: NoSuchElementException) {
            throw e
        }
    }

    @Throws(IllegalArgumentException::class)
    override fun getPositionOfModel(model: T): Int {
        sortedList.forEachIndexed { index, item ->
            if (item == model) return index
        }

        throw IllegalArgumentException("No data found")
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
    ) {
        val position = getPositionOfModel(model)

        notifyItemChanged(position, payloads)
        sortedList.recalculatePositionOfItemAt(position)
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

    override fun update(updateRequest: UpdateRequest<M>) {
        if (updateRequest.isDeleteOld) {
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

            removeList(removeList)
        }

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
        sortedList.beginBatchedUpdates()

        for (i in sortedList.size() - 1 downTo 0) {
            val model = sortedList[i]
            if (!models.contains(model)) {
                sortedList.remove(model)
            }
        }

        sortedList.addAll(models)
        sortedList.endBatchedUpdates()
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

    private fun applyFilters(models: List<T>): List<T>? {
        try {
            if (notAppliedFiltersMap.isNotEmpty()) {
                notAppliedFiltersMap.forEach { entry ->
                    val filteredByKeyList = models.mapNotNull {
                        if (filter(it, entry.key, entry.value)) it
                        else null
                    }

                    filterKeyMap[entry.key] = if (filterKeyMap.contains(entry.key)) {
                        filterKeyMap[entry.key]!!.toMutableList()
                            .apply { addAll(filteredByKeyList) }
                    } else {
                        filteredByKeyList
                    }
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

    fun applyFilters() {
        val listToSet =
            if (notAppliedFiltersMap.isNullOrEmpty() && filtersMap.isNullOrEmpty()) modelList
            else applyFilters(modelList)

        if (listToSet != null &&
            this.sortedList.isNotEquals(listToSet)
        ) setList(listToSet)
    }

    fun applyFiltersAsync() {
        filterJob = scope.asynchronously {
            withLock {
                applyFilters()
            }
        }
    }

    private fun swapFilters() {
        notAppliedFiltersMap.putAll(filtersMap)
        filtersMap.clear()
    }

    private fun setFilter(models: List<T>): List<T>? {
        filterJob?.let {
            if (!it.isActive) return null
        }

        return models.filter {
            filter(it, filterPattern)
        }.also { isFiltered = true }
    }

    /**
     * Don't forget to override areItemsTheSame method with real value
     */
    override fun setFilter(query: String) {
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
    }

    override fun setFilterAsync(query: String, func: () -> Unit) {
        filterJob?.cancel()

        filterJob = scope.asynchronously {
            withLock {
                setFilter(query)
                func.invoke()
            }
        }
    }

    private fun setList(list: List<T>) {
        sortedList.apply {
            beginBatchedUpdates()
            clear()
            addAll(list)
            endBatchedUpdates()
        }
    }

    private fun addList(list: List<T>) {
        sortedList.apply {
            beginBatchedUpdates()
            addAll(list)
            endBatchedUpdates()
        }
    }

    fun clearFilters() {
        isFiltered = false

        filtersMap.clear()
        notAppliedFiltersMap.clear()
        filterKeyMap.clear()

        setList(modelList)
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
        sortedList.apply {
            beginBatchedUpdates()
            clear()
            endBatchedUpdates()
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
        removeList(modelList)
    }

    /**
     * Be sure your model's compareTo method handles equal items!
     */
    private fun removeList(list: List<T>) {
        if (list.isNotEmpty()) {
            modelList.removeAll(list)
            filterKeyMap.clear()
            filterKeyMap.forEach { entry ->
                filterKeyMap[entry.key] = entry.value.toMutableList().apply { removeAll(list) }
            }

            sortedList.apply {
                beginBatchedUpdates()
                removeAll(list)
                endBatchedUpdates()
            }
        }
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
    ) {
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun first(): M {
        try {
            return getModelByPosition(0).getItem()
        } catch (e: Exception) {
            throw IndexOutOfBoundsException("List is empty")
        }
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun last(): M {
        try {
            return getModelByPosition(itemCount - 1).getItem()
        } catch (e: Exception) {
            throw IndexOutOfBoundsException("List is empty")
        }
    }
}
