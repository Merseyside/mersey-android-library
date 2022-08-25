@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces.base

import android.annotation.SuppressLint
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.merseyside.adapters.callback.HasOnItemClickListener
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
interface IBaseAdapter<Parent, Model> : AdapterListActions<Parent, Model>,
    Locker, HasOnItemClickListener<Parent>
        where Model : AdapterParentViewModel<out Parent, Parent> {

    val delegate: AdapterListChangeDelegate<Parent, Model>

    val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>

    var addJob: Job?
    var updateJob: Job?

    val scope: CoroutineScope
    override val mutex: Mutex

    val lock: Any

    @CallSuper
    fun addAsync(item: Parent, onComplete: (Model?) -> Unit = {}) {
        doAsync(onComplete) { add(item) }
    }

    suspend fun add(item: Parent): Model? {
        return delegate.add(listOf(item)).firstOrNull()
    }

    /**
     * Delegates items adding to [AdapterListChangeDelegate]
     * @return Added models
     */
    @CallSuper
    fun addAsync(items: List<Parent>, onComplete: (List<Model>) -> Unit = {}) {
        doAsync(onComplete) { add(items) }
    }

    suspend fun add(items: List<Parent>): List<Model> {
        return delegate.add(items)
    }

    fun addOrUpdateAsync(items: List<Parent>, onComplete: (Unit) -> Unit = {}) {
        doAsync(onComplete) { addOrUpdate(items) }
    }

    suspend fun addOrUpdate(items: List<Parent>) {
        if (isEmpty()) add(items)
        else update(UpdateRequest(items))
    }

    fun updateAsync(updateRequest: UpdateRequest<Parent>, provideResult: (Boolean) -> Unit = {}) {
        doAsync(provideResult) { update(updateRequest) }
    }

    suspend fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        return delegate.update(updateRequest)
    }

    fun updateAsync(items: List<Parent>, onComplete: (Boolean) -> Unit = {}) {
        doAsync(onComplete) { update(items) }
    }

    suspend fun update(items: List<Parent>): Boolean {
        return delegate.update(UpdateRequest(items))
    }

    @InternalAdaptersApi
    override suspend fun updateModel(model: Model, item: Parent): Boolean {
        val payload = model.payload(item)
        return if (containsModel(model)) {
            notifyModelUpdated(model, payload)
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
    fun removeAsync(item: Parent, onComplete: (Model?) -> Unit = {}) {
        doAsync(onComplete) { remove(item) }
    }

    suspend fun remove(item: Parent): Model? {
        return delegate.remove(item)
    }

    fun removeAsync(items: List<Parent>, onComplete: (List<Model>) -> Unit = {}) {
        doAsync(onComplete) { remove(items) }
    }

    suspend fun remove(items: List<Parent>): List<Model> {
        return delegate.remove(items)
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


    fun getItemsCount() = models.size

    fun getLastPositionIndex(): Int = getItemsCount() - 1

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

    fun containsModel(model: Model): Boolean {
        return models.contains(model)
    }

    fun find(item: Parent): Model? {
        return models.find {
            it.areItemsTheSame(item)
        }
    }

    @CallSuper
    fun clear() {
        doAsync { delegate.removeAll() }
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
            return getModelByPosition(getLastPositionIndex()).item
        } catch (e: Exception) {
            throw IndexOutOfBoundsException("List is empty")
        }
    }

    fun getAll(): List<Parent> {
        return models.map { it.item }
    }

    fun <Result> doAsync(
        provideResult: (Result) -> Unit = {},
        work: suspend IBaseAdapter<Parent, Model>.() -> Result,
    ): Job?
}