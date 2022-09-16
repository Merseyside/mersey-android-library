@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.interfaces.base

import android.annotation.SuppressLint
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.listDelegates.interfaces.AdapterListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.kotlin.contract.Identifiable
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.utils.measureAndLogTime
import kotlinx.coroutines.Job

@SuppressLint("NotifyDataSetChanged")
interface IBaseAdapter<Parent, Model> : AdapterListActions<Parent, Model>,
    HasOnItemClickListener<Parent>
        where Model : AdapterParentViewModel<out Parent, Parent> {

    @InternalAdaptersApi
    val delegate: AdapterListChangeDelegate<Parent, Model>

    @InternalAdaptersApi
    val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>

    val hashMap: MutableMap<Any, Model>

    @InternalAdaptersApi
    val onClick: (Parent) -> Unit

    @CallSuper
    fun addAsync(item: Parent, onComplete: (Model?) -> Unit = {}) {
        doAsync(onComplete) { add(item) }
    }

    suspend fun add(item: Parent): Model? {
        return delegate.add(listOf(item)).firstOrNull()
    }


    fun addAsync(items: List<Parent>, onComplete: (Unit) -> Unit = {}) {
        doAsync(onComplete) { add(items) }
    }

    /**
     * Delegates items adding to [AdapterListChangeDelegate]
     * @return Added models
     */
    suspend fun add(items: List<Parent>) {
        delegate.add(items)
    }

    fun addOrUpdateAsync(items: List<Parent>, onComplete: (Unit) -> Unit = {}) {
        doAsync(onComplete) { addOrUpdate(items) }
    }

    suspend fun addOrUpdate(items: List<Parent>) {
        delegate.addOrUpdate(items)
    }

    fun updateAsync(updateRequest: UpdateRequest<Parent>, provideResult: (Boolean) -> Unit = {}) {
        doAsync(provideResult) { update(updateRequest) }
    }

    suspend fun update(updateRequest: UpdateRequest<Parent>): Boolean {
        return measureAndLogTime("updateTime") {
            delegate.update(updateRequest)
        }
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

    @InternalAdaptersApi
    @CallSuper
    suspend fun onModelCreated(model: Model) {
        model.clickEvent.observe(onClick)
    }

    suspend fun notifyModelUpdated(model: Model, payloads: List<AdapterParentViewModel.Payloadable>)

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

    fun getItemCount(): Int

    fun getLastPositionIndex(): Int = getItemCount() - 1

    fun getItemByPosition(position: Int): Parent {
        return getModelByPosition(position).item
    }

    fun getModelByPosition(position: Int): Model {
        return models[position]
    }

    suspend fun getPositionOfModel(model: Model): Int

    fun getModelByItemAsync(item: Parent, onComplete: (Model?) -> Unit) {
        doAsync(onComplete) { getModelByItem(item) }
    }

    @InternalAdaptersApi
    suspend fun getModelByItem(item: Parent): Model? {
        return delegate.getModelByItem(item)
    }


    suspend fun getPositionOfItem(item: Parent): Int {
        val model = getModelByItem(item)
        return if (model != null) {
            getPositionOfModel(model)
        } else -1
    }

    suspend fun containsModel(model: Model): Boolean {
        return models.contains(model)
    }

    fun find(item: Parent): Model? {
        return models.find {
            it.areItemsTheSame(item)
        }
    }

    @CallSuper
    fun clearAsync(onComplete: (Unit) -> Unit = {}) {
        doAsync(onComplete) { clear() }
    }

    suspend fun clear() {
        delegate.clear()
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