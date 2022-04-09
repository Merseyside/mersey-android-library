@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.ext.asynchronously
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.merseyLib.kotlin.concurency.Locker
import com.merseyside.merseyLib.kotlin.extensions.isZero
import com.merseyside.merseyLib.kotlin.extensions.minByNullable
import com.merseyside.utils.mainThreadIfNeeds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex

@SuppressLint("NotifyDataSetChanged")
interface AdapterListUtils<Parent, Model : AdapterParentViewModel<out Parent, Parent>> :
    HasOnItemClickListener<Parent>, Locker {

    @InternalAdaptersApi
    val modelList: MutableList<Model>
    val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>

    var addJob: Job?
    var updateJob: Job?

    val scope: CoroutineScope
    override val mutex: Mutex

    var filterJob: Job?

    val lock: Any

    var isFiltered: Boolean

    val filtersMap: HashMap<String, Any>
    val notAppliedFiltersMap: HashMap<String, Any>
    var filterPattern: String
    val filterKeyMap: MutableMap<String, List<Model>>

    @InternalAdaptersApi
    fun createModels(items: List<Parent>): List<Model> {
        return items.map { createModel(it) }
    }

    @InternalAdaptersApi
    fun createModel(item: Parent): Model

    fun add(item: Parent) {
        addModel(createModel(item))
        adapter.notifyItemInserted(adapter.itemCount)
    }

    fun add(items: List<Parent>) {
        val startPosition = adapter.itemCount
        addModels(createModels(items))
        adapter.notifyItemRangeInserted(startPosition, items.size)
    }

    fun add(position: Int, item: Parent) {
        val size = modelList.size
        if (position in 0..size) {
            addModel(position, createModel(item))
            adapter.notifyItemInserted(position)
        } else {
            throw IndexOutOfBoundsException("List size is $size. Your index is $position")
        }
    }

    fun add(position: Int, items: List<Parent>) {
        val size = modelList.size
        if (position in 0..size) {
            addModels(position, createModels(items))
            adapter.notifyItemRangeInserted(position, items.size)
        } else {
            throw IndexOutOfBoundsException("List size is $size. Your index is $position")
        }
    }

    fun addBefore(beforeItem: Parent, item: Parent) {
        val position = getPositionOfItem(beforeItem)
        add(position, item)
    }

    fun addBefore(beforeItem: Parent, items: List<Parent>) {
        val position = getPositionOfItem(beforeItem)
        add(position, items)
    }

    fun addAfter(afterItem: Parent, item: Parent) {
        val position = getPositionOfItem(afterItem)
        add(position + 1, item)
    }

    fun addAfter(afterItem: Parent, item: List<Parent>) {
        val position = getPositionOfItem(afterItem)
        add(position + 1, item)
    }

    @InternalAdaptersApi
    fun add(model: Model) {
        modelList.add(model)
    }

    @InternalAdaptersApi
    fun add(index: Int, model: Model) {
        modelList.add(index, model)
    }

    fun addAsync(list: List<Parent>, func: () -> Unit = {}) {
        addJob = scope.asynchronously {
            withLock {
                add(list)
                func.invoke()
            }
        }
    }

    /**
     * Updates already existing items and remove oldItems
     */
    fun update(items: List<Parent>): Boolean {
        removeOldItems(items)
        items.forEachIndexed { index, item ->

            if (updateAndNotify(item)) {
                val oldPosition = getPositionOfItem(item)
                if (oldPosition != index) adapter.notifyItemMoved(oldPosition, index)
            } else {
                add(index, item)
            }
        }

        return true
    }

    private fun removeOldItems(newList: List<Parent>) {
        val removeList = ArrayList<Model>()
        modelList.forEach { model ->
            val item = newList.find { item -> model.areItemsTheSame(item) }
            if (item == null) removeList.add(model)
        }

        removeModels(removeList)
    }


    @InternalAdaptersApi
    fun update(model: Model, item: Parent): List<AdapterParentViewModel.Payloadable>? {
        return if (!model.areContentsTheSame(item)) {
            model.payload(item)
        } else null
    }

    fun updateAndNotify(item: Parent): Boolean {
        val model = find(item)
        return if (model != null) {
            val payloads = update(model, item)

            if (payloads != null) {
                mainThreadIfNeeds {
                    notifyModelChanged(model, payloads)
                }
            }
            true
        } else false
    }

    @Throws(NotImplementedError::class)
    fun setFilter(query: String): Int {
        throw NotImplementedError()
    }

    @Throws(NotImplementedError::class)
    fun setFilterAsync(query: String, callback: (filteredCount: Int) -> Unit = {}) {
        throw NotImplementedError()
    }

    fun filter(model: Model, query: String): Boolean {
        return true
    }

    fun filter(model: Model, key: String, filterObj: Any): Boolean {
        return true
    }

    @Throws(IllegalArgumentException::class)
    @InternalAdaptersApi
    fun notifyModelChanged(
        model: Model,
        payloads: List<AdapterParentViewModel.Payloadable> = emptyList()
    ): Int = try {
        val position = getPositionOfModel(model)
        adapter.notifyItemChanged(position, payloads)
        position
    } catch (e: IllegalArgumentException) {
        Logger.log("Skip notify item change!")
        throw e
    }

//    @Throws(IllegalArgumentException::class)
//    @InternalAdaptersApi
//    fun notifyModelMoved(
//        oldPosition: Int,
//        newPosition: Int,
//        model: Model,
//        payloads: List<AdapterParentViewModel.Payloadable> = emptyList()
//    ) {
//        adapter.notifyItemMoved()
//    }

    @Throws(IllegalArgumentException::class)
    fun getPositionOfItem(item: Parent): Int {
        modelList.forEachIndexed { index, model ->
            if (model.areItemsTheSame(item)) return index
        }

        throw IllegalArgumentException("No data found")
    }

    @Throws(IllegalArgumentException::class)
    fun getPositionOfModel(model: Model): Int {
        modelList.forEachIndexed { index, t ->
            if (t == model) return index
        }

        throw IllegalArgumentException("No data found")
    }

    @InternalAdaptersApi
    fun find(item: Parent): Model? {
        modelList.forEach {
            if (it.areItemsTheSame(item)) {
                return it
            }
        }

        return null
    }

    fun getModelByPosition(position: Int): Model {
        return modelList[position]
    }

    fun getItemByPosition(position: Int): Parent {
        return getModelByPosition(position).item
    }

    @InternalAdaptersApi
    fun addModels(list: List<Model>) {
        modelList.addAll(list)
    }

    @InternalAdaptersApi
    fun addModel(model: Model) {
        modelList.add(model)
    }

    @InternalAdaptersApi
    fun addModel(position: Int, model: Model) {
        modelList.add(position, model)
    }

    private fun addModels(position: Int, list: List<Model>) {
        modelList.addAll(position, list)
    }

    @InternalAdaptersApi
    fun getModelByItem(item: Parent): Model? {
        return modelList.firstOrNull { it.areItemsTheSame(item) }
    }

    fun removeModels(list: List<Model>): Boolean {
        return if (list.isNotEmpty()) {
            list.forEach { remove(it) }
            //modelList.removeAll(list)
            filterKeyMap.clear()
            filterKeyMap.forEach { entry ->
                filterKeyMap[entry.key] = entry.value.toMutableList().apply { removeAll(list) }
            }

            true
        } else false
    }

    fun remove(item: Parent): Boolean {
        val foundObj = getModelByItem(item)

        return if (foundObj != null) {
            remove(foundObj)
        } else false
    }

    fun remove(items: List<Parent>): Boolean {
        val removeList = items.mapNotNull { getModelByItem(it) }
        removeModels(removeList)

        return true
    }

    @InternalAdaptersApi
    fun remove(model: Model): Boolean {
        val position = getPositionOfModel(model)
        val removed = modelList.remove(model)
        if (removed) {
            adapter.notifyItemRemoved(position)
        }
        notifyPositionsChanged(position)

        return removed
    }

    fun notifyPositionsChanged(startsWithPosition: Int) {
        if (startsWithPosition < adapter.itemCount - 1) {
            (startsWithPosition until adapter.itemCount).forEach { index ->
                modelList[index].onPositionChanged(index)
            }
        }
    }

    fun clear() {
        modelList.clear()
        adapter.notifyDataSetChanged()
    }

    fun getAllModels(): List<Model> {
        return modelList.toList()
    }

    /**
     * @return true if modelList has items else - false
     */
    fun isEmpty(): Boolean = modelList.isEmpty()

    fun isNotEmpty(): Boolean = !isEmpty()

    @Throws(IndexOutOfBoundsException::class)
    fun first(): Parent {
        try {
            return getModelByPosition(0).item
        } catch (e: Exception) {
            throw IndexOutOfBoundsException("List is empty")
        }
    }

    @Throws(IndexOutOfBoundsException::class)
    fun last(): Parent {
        try {
            return getModelByPosition(adapter.itemCount - 1).item
        } catch (e: Exception) {
            throw IndexOutOfBoundsException("List is empty")
        }
    }

    fun getAll(): List<Parent> {
        return modelList.map { it.item }
    }

    fun notifyAdapterRemoved() {}

    fun isPayloadsValid(payloads: List<AdapterParentViewModel.Payloadable>): Boolean {
        return payloads.isNotEmpty() &&
                !payloads.contains(AdapterParentViewModel.Payloadable.None)
    }

    fun onPayloadable(
        holder: TypedBindingHolder<Model>,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {
    }
}