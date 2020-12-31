package com.merseyside.adapters.base

import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.model.BaseAdapterViewModel
import com.merseyside.adapters.model.BaseComparableAdapterViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.utils.Logger
import com.merseyside.utils.concurency.Locker
import com.merseyside.utils.ext.*
import com.merseyside.utils.isMainThread
import com.merseyside.utils.mainThreadIfNeeds
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import java.lang.reflect.ParameterizedType
import kotlin.collections.set
import kotlin.coroutines.CoroutineContext

@Suppress("UNCHECKED_CAST")
abstract class BaseSortedAdapter<M : Any, T : BaseComparableAdapterViewModel<M>>
    : BaseAdapter<M, T>(), CoroutineScope, Locker {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    private val computationContext = Dispatchers.Default

    override val mutex: Mutex = Mutex()

    /**
     * Any children of this class have to pass M and T types. Otherwise, this cast throws CastException
     */
    private val persistentClass: Class<T> =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<T>

    override val modelList: MutableList<T> = ArrayList()
    private val sortedList: SortedList<T>

    private var addJob: Job? = null
    private var updateJob: Job? = null
    private var filterJob: Job? = null

    private val comparator: Comparator<T> = Comparator { o1, o2 -> o1.compareTo(o2.getItem()) }

    private val lock = Any()

    private var isFiltered = false

    private val filtersMap by lazy { HashMap<String, Any>() }
    private val notAppliedFiltersMap by lazy { HashMap<String, Any>() }
    private var filterPattern: String = ""
    private var filterKeyMap: MutableMap<String, List<T>> = HashMap()

    init {
        sortedList = SortedList(persistentClass, object : SortedList.Callback<T>() {
            override fun onInserted(position: Int, count: Int) {
                mainThreadIfNeeds { notifyItemRangeInserted(position, count) }
            }

            override fun onRemoved(position: Int, count: Int) {
                mainThreadIfNeeds { notifyItemRangeRemoved(position, count) }
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                mainThreadIfNeeds { notifyItemMoved(fromPosition, toPosition) }
            }

            override fun compare(o1: T, o2: T): Int {
                return comparator.compare(o1, o2)
            }

            override fun onChanged(position: Int, count: Int) {
                mainThreadIfNeeds { notifyItemRangeChanged(position, count) }
            }

            override fun areContentsTheSame(obj1: T, obj2: T): Boolean {
                return obj1.areContentsTheSame(obj2.getItem())
            }

            override fun areItemsTheSame(obj1: T, obj2: T): Boolean {
                return obj1.areItemsTheSame(obj2.getItem())
            }
        })
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
    override fun getPositionOfObj(obj: M): Int {
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
    override fun notifyItemChanged(obj: M) {
        find(obj)?.let {
            it.notifyUpdate()
            recalculateItemPosition(obj)
        }
    }

    fun recalculateItemPosition(obj: M) {
        recalculatePositionOfItemAt(getPositionOfObj(obj))
    }

    fun recalculatePositionOfItemAt(position: Int) {
        sortedList.recalculatePositionOfItemAt(position)
    }

    override fun add(obj: M) {
        val isEmpty = isEmpty()

        val listItem = initItemViewModel(obj)
        modelList.add(listItem)
        sortedList.add(listItem)

        if (!isEmpty) {
            notifyPositionsChanged(0)
        }
    }

    override fun add(list: List<M>) {
        val isEmpty = isEmpty()
        addModels(itemsToModels(list))

        if (!isEmpty) {
            notifyPositionsChanged(0)
        }
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
        addJob = asynchronously {
            withLock {
                add(list)
                func.invoke()
            }
        }
    }

    open fun update(updateRequest: UpdateRequest<M>) {
        var isRemoved = false

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

            isRemoved = removeList.isNotEmpty()
            removeList(removeList, !updateRequest.isAddNew)
        }

        if (updateRequest.isAddNew) {
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
            } else if (isRemoved) {
                notifyPositionsChanged(0)
            }
        }
    }

    internal open fun update(obj: M): Boolean {
        return run found@{
            modelList.forEachIndexed { index, model ->
                if (model.areItemsTheSame(obj)) {
                    if (!model.areContentsTheSame(obj)) {
                        mainThreadIfNeeds {
                            model.setItem(obj)
                            recalculatePositionOfItemAt(index)
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
        updateJob = asynchronously {
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
                        filterKeyMap[entry.key]!!.toMutableList().apply { addAll(filteredByKeyList) }
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
        val listToSet = if (notAppliedFiltersMap.isNullOrEmpty() && filtersMap.isNullOrEmpty()) modelList
        else applyFilters(modelList)

        if (listToSet != null &&
            this.sortedList.isNotEquals(listToSet)) setList(listToSet)
    }

    fun applyFiltersAsync() {
        filterJob = asynchronously {
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

        filterJob = asynchronously {
            withLock {
                setFilter(query)
                func.invoke()
            }
        }
    }

    private fun setList(list: List<T>) {
        sortedList.beginBatchedUpdates()
        sortedList.clear()
        sortedList.addAll(list)
        sortedList.endBatchedUpdates()
    }

    private fun addList(list: List<T>) {
        sortedList.beginBatchedUpdates()
        sortedList.addAll(list)
        sortedList.endBatchedUpdates()
    }

    fun clearFilters() {
        isFiltered = false

        filtersMap.clear()
        notAppliedFiltersMap.clear()
        filterKeyMap.clear()

        setList(modelList)
    }

    override fun filter(obj: T, query: String): Boolean {
        throw NotImplementedError()
    }

    override fun filter(obj: T, key: String, filterObj: Any): Boolean {
        throw NotImplementedError()
    }

    override fun getItemCount() = sortedList.size()

    fun getAllItemCount() = modelList.size

    override fun clear() {
        cancel()

        asynchronously {
            withLock {
                sortedList.beginBatchedUpdates()
                sortedList.clear()
                sortedList.endBatchedUpdates()
                modelList.clear()

                clearFilters()
            }
        }
    }

    override fun notifyPositionsChanged(startWithPosition: Int) {
        if (startWithPosition < itemCount - 1) {
            (startWithPosition until itemCount).forEach { index ->
                sortedList[index].onPositionChanged(index)
            }
        }
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
    private fun removeList(list: List<T>, isNotifyPositionChanged: Boolean = true) {
        if (list.isNotEmpty()) {
            val smallestPosition = getSmallestPosition(list)

            modelList.removeAll(list)
            filterKeyMap.clear()
            filterKeyMap.forEach { entry ->
                filterKeyMap[entry.key] = entry.value.toMutableList().apply { removeAll(list) }
            }

            sortedList.beginBatchedUpdates()
            sortedList.removeAll(list)
            sortedList.endBatchedUpdates()

            if (isNotifyPositionChanged) notifyPositionsChanged(smallestPosition)
        }
    }

    private fun remove(model: T, isNotifyPositionChanged: Boolean = true) {
        val position = getPositionOfModel(model)

        sortedList.remove(model)
        modelList.remove(model)
        filterKeyMap.forEach { entry ->
            filterKeyMap[entry.key] = entry.value.toMutableList().apply { remove(model) }
        }
        filterKeyMap.clear()

        if (isNotifyPositionChanged) notifyPositionsChanged(position)
    }

    override fun onBindViewHolder(
        holder: TypedBindingHolder<T>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            sortedList[position].setItem(payloads[0] as M)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
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

    override fun getPosition(model: BaseAdapterViewModel<M>): Int {
        return getPositionOfModel(model as T)
    }
}
