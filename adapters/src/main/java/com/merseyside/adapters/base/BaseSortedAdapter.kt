package com.merseyside.adapters.base

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.model.BaseAdapterViewModel
import com.merseyside.adapters.model.BaseComparableAdapterViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.utils.Logger
import com.merseyside.utils.ext.isNotNullAndEmpty
import com.merseyside.utils.isMainThread
import com.merseyside.utils.mainThreadIfNeeds
import com.merseyside.utils.runThread
import java.lang.reflect.ParameterizedType
import kotlin.collections.set
import kotlin.jvm.Throws

@Suppress("UNCHECKED_CAST")
abstract class BaseSortedAdapter<M: Any, T: BaseComparableAdapterViewModel<M>> : BaseAdapter<M, T>() {

    /**
     * Any children of this class have to pass M and T types. Otherwise, this cast throws CastException
     */
    private val persistentClass: Class<T> = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<T>

    override val modelList: MutableList<T> = ArrayList()
    private val sortedList: SortedList<T>
    private var filteredList: MutableList<T> = ArrayList()

    private var addThread: Thread? = null
    private var updateThread: Thread? = null
    private var filterThread: Thread? = null

    private val comparator : Comparator<T> = Comparator{ o1, o2 -> o1.compareTo(o2.getItem()) }

    private val lock = Any()

    private var isFiltered = false

    private val filtersMap by lazy { HashMap<String, Any>() }
    private val notAppliedFiltersMap by lazy { HashMap<String, Any>() }
    private var filterPattern: String = ""

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
        sortedList.forEachIndexed {index, item ->
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
        addThread = runThread {
            synchronized(lock) {
                add(list)

                func.invoke()
                addThread = null
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
                if (isMainThread() || (updateThread != null && !updateThread!!.isInterrupted)) {
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
            (0 until modelList.size).forEach { index ->
                val model = modelList[index]

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
        updateThread = runThread {
            synchronized(lock) {
                update(updateRequest)

                onUpdated.invoke()

                interruptThread(updateThread)
                updateThread = null
            }
        }
    }

    private fun replaceAll(models: List<T>) {
        synchronized(lock) {
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
    }

    fun addFilter(key : String, obj : Any) {
        notAppliedFiltersMap[key] = obj

        if (filtersMap.containsKey(key)) {
            filtersMap.remove(key)
            notAppliedFiltersMap.putAll(filtersMap)
            filtersMap.clear()

            isFiltered = false
        }
    }

    fun removeFilter(key : String) {

        if (filtersMap.containsKey(key)) {
            filtersMap.remove(key)

            notAppliedFiltersMap.putAll(filtersMap)
            filtersMap.clear()

            filteredList.clear()
            isFiltered = false
        }

        notAppliedFiltersMap.remove(key)
    }

    private fun applyFilters(models: List<T>): List<T>? {
        try {
            if (notAppliedFiltersMap.isNotEmpty()) {

                val filteredList = run filterLabel@{
                    return@filterLabel models.filter{
                        if (filterThread != null && !filterThread!!.isInterrupted) {
                            filter(it, notAppliedFiltersMap)
                        } else {
                            return@filterLabel
                        }
                    }.toMutableList()
                }

                if (filterThread != null && !filterThread!!.isInterrupted) {
                    isFiltered = true

                    filtersMap.putAll(notAppliedFiltersMap)
                    notAppliedFiltersMap.clear()

                    return filteredList as List<T>
                }
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
            this.filteredList.addAll(filteredList)

            if (this.sortedList.isNotEquals(this.filteredList)) addList(filteredList)
        }
    }

    fun applyFilters() {
        val models: MutableList<T> = if (filteredList.isNotEmpty()) filteredList else modelList

        val filteredList = applyFilters(models)

        if (filteredList.isNotNullAndEmpty()) {
            this.filteredList = filteredList.toMutableList()

            if (this.sortedList.isNotEquals(this.filteredList)) setList(filteredList)
        }
    }

    fun applyFiltersAsync() {
        filterThread = runThread {
            synchronized(lock) {
                applyFilters()

                interruptThread(filterThread)
                filterThread = null
            }
        }
    }

    private fun swapFilters() {
        notAppliedFiltersMap.putAll(filtersMap)
        filtersMap.clear()
    }

    @CallSuper
    fun setFilter(models: List<T>): List<T>? {
        filterThread?.let {
            if (it.isInterrupted) return null
        }

        return models.filter {
            filter(it, filterPattern)
        }.also { isFiltered = true }
    }

    /**
     * Don't forget to override areItemsTheSame method with real value
     */
    override fun setFilter(query: String) {
        synchronized(lock) {
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
    }

    override fun setFilterAsync(query: String, func: () -> Unit) {
        if (filterThread == null) {
            filterThread = runThread {
                try {
                    setFilter(query)
                } catch (ignored: ConcurrentModificationException) {}

                interruptThread(filterThread)

                func.invoke()
            }
        } else {
            interruptThread(filterThread)
            filterThread = null

            setFilterAsync(query, func)
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
        filteredList.clear()
        notAppliedFiltersMap.clear()

        setList(modelList)
    }

    override fun filter(obj: T, query: String): Boolean {
        throw NotImplementedError()
    }

    override fun filter(obj: T, filterMap : Map<String, Any>): Boolean {
        throw NotImplementedError()
    }

    override fun getItemCount() = sortedList.size()

    fun getAllItemCount() = modelList.size

    override fun clear() {
        interruptThread(addThread)
        addThread = null

        interruptThread(updateThread)
        updateThread = null

        interruptThread(filterThread)
        filterThread = null

        synchronized(lock) {
            sortedList.beginBatchedUpdates()
            sortedList.clear()
            sortedList.endBatchedUpdates()
            modelList.clear()

            clearFilters()
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

    private fun removeList(list: List<T>, isNotifyPositionChanged: Boolean = true) {
        if (list.isNotEmpty()) {
            val smallestPosition = getSmallestPosition(list)

            modelList.removeAll(list)
            filteredList.removeAll(list)

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
        filteredList.remove(model)

        if (isNotifyPositionChanged) notifyPositionsChanged(position)
    }

    private fun interruptThread(thread: Thread?) {
        if (thread != null && !thread.isInterrupted) {
            thread.interrupt()
        }
    }

    override fun onBindViewHolder(holder: TypedBindingHolder<T>, position: Int, payloads: MutableList<Any>) {
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
