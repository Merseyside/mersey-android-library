@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.base

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.feature.filter.FilterListChangeDelegate
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.listDelegates.ListChangeDelegate
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.ItemCallback
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineWorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

abstract class BaseAdapter<Parent, Model>(
    internal val scope: CoroutineScope
) : RecyclerView.Adapter<TypedBindingHolder<Model>>(),
    ItemCallback<AdapterViewModel<Parent>>,
    HasOnItemClickListener<Parent>,
    IBaseAdapter<Parent, Model>
        where Model : AdapterParentViewModel<out Parent, Parent> {

    internal val workManager = CoroutineWorkManager<Any, Unit>(scope = scope)

    override val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>
        get() = this

    protected var isRecyclable: Boolean = true

    @InternalAdaptersApi
    override var clickListeners: MutableList<OnItemClickListener<Parent>> = ArrayList()

    internal val bindItemList: MutableList<Model> = ArrayList()
    protected var recyclerView: RecyclerView? = null

    override val modelProvider: (Parent) -> Model = ::createModel

    internal abstract val defaultDelegate: ListChangeDelegate<Parent, Model>
    internal abstract val filterDelegate: FilterListChangeDelegate<Parent, Model>

    internal abstract fun createModel(item: Parent): Model

    override val onClick: (Parent) -> Unit = { item ->
        clickListeners.forEach { listener -> listener.onItemClicked(item) }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, position: Int) {
        val model = bindModel(holder, position)
        model.onPositionChanged(position)

        bindItemList.add(model)

        if (!isRecyclable || isRecyclable && !holder.isRecyclable) {
            holder.setIsRecyclable(isRecyclable)
        }
    }

    @Suppress("UNCHECKED_CAST")
    internal abstract fun bindModel(holder: TypedBindingHolder<Model>, position: Int): Model

    override fun onBindViewHolder(
        holder: TypedBindingHolder<Model>,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val payloadable = payloads.first() as List<AdapterParentViewModel.Payloadable>

            if (isPayloadsValid(payloadable)) {
                onPayloadable(holder, payloadable)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun <Result> doAsync(
        provideResult: (Result) -> Unit,
        work: suspend IBaseAdapter<Parent, Model>.() -> Result,
    ): Job? {
        return workManager.addAndExecute {
            val result = work()
            provideResult(result)
        }
    }

    open fun removeListeners() {
        removeAllClickListeners()
    }

    internal fun getModel(
        holder: TypedBindingHolder<Model>,
        position: Int
    ): Model {
        return if (holder.isInitialized) {
            holder.getModel()
        } else {
            getModelByPosition(position)
        }
    }
}
