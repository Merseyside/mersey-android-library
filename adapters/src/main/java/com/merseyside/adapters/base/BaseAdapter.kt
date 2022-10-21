@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.base

import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.delegate
import com.merseyside.adapters.base.config.hasFeature
import com.merseyside.adapters.config.workManager
import com.merseyside.adapters.callback.HasOnItemClickListener
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.listManager.AdapterListManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.merseyLib.kotlin.coroutines.CoroutineQueue
import com.merseyside.merseyLib.kotlin.logger.ILogger
import com.merseyside.utils.reflection.ReflectionUtils
import kotlinx.coroutines.Job

@Suppress("LeakingThis")
abstract class BaseAdapter<Parent, Model>(
    open val adapterConfig: AdapterConfig<Parent, Model>,
) : RecyclerView.Adapter<TypedBindingHolder<Model>>(),
    HasOnItemClickListener<Parent>, IBaseAdapter<Parent, Model>, ILogger
        where Model : AdapterParentViewModel<out Parent, Parent> {

    override val workManager: CoroutineQueue<Any, Unit> by adapterConfig.workManager()

    override val models: List<Model>
        get() = delegate.modelList

    override val delegate: AdapterListManager<Parent, Model> by adapterConfig.delegate()


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

    override val modelClass: Class<Model> by lazy {
        ReflectionUtils.getGenericParameterClass(
            this.javaClass,
            BaseAdapter::class.java,
            1
        ) as Class<Model>
    }

    init {
        adapterConfig.initAdapterWithConfig(this)
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
        return delegate.getItemCount()
    }

    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, position: Int) {
        val model = getModelByPosition(position)
        bindModel(holder, model, position)

        bindItemList.add(model)

        if (!isRecyclable || !holder.isRecyclable) {
            holder.setIsRecyclable(isRecyclable)
        }
    }

    @Suppress("UNCHECKED_CAST")
    internal abstract fun bindModel(
        holder: TypedBindingHolder<Model>,
        model: Model,
        position: Int
    )

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

    override fun hasFeature(key: String): Boolean {
        return adapterConfig.hasFeature(key)
    }

    override val tag: String = "BaseAdapter"
}