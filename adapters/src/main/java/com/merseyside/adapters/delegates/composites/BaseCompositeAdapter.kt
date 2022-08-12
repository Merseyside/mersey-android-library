@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.delegates.composites

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.utils.ItemCallback
import com.merseyside.adapters.utils.list.AdapterListChangeDelegate
import com.merseyside.adapters.utils.list.DefaultListChangeDelegate
import com.merseyside.merseyLib.kotlin.concurency.Locker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Mutex

abstract class BaseCompositeAdapter<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    val delegatesManager: DelegatesManager<Parent, Model>,
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : RecyclerView.Adapter<TypedBindingHolder<Model>>(),
    IBaseAdapter<Parent, Model>,
    ItemCallback<AdapterViewModel<Parent>>, Locker {

    override var addJob: Job? = null
    override var updateJob: Job? = null

    override val lock = Any()
    override val mutex: Mutex = Mutex()

    override var listener: OnItemClickListener<Parent>? = null
    override val modelProvider: (Parent) -> Model = ::createModel


    override val delegate: AdapterListChangeDelegate<Parent, Model> by lazy {
        DefaultListChangeDelegate(this)
    }

    override val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>
        get() = this

    protected var recyclerView: RecyclerView? = null

    init {
        delegatesManager.setOnDelegateRemoveCallback { delegate ->
            val removeList = models.filter { delegate.isResponsibleFor(it.item) }
            remove(removeList.map { it.item })
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getViewTypeByItem(getModelByPosition(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypedBindingHolder<Model> {
        return delegatesManager.createViewHolder(parent, viewType)
    }

    @CallSuper
    override fun onBindViewHolder(holder: TypedBindingHolder<Model>, position: Int) {
        delegatesManager.onBindViewHolder(holder, getModelByPosition(position), position)
        listener?.let { holder.getModel().setOnItemClickListener(it) }
    }

    override fun getItemCount() = models.size

    override fun createModel(item: Parent): Model {
        val model = delegatesManager.createModel(item)
        onModelCreated(model)
        return model
    }

    @CallSuper
    protected open fun onModelCreated(model: Model) {
        listener?.let {
            model.setOnItemClickListener(it)
        }
    }

    open fun removeListeners() {
        listener = null
    }
}