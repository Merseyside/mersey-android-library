@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces.base

import android.annotation.SuppressLint
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.extensions.asynchronously
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.listDelegates.interfaces.AdapterListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.kotlin.concurency.Locker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex

@SuppressLint("NotifyDataSetChanged")
interface IBaseAdapter<Parent, Model : AdapterParentViewModel<out Parent, Parent>> :
    HasOnItemClickListener<Parent>, AdapterListActions<Parent, Model>, Locker {

    val delegate: AdapterListChangeDelegate<Parent, Model>

    val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>

    var addJob: Job?
    var updateJob: Job?

    val scope: CoroutineScope
    override val mutex: Mutex

    val lock: Any

    @InternalAdaptersApi
    fun createModels(items: List<Parent>): List<Model> {
        return items.map { createModel(it) }
    }

    @InternalAdaptersApi
    fun createModel(item: Parent): Model

    @CallSuper
    fun add(item: Parent) {
        delegate.add(listOf(item))
    }

    /**
     * Delegates items adding to [AdapterListChangeDelegate]
     * @return items count that added
     */
    @CallSuper
    fun add(items: List<Parent>) {
        delegate.add(items)
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

    fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        return delegate.update(updateRequest)
    }

    fun update(items: List<Parent>): Boolean {
        return update(
            UpdateRequest.Builder(items)
                .isDeleteOld(true)
                .build()
        )
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

    @InternalAdaptersApi
    override fun updateModel(model: Model, item: Parent): Boolean {
        return if (!model.areContentsTheSame(item)) {
            notifyModelUpdated(model, model.payload(item))
            true
        } else false
    }

    fun notifyModelUpdated(model: Model, payloads: List<AdapterParentViewModel.Payloadable>)

    /**
     * Removes model by item
     * Calls onItemRemoved callback method on success.
     * @return position of removed item
     */
    @CallSuper
    fun remove(item: Parent): Boolean {
        return delegate.remove(item) != null
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

    @CallSuper
    fun clear() {
        delegate.removeAll()
    }

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