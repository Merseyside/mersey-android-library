@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.delegates.composites

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.ItemCallback
import com.merseyside.adapters.callback.OnItemClickListener
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.adapters.utils.AdapterListUtils
import com.merseyside.adapters.utils.InternalAdaptersApi
import com.merseyside.adapters.view.TypedBindingHolder

abstract class CompositeAdapter<Parent, Model : AdapterParentViewModel<out Parent, Parent>>(
    val delegatesManager: DelegatesManager<Parent, Model> = DelegatesManager()
) : RecyclerView.Adapter<TypedBindingHolder<Model>>(),
    ItemCallback<AdapterViewModel<Parent>>,
    AdapterListUtils<Parent, Model> {

    override var listener: OnItemClickListener<Parent>? = null

    override val modelList: MutableList<Model> = ArrayList()
    override val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>
        get() = this

    protected var recyclerView: RecyclerView? = null

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
        delegatesManager.bindViewHolder(holder, getModelByPosition(position), position)
        listener?.let { holder.getModel().setOnItemClickListener(it) }
    }

    override fun getItemCount() = modelList.size

    override fun createModels(items: List<Parent>): List<Model> {
        return delegatesManager.createModels(items).onEach { model -> onModelCreated(model) }
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