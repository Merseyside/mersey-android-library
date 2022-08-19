@file:OptIn(InternalAdaptersApi::class)
package com.merseyside.adapters.delegates.composites

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseCompositeAdapter<Parent, Model>(
    val delegatesManager: DelegatesManager<Parent, Model>,
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : BaseAdapter<Parent, Model>()
    where Model : AdapterParentViewModel<out Parent, Parent> {

    override val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>
        get() = this

    init {
        delegatesManager.setOnDelegateRemoveCallback { delegate ->
            val removeList = models.filter { delegate.isResponsibleFor(it.item) }
            remove(removeList.map { it.item })
        }
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
}