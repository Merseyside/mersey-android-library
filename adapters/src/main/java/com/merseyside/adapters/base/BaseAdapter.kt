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
import com.merseyside.merseyLib.kotlin.concurency.Locker
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineWorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex

abstract class BaseAdapter<Parent, Model>(
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : RecyclerView.Adapter<TypedBindingHolder<Model>>(),
    ItemCallback<AdapterViewModel<Parent>>,
    HasOnItemClickListener<Parent>,
    IBaseAdapter<Parent, Model>, Locker
    where Model : AdapterParentViewModel<out Parent, Parent> {

    internal val workManager = CoroutineWorkManager<Any, Unit>(scope = coroutineScope)

    override val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>
        get() = this

    protected var isRecyclable: Boolean = true

    override var listener: OnItemClickListener<Parent>? = null

    internal val bindItemList: MutableList<Model> = ArrayList()
    protected var recyclerView: RecyclerView? = null

    override var addJob: Job? = null
    override var updateJob: Job? = null

    override val lock = Any()
    override val mutex: Mutex = Mutex()

    override val modelProvider: (Parent) -> Model = ::createModel

    internal abstract val defaultDelegate: ListChangeDelegate<Parent, Model>
    internal abstract val filterDelegate: FilterListChangeDelegate<Parent, Model>

    @InternalAdaptersApi
    abstract fun createModel(item: Parent): Model

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

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

    @CallSuper
    override fun onViewRecycled(holder: TypedBindingHolder<Model>) {
        super.onViewRecycled(holder)
        if (holder.absoluteAdapterPosition != RecyclerView.NO_POSITION &&
            holder.absoluteAdapterPosition < itemCount) {

            getModelByPosition(holder.absoluteAdapterPosition).apply {
                bindItemList.remove(this)
                listener?.let {
                    removeOnItemClickListener(it)
                }
                onRecycled()
            }
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
        listener = null
    }
}
