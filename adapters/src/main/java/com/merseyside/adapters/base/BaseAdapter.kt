@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.base

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.contract.OnBindItemListener
import com.merseyside.adapters.config.ext.hasFeature
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.VM
import com.merseyside.adapters.utils.AdapterWorkManager
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.logger.ILogger
import com.merseyside.utils.reflection.ReflectionUtils
import kotlinx.coroutines.Job

abstract class BaseAdapter<Parent, Model>(
    override val adapterConfig: AdapterConfig<Parent, Model>,
) : RecyclerView.Adapter<TypedBindingHolder<Model>>(),
    HasOnItemClickListener<Parent>, IBaseAdapter<Parent, Model>, ILogger
        where Model : VM<Parent> {

    override val models: List<Model>
        get() = listManager.modelList

    override lateinit var workManager: AdapterWorkManager

    override var onBindItemListener: OnBindItemListener<Parent, Model>? = null

    @InternalAdaptersApi
    override val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>
        get() = this

    protected var isRecyclable: Boolean = true

    @InternalAdaptersApi
    override var clickListeners: MutableList<OnItemClickListener<Parent>> = ArrayList()

    internal val bindItemList: MutableList<Model> = ArrayList()
    protected var recyclerView: RecyclerView? = null

    override val provideModelByItem: suspend (Parent) -> Model = { item ->
        createModel(item).also { model ->
            onModelCreated(model)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getModelClass(): Class<Model> {
        return ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            BaseAdapter::class.java,
            1
        ) as Class<Model>
    }

    internal abstract fun createModel(item: Parent): Model

    override val callbackClick: (Parent) -> Unit = { item ->
        clickListeners.forEach { listener -> listener.onItemClicked(item) }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return listManager.getItemCount()
    }

    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, position: Int) {
        val model = getModelByPosition(position)
        bindModel(holder, model, position)

        bindItemList.add(model)
    }

    @Suppress("UNCHECKED_CAST")
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
    internal open fun bindModel(
        holder: TypedBindingHolder<Model>,
        model: Model,
        position: Int
    ) {
        onBindItemListener?.onBindViewHolder(holder, model, position)
    }

    open fun removeListeners() {
        removeAllClickListeners()
    }

    override fun hasFeature(key: String): Boolean {
        return adapterConfig.hasFeature(key)
    }

    fun <Result> doAsync(
        onComplete: (Result) -> Unit = {},
        onError: ((e: Exception) -> Unit)? = null,
        work: suspend () -> Result,
    ): Job? {
        return workManager.doAsync(onComplete, onError, work)
    }

    override val tag: String = "BaseAdapter"
}