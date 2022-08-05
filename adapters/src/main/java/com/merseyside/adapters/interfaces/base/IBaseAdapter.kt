@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces.base

import android.annotation.SuppressLint
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.ext.asynchronously
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.list.AdapterListChangeDelegate
import com.merseyside.merseyLib.kotlin.concurency.Locker
import com.merseyside.utils.mainThreadIfNeeds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex

@SuppressLint("NotifyDataSetChanged")
interface IBaseAdapter<Parent, Model : AdapterParentViewModel<out Parent, Parent>> :
    HasOnItemClickListener<Parent>, AdapterListActions<Parent, Model>, Locker {

    var delegate: AdapterListChangeDelegate<Parent, Model>

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

    @CallSuper
    fun add(item: Parent): Model? {
        return delegate.add(listOf(item)).firstOrNull()
    }

    /**
     * Delegates items adding to [AdapterListChangeDelegate]
     * @return items count that added
     */
    @CallSuper
    fun add(items: List<Parent>): List<Model> {
        return delegate.add(items)
    }

    fun addAsync(items: List<Parent>, func: () -> Unit = {}) {
        addJob = scope.asynchronously {
            withLock {
                add(items)
                func.invoke()
            }
        }
    }

    fun addOrUpdate(items: List<Parent>) {
        if (isEmpty()) add(items)
        else update(items)
    }


    fun update(items: List<Parent>): Boolean

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
    /**
     * Removes model by item
     * Calls onItemRemoved callback method on success.
     * @return position of removed item
     */
    @CallSuper
    fun remove(item: Parent): Boolean {
        val position = getPositionOfItem(item)
        return delegate.remove(position)
    }

    fun remove(items: List<Parent>) {
        items.forEach { remove(it) }
    }

    fun notifyAdapterRemoved() {}

    fun isPayloadsValid(payloads: List<AdapterParentViewModel.Payloadable>): Boolean {
        return payloads.isNotEmpty() &&
                !payloads.contains(AdapterParentViewModel.Payloadable.None)
    }

    fun onPayloadable(
        holder: TypedBindingHolder<Model>,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {}


    fun getItemsCount(): Int {
        return models.size
    }

    fun getItemByPosition(position: Int): Parent {
        return getModelByPosition(position).item
    }

    fun getModelByPosition(position: Int): Model {
        return models[position]
    }

    fun getModelByItem(item: Parent): Model? {
        return models.find { it.areItemsTheSame(item) }
    }

    fun getPositionOfModel(model: Model): Int {
        return getPositionOfItem(model.item).let { position ->
            if (position != SortedList.INVALID_POSITION) position
            else throw IllegalArgumentException("No data found")
        }
    }

    fun getPositionOfItem(item: Parent): Int {
        models.forEachIndexed { index, model ->
            if (model.areItemsTheSame(item)) return index
        }

        throw IllegalArgumentException("No data found")
    }

    fun find(item: Parent): Model? {
        return models.find {
            it.areItemsTheSame(item)
        }
    }

    fun clear()

    /**
     * @return true if modelList has items else - false
     */
    fun isEmpty(): Boolean = models.isEmpty()

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
        return models.map { it.item }
    }
}