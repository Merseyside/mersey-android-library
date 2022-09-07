package com.merseyside.adapters.delegates.composites

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.delegates.DelegatesManager
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi
import kotlinx.coroutines.CoroutineScope

abstract class BaseCompositeAdapter<Parent, Model>(
    scope: CoroutineScope,
    val delegatesManager: DelegatesManager<Parent, Model>,
) : BaseAdapter<Parent, Model>(scope)
    where Model : AdapterParentViewModel<out Parent, Parent> {

    override val adapter: RecyclerView.Adapter<TypedBindingHolder<Model>>
        get() = this

    init {
        delegatesManager.setOnDelegateRemoveCallback { delegate ->
            val removeList = models.filter { delegate.isResponsibleFor(it.item) }
            removeAsync(removeList.map { it.item })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getViewTypeByItem(getModelByPosition(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypedBindingHolder<Model> {
        return delegatesManager.createViewHolder(parent, viewType)
    }

    override fun bindModel(holder: TypedBindingHolder<Model>, model: Model, position: Int) {
        delegatesManager.onBindViewHolder(holder, model, position)
    }

    override fun getItemCount() = models.size

    @InternalAdaptersApi
    override fun createModel(item: Parent): Model {
        return delegatesManager.createModel(item)
    }
}