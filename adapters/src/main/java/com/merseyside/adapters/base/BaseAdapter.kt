@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.utils.AdapterListUtils
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.merseyLib.kotlin.concurency.Locker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Mutex

abstract class BaseAdapter<Item, Model : AdapterViewModel<Item>>(
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : RecyclerView.Adapter<TypedBindingHolder<Model>>(),
    ItemCallback<AdapterViewModel<Item>>,
    HasOnItemClickListener<Item>,
    AdapterListUtils<Item, Model>, Locker {

    override val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>
        get() = this

    protected var isRecyclable: Boolean = true

    override var listener: OnItemClickListener<Item>? = null

    override val modelList: MutableList<Model> = ArrayList()
    private val bindItemList: MutableList<Model> = ArrayList()
    protected var recyclerView: RecyclerView? = null

    override var addJob: Job? = null
    override var updateJob: Job? = null
    override var filterJob: Job? = null

    override var isFiltered: Boolean = false
    override val filtersMap: HashMap<String, Any> by lazy { HashMap() }
    override val notAppliedFiltersMap: HashMap<String, Any> by lazy { HashMap() }
    override var filterPattern: String = ""
    override val filterKeyMap: MutableMap<String, List<Model>> by lazy { HashMap() }

    override val lock = Any()
    override val mutex: Mutex = Mutex()

    protected abstract fun getLayoutIdForPosition(position: Int): Int
    protected abstract fun getBindingVariable(): Int
    protected abstract fun createItemViewModel(item: Item): Model

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount() = modelList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypedBindingHolder<Model> {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return getBindingHolder(binding)
    }

    open fun getBindingHolder(binding: ViewDataBinding): TypedBindingHolder<Model> {
        return TypedBindingHolder(binding)
    }

    @CallSuper
    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, position: Int) {
        val model = getModelByPosition(position)
        model.onPositionChanged(position)

        bindItemList.add(model)

        listener?.let { model.setOnItemClickListener(it) }
        bind(holder, model)

        if (!isRecyclable || isRecyclable && !holder.isRecyclable) {
            holder.setIsRecyclable(isRecyclable)
        }
    }

    override fun onBindViewHolder(
        holder: TypedBindingHolder<Model>,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val payloadable = payloads[0] as List<AdapterParentViewModel.Payloadable>

            if (isPayloadsValid(payloadable)) {
                onPayloadable(holder, payloadable)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    @CallSuper
    internal open fun bind(holder: TypedBindingHolder<Model>, model: Model) {
        holder.bind(getBindingVariable(), model)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    @CallSuper
    override fun onViewRecycled(holder: TypedBindingHolder<Model>) {
        super.onViewRecycled(holder)
        if (holder.absoluteAdapterPosition != RecyclerView.NO_POSITION && holder.absoluteAdapterPosition < itemCount) {

            getModelByPosition(holder.absoluteAdapterPosition).apply {
                bindItemList.remove(this)
                listener?.let {
                    removeOnItemClickListener(it)
                }
                onRecycled()
            }
        }
    }

    @InternalAdaptersApi
    override fun createModel(item: Item): Model {
        return initItemViewModel(item)
    }

    internal open fun initItemViewModel(item: Item): Model {
        return createItemViewModel(item).apply {
            setItemPositionInterface(this@BaseAdapter)
        }
    }

    open fun removeListeners() {
        listener = null
    }

    companion object {
        const val NO_ITEM = -1
    }
}
