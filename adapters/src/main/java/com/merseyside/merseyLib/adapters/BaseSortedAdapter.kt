package com.merseyside.merseyLib.adapters

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.SortedList
import com.merseyside.merseyLib.presentation.model.BaseComparableAdapterViewModel
import com.merseyside.merseyLib.presentation.view.BaseViewHolder
import com.merseyside.merseyLib.utils.Logger
import com.merseyside.merseyLib.utils.ext.isNotNullAndEmpty
import com.merseyside.merseyLib.utils.isMainThread
import com.merseyside.merseyLib.utils.mainThread
import java.lang.reflect.ParameterizedType
import kotlin.collections.set


@Suppress("UNCHECKED_CAST")
abstract class BaseSortedAdapter<M: Any, T: BaseComparableAdapterViewModel<M>> : BaseAdapter<M, T>() {

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
                /* This bug google don't wanna fix for a long long time!*/
                runOnRightThread { notifyItemRangeInserted(position, count) }
            }

            override fun onRemoved(position: Int, count: Int) {
                runOnRightThread { notifyItemRangeRemoved(position, count) }
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                runOnRightThread { notifyItemMoved(fromPosition, toPosition) }
            }

            override fun compare(o1: T, o2: T): Int {
                return comparator.compare(o1, o2)
            }

            override fun onChanged(position: Int, count: Int) {
                runOnRightThread { notifyItemRangeChanged(position, count) }
            }

            override fun areContentsTheSame(obj1: T, obj2: T): Boolean {
                return obj1.areContentsTheSame(obj2.getItem())
            }

            override fun areItemsTheSame(obj1: T, obj2: T): Boolean {
                return obj1.areItemsTheSame(obj2.getItem())
            }
        })
    }

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

    @Throws(IllegalArgumentException::class)
    override fun getPositionOfObj(obj: M): Int {

        for (i in 0 until sortedList.size()) {
            if (sortedList.get(i).areItemsTheSame(obj)) return i
        }

        throw IllegalArgumentException("No data found")
    }

    override fun find(obj: M): T? {

        for (i in 0 until sortedList.size()) {
            sortedList.get(i).let {
                if (it.areItemsTheSame(obj)) {
                return it
                }
            }
        }

        return null
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
        val listItem = createItemViewModel(obj)
        modelList.add(listItem)
        sortedList.add(listItem)
    }

    override fun add(list: List<M>) {
        val newModelList = ArrayList<T>()

        for (obj in list) {
            newModelList.add(createItemViewModel(obj))
        }

        modelList.addAll(newModelList)

        if (!isFiltered) {
            runOnRightThread {
                addList(newModelList)
            }
        } else {
            applyFilterToNewModels(newModelList)

        }
    }

    private fun runOnRightThread(func: () -> Unit) {
        if (!isMainThread()) {
            mainThread {
                func()
            }
        } else {
            func()
        }
    }

    fun addAsync(list: List<M>, func: () -> Unit = {}) {
        addThread = Thread {
            synchronized(lock) {
                add(list)

                func.invoke()
                addThread = null
            }
        }.apply { start() }
    }

    open fun update(updateRequest: UpdateRequest<M>) {
        if (!isFiltered) {

            if (updateRequest.isDeleteOld) {
                val removeList = (0 until sortedList.size()).map {
                    sortedList.get(it)
                }.filter {
                    var isFound = false

                    for (obj in updateRequest.list) {
                        if (it.areItemsTheSame(obj)) {
                            isFound = true
                            break
                        }
                    }

                    !isFound
                }

                for (removeItem in removeList) {
                    remove(removeItem)
                }
            }
        }


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

        if (updateRequest.isAddNew) add(addList)
    }

    private fun update(obj: M): Boolean {
        var isFound = false
        for (i in 0 until sortedList.size()) {

            val model = sortedList.get(i)
            if (model.areItemsTheSame(obj)) {
                if (!model.areContentsTheSame(obj)) {
                    runOnRightThread {
                        model.setItem(obj)
                        recalculatePositionOfItemAt(i)
                    }
                }
                isFound = true
                break
            }
        }
        return isFound
    }

    fun updateAsync(
        updateRequest: UpdateRequest<M>,
        func: () -> Unit = {}
    ) {
        updateThread = Thread {
            synchronized(lock) {
                update(updateRequest)

                func.invoke()

                interruptThread(updateThread)
                updateThread = null
            }
        }.apply { start() }
    }

    private fun replaceAll(models: List<T>) {
        synchronized(lock) {
            sortedList.beginBatchedUpdates()
            for (i in sortedList.size() - 1 downTo 0) {
                val model = sortedList.get(i)
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

                    return filteredList
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
            this.filteredList = filteredList!!.toMutableList()

            if (this.sortedList.isNotEquals(this.filteredList)) setList(filteredList)
        }
    }

    fun applyFiltersAsync() {
        filterThread = Thread {
            synchronized(lock) {
                applyFilters()

                filterThread = null
            }
        }.also { it.start() }
    }

    private fun swapFilters() {
        notAppliedFiltersMap.putAll(filtersMap)
        filtersMap.clear()
    }

    @CallSuper
    fun setFilter(models: List<T>): List<T>? {

        isFiltered = true
        val filteredList = ArrayList<T>()
        for (obj in models) {
            if (filterThread != null && filterThread!!.isInterrupted) {
                return null
            } else {
                if (filter(obj, filterPattern)) filteredList.add(obj)
            }
        }

        return filteredList
    }

    override fun setFilter(query: String) {
        synchronized(lock) {
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
        if (filterThread == null) {
            filterThread = Thread {
                try {
                    setFilter(query)
                } catch (ignored: ConcurrentModificationException) {}

                filterThread = null

                func.invoke()
            }.apply { start() }
        } else {
            filterThread!!.interrupt()
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
        return true
    }

    override fun filter(obj: T, filterMap : Map<String, Any>): Boolean {
        return true
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

    override fun remove(obj: M) {
        val foundObj = (0 until sortedList.size())
            .asSequence()
            .map { sortedList.get(it) }
            .firstOrNull { it.areItemsTheSame(obj) }

        if (foundObj != null) {
            remove(foundObj)
        }
    }

    override fun remove(list: List<M>) {
        list.forEach { remove(it) }
    }

    private fun remove(obj: T) {
        sortedList.remove(obj)
        modelList.remove(obj)
        filteredList.remove(obj)
    }

    private fun interruptThread(thread: Thread?) {

        if (thread != null && !thread.isInterrupted) {
            thread.interrupt()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            sortedList.get(position).setItem(payloads[0] as M)
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

    companion object {
        private const val TAG = "BaseSortedAdapter"
    }
}
